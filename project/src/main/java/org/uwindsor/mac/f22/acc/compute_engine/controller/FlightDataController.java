package org.uwindsor.mac.f22.acc.compute_engine.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uwindsor.mac.f22.acc.compute_engine.service.ComputeEngineService;

import java.util.Date;
import java.util.List;

/**
 * @author Vivek
 * @since 04/11/22
 */
@Slf4j
@RestController
@RequestMapping("/flight")
public class FlightDataController {

    @Autowired
    private ComputeEngineService computeEngineService;

    @GetMapping("/time/system")
    public String getSystemTime() {
        return (new Date()).toString();
    }

    @GetMapping("/data/nearest/code")
    public List<String> getNearestCode(@RequestParam("input") String code, @RequestParam(value = "topRelevantMatchCount", defaultValue = "10") int topN) {
        code = code.toLowerCase();
        return computeEngineService.getTopNNearestCityCodes(code, topN);
    }

    @GetMapping("/data/nearest/city")
    public List<String> getNearestCity(@RequestParam("input") String city, @RequestParam(value = "topRelevantMatchCount", defaultValue = "10") int topN) {
        city = city.toLowerCase();
        return computeEngineService.getTopNNearestCityCodesBasedOnNames(city, topN);
    }
}