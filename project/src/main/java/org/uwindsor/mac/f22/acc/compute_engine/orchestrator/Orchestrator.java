package org.uwindsor.mac.f22.acc.compute_engine.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchRequest;
import org.uwindsor.mac.f22.acc.compute_engine.service.ComputeEngineService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            log.info(computeEngineService.getAutoCompleteForPrefixOnCodes(searchRequest.getFrom()).toString());

            /*log.info(computeEngineService.getAutoCompleteForPrefixOnCodes("y").toString());
            log.info(computeEngineService.getAutoCompleteForPrefixOnCodes("e").toString());
            log.info(computeEngineService.getAutoCompleteForPrefixOnCodes("bo").toString());

            log.info(computeEngineService.getAutoCompleteForPrefixOnNames("bom").toString());
            log.info(computeEngineService.getAutoCompleteForPrefixOnNames("wi").toString());*/
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
        //check if src matches any of the existing names or codes - present the auto complete option if any is matching or present the best matching strings?

        log.info("Enter the destination: ");
        String dest = in.readLine();

        return new SearchRequest(src, dest, 2, 20221226, "economy");
    }

    private void shutdown() {
        log.info("Bye.");
        System.exit(0);
    }
}