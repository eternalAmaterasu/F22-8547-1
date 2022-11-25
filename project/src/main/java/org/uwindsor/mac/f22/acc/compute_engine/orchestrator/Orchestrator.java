package org.uwindsor.mac.f22.acc.compute_engine.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.engine.selenium.KayakEngine;
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

//    @Autowired
//    private SkyScannerEngine skyScannerEngine;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() throws IOException {
        log.info("Spring boot startup is complete and application is ready! Entering console mode!");

        log.info("**** WELCOME TO FLIGHT PRICE EXTRACTION SERVICE BY COMPUTE-ENGINE TEAM ****");

        while (true) {
            SearchRequest searchRequest = getSearchRequest();
            if (searchRequest == null) {
                break;
            }
            log.info("Search request received: {}", searchRequest);
            List<SearchResponse> responses = kayakEngine.getInformationFromKayak(searchRequest, 21);
            responses.sort(Comparator.comparingDouble(SearchResponse::getBestDealPrice));
            log.info("Search response list in increasing order of cost: \n{}", responses);
        }
        shutdown();
    }

    private SearchRequest getSearchRequest() throws IOException {
        log.info("Chose whether you want to enter airport code [1] or airport city name [2] or exit [3]?");
        int input = Integer.parseInt(in.readLine());
        if (input == 3) return null;

        boolean isCodeSelected = input == 1;
        log.info("Enter the source: ");
        String src = in.readLine();
        if (isCodeSelected) {
            src = processSourceOrDestinationOnCode(src);
        } else {
            src = processSourceOrDestinationOnName(src);
        }

        log.info("Enter the destination: ");
        String dest = in.readLine();
        if (isCodeSelected) {
            dest = processSourceOrDestinationOnCode(dest);
        } else {
            dest = processSourceOrDestinationOnName(dest);
        }

        log.info("Enter the number of adult passengers: ");
        int numberOfPassengers = Integer.parseInt(in.readLine());

        log.info("Enter the travel date in yyyyMMdd format: ");
        int travelDate = Integer.parseInt(in.readLine());

        log.info("Enter the flight class:\n1 - Economy\n2 - Business");
        int flightClass = Integer.parseInt(in.readLine());

        return new SearchRequest(src, dest, numberOfPassengers, travelDate, flightClass == 1 ? "economy" : "business");
    }

    private String processSourceOrDestinationOnName(String src) throws IOException {
        List<AirportData> airportData = computeEngineService.getAirportDataList(src);
        if (!airportData.isEmpty()) {
            if (airportData.size() == 1) return airportData.get(0).getCode();
            log.info("Looks like there are multiple cities for the same name. Please view the airport city you want: ");
            for (int i = 0; i < airportData.size(); i++) log.info("{} - {} : {} : {}", i + 1, airportData.get(i).getCity(), airportData.get(i).getCountry(), airportData.get(i).getCountry());
            int selection = Integer.parseInt(in.readLine());
            return airportData.get(selection - 1).getCode();
        }

        List<String> autoCompleteCities = computeEngineService.getAutoCompleteForPrefixOnNames(src);
        if (!autoCompleteCities.isEmpty()) {
            log.info("Please view the airport city you want:-");
            for (int i = 0; i < autoCompleteCities.size(); i++) log.info("{} - {}", i + 1, autoCompleteCities.get(i));
            log.info("Please enter the number of airport city you want: ");
            int selection = Integer.parseInt(in.readLine());
            airportData = computeEngineService.getAirportDataList(autoCompleteCities.get(selection - 1));
            if (airportData.size() == 1) return airportData.get(0).getCode();
            log.info("Looks like there are multiple cities for the same name. Please enter the number of the airport city you want: ");
            for (int i = 0; i < airportData.size(); i++) log.info("{} - {} : {} : {}", i + 1, airportData.get(i).getCity(), airportData.get(i).getCountry(), airportData.get(i).getCode());
            selection = Integer.parseInt(in.readLine());
            return airportData.get(selection - 1).getCode();
        }
        log.warn("Found no city names to autocomplete, thus switching to spell checking!");
        List<String> topNearestCodes = computeEngineService.getTopNNearestCityCodesBasedOnNames(src, 5);
        log.info("Please view the airport code you want:-");
        for (int i = 0; i < topNearestCodes.size(); i++) log.info("{} - {}", i + 1, topNearestCodes.get(i));
        log.info("Please enter the number of airport code you want: ");
        int selection = Integer.parseInt(in.readLine());
        return topNearestCodes.get(selection - 1);
    }

    private String processSourceOrDestinationOnCode(String src) throws IOException {
        if (computeEngineService.isCodePresent(src)) return src;

        List<String> autoCompleteCodes = computeEngineService.getAutoCompleteForPrefixOnCodes(src);
        //log.info(autoCompleteCodes.toString());
        if (!autoCompleteCodes.isEmpty()) {
            log.info("Please view the airport code you want:-");
            for (int i = 0; i < autoCompleteCodes.size(); i++) log.info("{} - {}", i + 1, autoCompleteCodes.get(i));
            log.info("Please enter the number of airport code you want: ");
            int selection = Integer.parseInt(in.readLine());
            return autoCompleteCodes.get(selection - 1);
        }
        log.warn("Found no city names to autocomplete, thus switching to spell checking!");
        List<String> topNearestCodes = computeEngineService.getTopNNearestCityCodes(src, 5);
        log.info("Please view the airport code you want:-");
        for (int i = 0; i < topNearestCodes.size(); i++) log.info("{} - {}", i + 1, topNearestCodes.get(i));
        log.info("Please enter the number of airport code you want: ");
        int selection = Integer.parseInt(in.readLine());
        return topNearestCodes.get(selection - 1);
    }

    private void shutdown() {
        log.info("Bye.");
        System.exit(0);
    }
}