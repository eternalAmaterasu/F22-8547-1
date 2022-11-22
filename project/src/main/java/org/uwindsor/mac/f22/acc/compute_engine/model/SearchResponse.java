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
                .append("airline", airline)
                .append("launchTime", launchTime)
                .append("landTime", landTime)
                .append("travelTime", travelTime)
                .append("stops", stops)
                .append("sourceAirCode", sourceAirCode)
                .append("destinationAirCode", destinationAirCode)
                .append("bestDealPrice", bestDealPrice)
                .toString();
    }
}