package org.uwindsor.mac.f22.acc.compute_engine.engine.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex.Index;
import org.uwindsor.mac.f22.acc.compute_engine.engine.parser.SkyScannerParser;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchRequest;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Vivek
 * @since 20/11/22
 */
@Slf4j
@Component
public class SkyScannerEngine {

    private static final String SKYSCANNER_ENDPOINT_STRING = "https://www.skyscanner.ca/transport/flights/%s/%s/%s/?adults=%d&adultsv2=1&cabinclass=%s";
    //https://www.skyscanner.ca/transport/flights/bom/del/221123/?adults=1&adultsv2=1&cabinclass=economy

    @Autowired
    private WebDriver driver;

    public List<SearchResponse> getInformationFromSkyScanner(SearchRequest searchRequest, int seleniumWaitTime) {
        log.info("Starting exec of search request part for SkyScanner!");

        String skyScannerUrl = getSkyScannerEndpointString(searchRequest);
        log.info("Will be hitting {}", skyScannerUrl);
        driver.get(skyScannerUrl);

        try {
            Thread.sleep(seleniumWaitTime * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = driver.getPageSource();
        //System.out.println(pageSource);
        try {
            Index.FlightIni(pageSource,2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driver.manage().deleteAllCookies();

        List<SearchResponse> skyScannerSearchResponses = SkyScannerParser.parseSkyScannerData(pageSource);
        log.info("Found {} responses from SkyScanner", skyScannerSearchResponses.size());
        return skyScannerSearchResponses;
    }

    String getSkyScannerEndpointString(SearchRequest searchRequest) {
        int year = (searchRequest.getTravelDate() / 10000) % 100;
        int buffer = searchRequest.getTravelDate() % 10000;
        int month = buffer / 100;
        int day = buffer % 100;

        return String.format(SKYSCANNER_ENDPOINT_STRING,
                searchRequest.getFrom().toLowerCase(),
                searchRequest.getTo().toLowerCase(),
                String.format("%02d%02d%02d", year, month, day),
                searchRequest.getNumberOfPassengers(),
                searchRequest.getFlightClass()
        );
    }
}