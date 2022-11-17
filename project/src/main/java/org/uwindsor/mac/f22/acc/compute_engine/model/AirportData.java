package org.uwindsor.mac.f22.acc.compute_engine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Vivek
 * @since 17/11/22
 */
public class AirportData {

    private final String city;
    private final String country;
    private final String code;

    public AirportData(String city, String country, String code) {
        this.city = city;
        this.country = country;
        this.code = code;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("city", city)
                .append("country", country)
                .append("code", code)
                .toString();
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }
}