package org.uwindsor.mac.f22.acc.compute_engine.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;
import org.uwindsor.mac.f22.acc.compute_engine.reader.ReadAirportCodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vivek
 * @since 17/11/22
 */
@Slf4j
@Configuration
public class BeanStore {

    @Bean
    public List<AirportData> getAirportDataList() {
        return ReadAirportCodes.readAirportDataFile();
    }

    @Bean
    @Qualifier("cityXAirportMap")
    public Map<String, AirportData> getCityAndAirportDataMap() {
        List<AirportData> data = getAirportDataList();
        Map<String, AirportData> map = new HashMap<>();
        data.forEach(airportData -> map.put(airportData.getCity(), airportData));
        return map;
    }

    @Bean
    @Qualifier("codeXAirportMap")
    public Map<String, AirportData> getCodeAndAirportDataMap() {
        List<AirportData> data = getAirportDataList();
        Map<String, AirportData> map = new HashMap<>();
        data.forEach(airportData -> map.put(airportData.getCode(), airportData));
        return map;
    }
}