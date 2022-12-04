package org.uwindsor.mac.f22.acc.compute_engine.engine.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.Count.FrequencyCount;
import org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex.Index;
import org.uwindsor.mac.f22.acc.compute_engine.InvertedIndex.StringToText;
import org.uwindsor.mac.f22.acc.compute_engine.engine.parser.KayakParser;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchRequest;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Vivek
 * @since 22/11/22
 */
@Slf4j
@Component
public class KayakEngine {

    private static final String KAYAK_ENDPOINT_STRING = "https://www.ca.kayak.com/flights/%s-%s/%04d-%02d-%02d/%s%dadults?sort=bestflight_a";
    //https://www.ca.kayak.com/flights/YQG-YUL/2022-12-21/2adults?sort=bestflight_a

    @Autowired
    private WebDriver driver;

    public List<SearchResponse> getInformationFromKayak(SearchRequest searchRequest, int seleniumWaitTime) {
        log.info("Starting exec of search request part for Kayak!");

        String kayakUrl = getKayakEndpointString(searchRequest);
        log.info("Will be hitting {}", kayakUrl);
        driver.get(kayakUrl);

        try {
            Thread.sleep(seleniumWaitTime * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String pageSource = driver.getPageSource();
        try {
            Index.FlightIni(pageSource,1);
            FrequencyCount.Frequency(pageSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(pageSource);

        driver.manage().deleteAllCookies();

        List<SearchResponse> kayakSearchResponses = KayakParser.parseKayakData(pageSource);
        log.info("Found {} responses from Kayak", kayakSearchResponses.size());
        return kayakSearchResponses;
    }

    String getKayakEndpointString(SearchRequest searchRequest) {
        int year = searchRequest.getTravelDate() / 10000;
        int buffer = searchRequest.getTravelDate() % 10000;
        int month = buffer / 100;
        int day = buffer % 100;

        return String.format(KAYAK_ENDPOINT_STRING,
                searchRequest.getFrom().toUpperCase(),
                searchRequest.getTo().toUpperCase(),
                year,
                month,
                day,
                searchRequest.getFlightClass().equals("business") ? "business/" : "",
                searchRequest.getNumberOfPassengers()
        );
    }
}