package org.uwindsor.mac.f22.acc.compute_engine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.uwindsor.mac.f22.acc.compute_engine.engine.ComputeEngine;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;
import org.uwindsor.mac.f22.acc.compute_engine.model.TrieNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 21/11/22
 */
@Slf4j
@Component
public class ComputeEngineService {

    @Autowired
    @Qualifier("codeXAirportMap")
    private Map<String, AirportData> codeAndAirportDataMap;

    @Autowired
    @Qualifier("cityXAirportMap")
    private Map<String, List<AirportData>> cityAndAirportDataMap;

    @Autowired
    @Qualifier("trieOfCityCodes")
    public TrieNode trieCodeRoot;

    @Autowired
    @Qualifier("trieOfCityNames")
    public TrieNode trieNameRoot;

    @Autowired
    public ComputeEngine computeEngine;

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

    public List<String> getAutoCompleteForPrefixOnCodes(String prefix) {
        List<String> autoCompletedCodes = computeEngine.getAutoCompleteForPrefix(prefix, trieCodeRoot);
        log.info("Autocompleted values for prefix: {} => {}", prefix, autoCompletedCodes.toString());
        return autoCompletedCodes;
    }

    public List<String> getAutoCompleteForPrefixOnNames(String prefix) {
        List<String> autoCompletedCodes = computeEngine.getAutoCompleteForPrefix(prefix, trieNameRoot);
        log.info("Autocompleted values for prefix: {} => {}", prefix, autoCompletedCodes.toString());
        return autoCompletedCodes;
    }

    public boolean isCodePresent(String code) {
        return codeAndAirportDataMap.containsKey(code);
    }

    public List<AirportData> getAirportDataList(String key) {
        return cityAndAirportDataMap.getOrDefault(key, new ArrayList<>());
    }
}