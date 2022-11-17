package org.uwindsor.mac.f22.acc.compute_engine.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.uwindsor.mac.f22.acc.compute_engine.model.AirportData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Vivek
 * @since 17/11/22
 */
@Slf4j
public class ReadAirportCodes {

    private static final String AIRPORT_DATA_FILE = "project/src/main/resources/airport-data.txt";

    /**
     * Helper method to read the actual html file from the filesystem based on the path of the file to be read
     *
     * @return the text values of the html file in a single string
     */
    public static List<AirportData> readAirportDataFile() {
        List<AirportData> airportData = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        log.info("Initiating reading of airport data file now");
        stopWatch.start();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(AIRPORT_DATA_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] data = line.split("\\|");
                if (data[0].length() > 0 && data[1].length() > 0 && data[2].length() > 0) airportData.add(generateAirportData(data[0], data[1], data[2]));
            }
            log.info("Reading of the airport data file is completed!");
        } catch (IOException e) {
            log.error("Encountered error while reading the file at '{}': ", AIRPORT_DATA_FILE, e);
        } finally {
            stopWatch.stop();
            log.info("Time taken to attempt reading the file took {} ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
        return airportData;
    }

    private static AirportData generateAirportData(String city, String country, String code) {
        return new AirportData(city, country, code);
    }
}