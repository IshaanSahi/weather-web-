package com.weather.weather_web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoResponse(
    Current current,
    Daily daily,
    @JsonProperty("utc_offset_seconds") Integer utcOffsetSeconds
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
        String time,
        @JsonProperty("temperature_2m") Double temperature,
        @JsonProperty("relative_humidity_2m") Integer humidity,
        @JsonProperty("apparent_temperature") Double feelsLike,
        @JsonProperty("weather_code") Integer weatherCode,
        @JsonProperty("wind_speed_10m") Double windSpeed,
        @JsonProperty("wind_direction_10m") Integer windDirection,
        @JsonProperty("wind_gusts_10m") Double windGust,
        @JsonProperty("pressure_msl") Double pressure,
        Double visibility,
        @JsonProperty("cloud_cover") Integer cloudCover
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Daily(
        List<String> time,
        @JsonProperty("temperature_2m_min") List<Double> temperatureMin,
        @JsonProperty("temperature_2m_max") List<Double> temperatureMax,
        List<String> sunrise,
        List<String> sunset
    ) {
    }
}
