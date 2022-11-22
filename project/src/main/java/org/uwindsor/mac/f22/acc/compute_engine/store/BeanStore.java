package org.uwindsor.mac.f22.acc.compute_engine.store;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;
import org.uwindsor.mac.f22.acc.compute_engine.reader.ReadAirportCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vivek
 * @since 17/11/22
 */
@Slf4j
@Configuration
public class BeanStore {

    @Bean
    public StopWatch getStopWatch() {
        return new StopWatch();
    }

    @Bean(destroyMethod = "quit")
    public WebDriver getSeleniumWebDriver() {
        log.info("Starting firefox driver selenium creation");
        StopWatch stopWatch = getStopWatch();
        stopWatch.start();

        FirefoxOptions options = new FirefoxOptions();
        //options.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        stopWatch.stop();

        log.info("Firefox driver creation completed in {} s!", stopWatch.getTime(TimeUnit.SECONDS));
        return driver;
    }

    @Bean
    public List<AirportData> getAirportDataList() {
        return ReadAirportCodes.readAirportDataFile();
    }

    @Bean
    @Qualifier("cityXAirportMap")
    public Map<String, List<AirportData>> getCityAndAirportDataMap() {
        List<AirportData> data = getAirportDataList();
        Map<String, List<AirportData>> map = new HashMap<>();
        data.forEach(airportData -> {
            List<AirportData> dataWithSameCityName = map.get(airportData.getCity());
            if (Objects.isNull(dataWithSameCityName)) dataWithSameCityName = new ArrayList<>();
            dataWithSameCityName.add(airportData);
            map.put(airportData.getCity(), dataWithSameCityName);
        });
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