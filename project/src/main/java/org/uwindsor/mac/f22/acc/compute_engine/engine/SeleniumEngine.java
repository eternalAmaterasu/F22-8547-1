package org.uwindsor.mac.f22.acc.compute_engine.engine;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

/**
 * @author Vivek
 * @since 20/11/22
 */
public class SeleniumEngine {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/etc/WEBDRIVER/FIREFOX/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        //options.setHeadless(true);
        WebDriver driver = new FirefoxDriver(options);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://www.skyscanner.ca/transport/flights/cdg/yul/221123/?adults=1&adultsv2=1&cabinclass=economy&children=0&childrenv2=&destinationentityid=27536613&inboundaltsenabled=false&infants=0&originentityid=27537339&outboundaltsenabled=false&preferdirects=false&ref=home&rtn=0");

        Thread.sleep(20 * 1000);
        System.out.println(driver.getPageSource());

        driver.close();
        driver.quit();
    }
}
