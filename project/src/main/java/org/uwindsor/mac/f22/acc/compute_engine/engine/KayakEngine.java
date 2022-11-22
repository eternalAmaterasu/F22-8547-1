package org.uwindsor.mac.f22.acc.compute_engine.engine;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchRequest;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vivek
 * @since 22/11/22
 */
@Slf4j
@Component
public class KayakEngine {

    private static final String KAYAK_ENDPOINT_STRING = "https://www.ca.kayak.com/flights/%s-%s/%04d-%02d-%02d/%dadults?sort=bestflight_a";

    @Autowired
    private WebDriver driver;

    public List<SearchResponse> getInformationFromKayak(SearchRequest searchRequest, int seleniumWaitTime) {
        log.info("Starting exec of search request part!");

        int year = searchRequest.getTravelDate() / 10000;
        int buffer = searchRequest.getTravelDate() % 10000;
        int month = buffer / 100;
        int day = buffer % 100;

        String kayakUrl = String.format(KAYAK_ENDPOINT_STRING,
                searchRequest.getFrom().toUpperCase(),
                searchRequest.getTo().toUpperCase(),
                year,
                month,
                day,
                searchRequest.getNumberOfPassengers()
        );
        log.info("Will be hitting {}", kayakUrl);

        driver.get(kayakUrl);

        try {
            Thread.sleep(seleniumWaitTime * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(driver.getPageSource());

        driver.close();

        List<SearchResponse> searchResponses = new ArrayList<>();
        return searchResponses;
    }
}