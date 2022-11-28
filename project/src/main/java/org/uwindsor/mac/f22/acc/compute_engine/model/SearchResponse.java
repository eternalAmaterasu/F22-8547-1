package org.uwindsor.mac.f22.acc.compute_engine.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Vivek
 * @since 22/11/22
 */
@Getter
@Setter
public class SearchResponse {

    private String airline;
    private int launchTime; //hhMM 24 hours
    private int landTime; //hhMM 24 hours
    private int travelTime; //travel time in minutes
    private int stops;
    private String sourceAirCode;
    private String destinationAirCode;
    private double bestDealPrice;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bestDealPrice", bestDealPrice)
                .append("airline", airline)
                .append("launchTime", String.format("%04d", launchTime))
                .append("landTime", String.format("%04d", landTime))
                .append("travelTime", String.format("%02dh %02dm", travelTime / 60, travelTime - (travelTime / 60) * 60))
                .append("stops", stops)
                .append("sourceAirCode", sourceAirCode)
                .append("destinationAirCode", destinationAirCode)
                .toString();
    }
}