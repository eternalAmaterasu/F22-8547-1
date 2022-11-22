package org.uwindsor.mac.f22.acc.compute_engine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Vivek
 * @since 23/11/22
 */
@RequiredArgsConstructor
@Getter
public class SearchRequest {

    private final String from;
    private final String to;
    private final int numberOfPassengers;
    private final int travelDate;
    private final String flightClass;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("from", from)
                .append("to", to)
                .append("numberOfPassengers", numberOfPassengers)
                .append("travelDate", travelDate)
                .append("flightClass", flightClass)
                .toString();
    }
}