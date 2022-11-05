package org.uwindsor.mac.f22.acc.compute_engine.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Vivek
 * @since 04/11/22
 */
@Slf4j
@RestController
@RequestMapping("/flight/price")
public class FlightPriceController {

    @GetMapping("/time/system")
    public String getSystemTime() {
        return (new Date()).toString();
    }
}