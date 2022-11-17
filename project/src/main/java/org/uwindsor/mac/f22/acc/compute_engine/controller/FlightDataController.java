package org.uwindsor.mac.f22.acc.compute_engine.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uwindsor.mac.f22.acc.compute_engine.engine.ComputeEngine;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;

import java.util.Date;
import java.util.Map;

/**
 * @author Vivek
 * @since 04/11/22
 */
@Slf4j
@RestController
@RequestMapping("/flight")
public class FlightDataController {

    @Autowired
    @Qualifier("codeXAirportMap")
    private Map<String, AirportData> codeAndAirportDataMap;

    @Autowired
    @Qualifier("cityXAirportMap")
    private Map<String, AirportData> cityAndAirportDataMap;

    @Autowired
    private ComputeEngine computeEngine;

    @GetMapping("/time/system")
    public String getSystemTime() {
        return (new Date()).toString();
    }

    @GetMapping("/data/nearest/code")
    public String getNearestCode(@RequestParam("input") String code, @RequestParam(value = "topRelevantMatchCount", defaultValue = "3") int topN) {
        log.info(codeAndAirportDataMap.toString());
        code = code.toLowerCase();
        return computeEngine.getTopNNearestStrings(code, codeAndAirportDataMap.keySet(), topN).toString();
    }

    @GetMapping("/data/nearest/city")
    public String getNearestCity(@RequestParam("input") String city, @RequestParam(value = "topRelevantMatchCount", defaultValue = "3") int topN) {
        log.info(cityAndAirportDataMap.toString());
        city = city.toLowerCase();
        return computeEngine.getTopNNearestStrings(city, cityAndAirportDataMap.keySet(), topN).toString();
    }
}