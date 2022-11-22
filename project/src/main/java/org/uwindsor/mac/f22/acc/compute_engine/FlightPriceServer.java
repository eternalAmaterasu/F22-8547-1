package org.uwindsor.mac.f22.acc.compute_engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author Vivek
 * @since 04/11/22
 */
@Slf4j
@SpringBootApplication
public class FlightPriceServer {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("EST", ZoneId.SHORT_IDS))); //force setting, because for some reason my jvm runs in IST time
        System.setProperty("webdriver.gecko.driver", "/etc/WEBDRIVER/FIREFOX/geckodriver-v0.32.0-linux64/geckodriver");
        SpringApplication.run(FlightPriceServer.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.uwindsor.mac.f22.acc.compute_engine"))
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        log.info("Startup complete! Swagger should be accessible on http://localhost:3131/swagger-ui/index.html");
    }
}