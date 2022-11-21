package org.uwindsor.mac.f22.acc.compute_engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.engine.ComputeEngine;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 21/11/22
 */

@Component
public class ComputeEngineService {

    @Autowired
    @Qualifier("codeXAirportMap")
    private Map<String, AirportData> codeAndAirportDataMap;

    @Autowired
    @Qualifier("cityXAirportMap")
    private Map<String, List<AirportData>> cityAndAirportDataMap;

    @Autowired
    private ComputeEngine computeEngine;

    public List<String> getTopNNearestCityCodes(String keyword, int topN) {
        return computeEngine.getTopNNearestStrings(keyword, codeAndAirportDataMap.keySet(), topN);
    }


    public List<String> getTopNNearestCityCodesBasedOnNames(String city, int topN) {
        List<String> nearestCities = computeEngine.getTopNNearestStrings(city, cityAndAirportDataMap.keySet(), topN);
        List<AirportData> cities = new ArrayList<>();
        nearestCities.forEach(nearestCity -> cities.addAll(cityAndAirportDataMap.get(nearestCity)));
        return cities.subList(0, Math.min(cities.size(), topN)).stream()
                .map(AirportData::getCode)
                .collect(Collectors.toList());
    }
}