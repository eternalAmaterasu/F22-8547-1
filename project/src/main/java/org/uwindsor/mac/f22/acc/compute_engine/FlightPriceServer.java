package org.uwindsor.mac.f22.acc.compute_engine;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author Vivek
 * @since 04/11/22
 */
@SpringBootApplication
public class FlightPriceServer {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("EST", ZoneId.SHORT_IDS))); //force setting, because for some reason my jvm runs in IST time
        //System.setProperty("webdriver.gecko.driver", "/etc/WEBDRIVER/FIREFOX/geckodriver"); //setting path of the firefox driver for the server
        System.setProperty("webdriver.gecko.driver", "D://geckodriver.exe"); //setting path of the firefox driver for the server
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true"); //for suppressing selenium logs
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null"); //for suppressing selenium logs

        SpringApplication.run(FlightPriceServer.class, args);
    }
}