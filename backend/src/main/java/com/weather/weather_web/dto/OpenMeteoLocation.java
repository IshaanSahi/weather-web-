package com.weather.weather_web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoLocation(
    String name,
    String country,
    double latitude,
    double longitude
) {
}
