package org.uwindsor.mac.f22.acc.compute_engine.store;

import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;
import org.uwindsor.mac.f22.acc.compute_engine.model.TrieNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Vivek
 * @since 24/11/22
 */
public class DataStore {

    private final List<AirportData> sourceAirportDataList;
    private final Map<String, AirportData> codeAndAirportDataMap;
    private final Map<String, List<AirportData>> cityAndAirportDataMap;
    private final TrieNode trieOfCityCodes;
    private final TrieNode trieOfCityNames;

    public DataStore(List<AirportData> sourceAirportDataList) {
        this.sourceAirportDataList = sourceAirportDataList;

        this.cityAndAirportDataMap = generateCityAndAirportDataMap();
        this.codeAndAirportDataMap = generateCodeAndAirportDataMap();
        this.trieOfCityCodes = generateTrieFromSet(codeAndAirportDataMap.keySet());
        this.trieOfCityNames = generateTrieFromSet(cityAndAirportDataMap.keySet());
    }

    private TrieNode generateTrieFromSet(Set<String> data) {
        TrieNode root = new TrieNode();
        data.forEach(code -> {
            TrieNode current = root;
            for (int i = 0; i < code.length(); i++) {
                char ch = code.charAt(i);
                TrieNode value = current.getChildAt(ch);
                if (value == null) {
                    current.putChildAt(ch, new TrieNode());
                }
                current = current.getChildAt(ch);
            }
        });
        return root;
    }

    private Map<String, AirportData> generateCodeAndAirportDataMap() {
        Map<String, AirportData> map = new HashMap<>();
        sourceAirportDataList.forEach(airportData -> map.put(airportData.getCode(), airportData));
        return map;
    }

    private Map<String, List<AirportData>> generateCityAndAirportDataMap() {
        Map<String, List<AirportData>> map = new HashMap<>();
        sourceAirportDataList.forEach(airportData -> {
            List<AirportData> dataWithSameCityName = map.get(airportData.getCity());
            if (Objects.isNull(dataWithSameCityName)) dataWithSameCityName = new ArrayList<>();
            dataWithSameCityName.add(airportData);
            map.put(airportData.getCity(), dataWithSameCityName);
        });
        return map;
    }

    public List<AirportData> getSourceAirportDataList() {
        return sourceAirportDataList;
    }

    public Map<String, AirportData> getCodeAndAirportDataMap() {
        return codeAndAirportDataMap;
    }

    public Map<String, List<AirportData>> getCityAndAirportDataMap() {
        return cityAndAirportDataMap;
    }

    public TrieNode getTrieOfCityCodes() {
        return trieOfCityCodes;
    }

    public TrieNode getTrieOfCityNames() {
        return trieOfCityNames;
    }
}