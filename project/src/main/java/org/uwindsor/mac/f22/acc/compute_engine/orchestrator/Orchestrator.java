package org.uwindsor.mac.f22.acc.compute_engine.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.engine.selenium.KayakEngine;
import org.uwindsor.mac.f22.acc.compute_engine.engine.selenium.SkyScannerEngine;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchRequest;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;
import org.uwindsor.mac.f22.acc.compute_engine.service.ComputeEngineService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

/**
 * @author Vivek
 * @since 24/11/22
 */
@Slf4j
@Component
public class Orchestrator {

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    @Autowired
    private ComputeEngineService computeEngineService;

    @Autowired
    private KayakEngine kayakEngine;

    @Autowired
    private SkyScannerEngine skyScannerEngine;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() throws IOException {
        log.info("Spring boot startup is complete and application is ready! Entering console mode!");
        log.info("**** WELCOME TO FLIGHT PRICE EXTRACTION SERVICE BY COMPUTE-ENGINE TEAM ****");

        while (true) {
            log.info("\n");
            SearchRequest searchRequest = getSearchRequest();
            if (searchRequest == null) {
                break;
            }
            log.info("Search request received: {}", searchRequest);
            List<SearchResponse> responses = kayakEngine.getInformationFromKayak(searchRequest, 15); // firing up search on kayak
            responses.addAll(skyScannerEngine.getInformationFromSkyScanner(searchRequest, 5)); //firing up search on skyscanner
            responses.sort(Comparator.comparingDouble(SearchResponse::getBestDealPrice)); //sorting the combined results on the basis of the best deal price
            log.info("Search response list in increasing order of cost:");
            responses.forEach(searchResponse -> log.info(searchResponse.toString())); //printing out the result on each line
        }
        shutdown();
    }

    private SearchRequest getSearchRequest() throws IOException {
        log.info("Chose whether you want to enter airport code [1] or airport city name [2] or exit [-1]?");
        int input = Integer.parseInt(in.readLine()); //get the choice if user wants to input code or city
        if (input == -1) return null;

        boolean isCodeSelected = input == 1;
        String codeOrCity = isCodeSelected ? " code" : " city";
        log.info("Enter the source{}: ", codeOrCity); //request to input source city or code
        String src = in.readLine().toLowerCase();
        if (isCodeSelected) {
            src = processSourceOrDestinationOnCode(src); //Process the source on the basis of code
        } else {
            src = processSourceOrDestinationOnName(src); //Process the source on the basis of city
        }

        log.info("Enter the destination{}: ", codeOrCity); //request to input destination city or code
        String dest = in.readLine().toLowerCase();
        if (isCodeSelected) {
            dest = processSourceOrDestinationOnCode(dest); //Process the destination on the basis of code
        } else {
            dest = processSourceOrDestinationOnName(dest); //Process the destination on the basis of city
        }

        log.info("Enter the number of adult passengers: ");
        int numberOfPassengers = Integer.parseInt(in.readLine()); //get the number of adult passengers
        if (numberOfPassengers <= 0 || numberOfPassengers > 16) {
            log.warn("Incorrect number of passengers entered: {}. Please try again", numberOfPassengers);
            return null;
        }

        log.info("Enter the travel date in yyyyMMdd format: ");
        int travelDate = Integer.parseInt(in.readLine()); //get the travel date in yyyyMMdd format

        log.info("Enter the flight class:\n1 - Economy\n2 - Business");
        int flightClass = Integer.parseInt(in.readLine()); //get the class of travel

        return new SearchRequest(src, dest, numberOfPassengers, travelDate, flightClass == 1 ? "economy" : "business"); //generate the SearchRequest class
    }

    private String processSourceOrDestinationOnName(String cityName) throws IOException {
        List<AirportData> airportData = computeEngineService.getAirportDataList(cityName);
        if (!airportData.isEmpty()) {
            if (airportData.size() == 1) return airportData.get(0).getCode(); // if the search based on city name gives an exact match
            log.info("Looks like there are multiple cities for the same name. Please view the airport city you want: "); //otherwise
            for (int i = 0; i < airportData.size(); i++) log.info("{} - {} : {} : {}", i + 1, airportData.get(i).getCity(), airportData.get(i).getCountry(), airportData.get(i).getCountry());
            int selection = Integer.parseInt(in.readLine()); //take selection from user on the city they want
            return airportData.get(selection - 1).getCode(); //finalize the city and extract and return it's code
        }

        List<String> autoCompleteCities = computeEngineService.getAutoCompleteForPrefixOnNames(cityName); //if no exact matches from the airport data list then trigger autocomplete feature
        if (!autoCompleteCities.isEmpty()) { //if autocomplete gives an result
            log.info("Please view the airport city you want:-");
            for (int i = 0; i < autoCompleteCities.size(); i++) log.info("{} - {}", i + 1, autoCompleteCities.get(i));
            log.info("Please enter the number of airport city you want: ");
            int selection = Integer.parseInt(in.readLine()); //get user input on which autocompleted city they want
            airportData = computeEngineService.getAirportDataList(autoCompleteCities.get(selection - 1)); //extract airportdata based on that city
            if (airportData.size() == 1) return airportData.get(0).getCode(); //if only unique code then get the first code
            log.info("Looks like there are multiple cities for the same name. Please enter the number of the airport city you want: ");
            for (int i = 0; i < airportData.size(); i++) log.info("{} - {} : {} : {}", i + 1, airportData.get(i).getCity(), airportData.get(i).getCountry(), airportData.get(i).getCode());
            selection = Integer.parseInt(in.readLine()); //get user selection on the city they want
            return airportData.get(selection - 1).getCode(); //finalize the city and extract and return it's code
        }
        log.warn("Found no city names to autocomplete, thus switching to spell checking!"); //autocomplete turned up empty, so now go for spell checking and edit distance
        List<String> topNearestCodes = computeEngineService.getTopNNearestCityCodesBasedOnNames(cityName, 5); //find best top 5 city names based on input
        log.info("Please view the airport code you want:-");
        for (int i = 0; i < topNearestCodes.size(); i++) log.info("{} - {}", i + 1, topNearestCodes.get(i));
        log.info("Please enter the number of airport code you want: ");
        int selection = Integer.parseInt(in.readLine()); //get user selection on the city they want
        return topNearestCodes.get(selection - 1); //finalize the city and extract and return it's code
    }

    private String processSourceOrDestinationOnCode(String src) throws IOException {
        if (computeEngineService.isCodePresent(src)) return src; //if the code entered matches exactly the map then return that code

        List<String> autoCompleteCodes = computeEngineService.getAutoCompleteForPrefixOnCodes(src); //if no direct matches, go for autocomplete on code
        //log.info(autoCompleteCodes.toString());
        if (!autoCompleteCodes.isEmpty()) {
            log.info("Please view the airport code you want:-");
            for (int i = 0; i < autoCompleteCodes.size(); i++) log.info("{} - {}", i + 1, autoCompleteCodes.get(i));
            log.info("Please enter the number of airport code you want: ");
            int selection = Integer.parseInt(in.readLine()); //get user selection on the city they want
            return autoCompleteCodes.get(selection - 1); //finalize the city code and return it
        }
        log.warn("Found no city names to autocomplete, thus switching to spell checking!"); //autocomplete turned up empty, so now go for spell checking and edit distance
        List<String> topNearestCodes = computeEngineService.getTopNNearestCityCodes(src, 5); //find best top 5 city codes based on input
        log.info("Please view the airport code you want:-");
        for (int i = 0; i < topNearestCodes.size(); i++) log.info("{} - {}", i + 1, topNearestCodes.get(i));
        log.info("Please enter the number of airport code you want: ");
        int selection = Integer.parseInt(in.readLine()); //get user selection on the city they want
        return topNearestCodes.get(selection - 1); //finalize the city code and return it
    }

    private void shutdown() {
        log.info("Bye.");
        System.exit(0);
    }
}