package org.uwindsor.mac.f22.acc.compute_engine.model;

/**
 * @author Vivek
 * @since 22/11/22
 */
public class SearchRow {

    private String airline;
    private int launchTime; //hhMM 24 hours
    private int landTime; //hhMM 24 hours
    private int travelTime; //travel time in minutes
    private int stops;
    private String sourceAirCode;
    private String destinationAirCode;
    private double bestDealPrice;
}