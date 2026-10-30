package com.weather.weather_web.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.weather.weather_web.dto.OpenMeteoLocation;
import com.weather.weather_web.dto.OpenMeteoResponse;
import com.weather.weather_web.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    
    @Value("${weather.geocoding.url}")
    private String geocodingUrl;
    
    @Value("${weather.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public WeatherDTO getWeatherByCity(String city) {
        OpenMeteoLocation location = findLocation(city);
        OpenMeteoResponse response = fetchWeather(location);
        return convertToDTO(location, response);
    }

    private OpenMeteoLocation findLocation(String city) {
        URI uri = UriComponentsBuilder.fromUriString(geocodingUrl)
            .queryParam("name", city)
            .queryParam("count", 1)
            .queryParam("language", "en")
            .queryParam("format", "json")
            .build()
            .encode()
            .toUri();

        try {
            OpenMeteoLocation[] locations = restTemplate.getForObject(uri, OpenMeteoLocation[].class);
            if (locations == null || locations.length == 0) {
                throw new RuntimeException("City not found: " + city);
            }
            return locations[0];
        } catch (RestClientException e) {
            throw new RuntimeException("Unable to find the city using Open-Meteo", e);
        }
    }

    private OpenMeteoResponse fetchWeather(OpenMeteoLocation location) {
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
            .queryParam("latitude", location.latitude())
            .queryParam("longitude", location.longitude())
            .queryParam("current", String.join(",",
                "temperature_2m",
                "relative_humidity_2m",
                "apparent_temperature",
                "weather_code",
                "wind_speed_10m",
                "wind_direction_10m",
                "wind_gusts_10m",
                "pressure_msl",
                "visibility",
                "cloud_cover"))
            .queryParam("daily", "temperature_2m_min,temperature_2m_max,sunrise,sunset")
            .queryParam("temperature_unit", "celsius")
            .queryParam("wind_speed_unit", "ms")
            .queryParam("timezone", "auto")
            .build()
            .encode()
            .toUri();

        try {
            OpenMeteoResponse response = restTemplate.getForObject(uri, OpenMeteoResponse.class);
            if (response == null || response.current() == null) {
                throw new RuntimeException("No current weather data returned by Open-Meteo");
            }
            return response;
        } catch (RestClientException e) {
            throw new RuntimeException("Unable to fetch weather data from Open-Meteo", e);
        }
    }

    private WeatherDTO convertToDTO(OpenMeteoLocation location, OpenMeteoResponse response) {
        OpenMeteoResponse.Current current = response.current();
        int timezoneOffset = response.utcOffsetSeconds() == null ? 0 : response.utcOffsetSeconds();
        WeatherDTO dto = new WeatherDTO();
        dto.setCityName(location.name());
        dto.setCountry(location.country());
        dto.setTemperature(valueOrZero(current.temperature()));
        dto.setFeelsLike(valueOrZero(current.feelsLike()));
        dto.setHumidity(current.humidity() == null ? 0 : current.humidity());
        dto.setPressure((int) Math.round(valueOrZero(current.pressure())));
        dto.setWindSpeed(valueOrZero(current.windSpeed()));
        dto.setWindDeg(current.windDirection());
        dto.setWindGust(current.windGust());
        dto.setVisibility(current.visibility() == null ? 0 : (int) Math.round(current.visibility()));
        dto.setCloudiness(current.cloudCover() == null ? 0 : current.cloudCover());
        dto.setTimezoneOffset(timezoneOffset);
        dto.setDt(toEpochSeconds(current.time(), timezoneOffset));

        if (current.weatherCode() != null) {
            dto.setWeatherMain(conditionCategory(current.weatherCode()));
            dto.setDescription(conditionDescription(current.weatherCode()));
        }

        OpenMeteoResponse.Daily daily = response.daily();
        if (daily != null) {
            dto.setTempMin(firstOrZero(daily.temperatureMin()));
            dto.setTempMax(firstOrZero(daily.temperatureMax()));
            dto.setSunrise(toEpochSeconds(firstOrNull(daily.sunrise()), timezoneOffset));
            dto.setSunset(toEpochSeconds(firstOrNull(daily.sunset()), timezoneOffset));
        }
        return dto;
    }

    private static double valueOrZero(Number value) {
        return value == null ? 0 : value.doubleValue();
    }

    private static double firstOrZero(List<Double> values) {
        return values == null || values.isEmpty() ? 0 : valueOrZero(values.get(0));
    }

    private static String firstOrNull(List<String> values) {
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private static long toEpochSeconds(String localDateTime, int timezoneOffset) {
        if (localDateTime == null || localDateTime.isBlank()) {
            return 0;
        }
        return LocalDateTime.parse(localDateTime)
            .toEpochSecond(ZoneOffset.ofTotalSeconds(timezoneOffset));
    }

    private static String conditionCategory(int code) {
        if (code == 0 || code == 1) return "Clear";
        if (code == 2 || code == 3) return "Clouds";
        if (code == 45 || code == 48) return "Fog";
        if (code >= 51 && code <= 67 || code >= 80 && code <= 82) return "Rain";
        if (code >= 71 && code <= 77 || code == 85 || code == 86) return "Snow";
        if (code >= 95) return "Thunderstorm";
        return "Clouds";
    }

    private static String conditionDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51 -> "Light drizzle";
            case 53 -> "Moderate drizzle";
            case 55, 56, 57 -> "Dense or freezing drizzle";
            case 61 -> "Slight rain";
            case 63 -> "Moderate rain";
            case 65, 66, 67 -> "Heavy or freezing rain";
            case 71 -> "Slight snow";
            case 73 -> "Moderate snow";
            case 75, 77 -> "Heavy snow";
            case 80 -> "Slight rain showers";
            case 81 -> "Moderate rain showers";
            case 82 -> "Violent rain showers";
            case 85, 86 -> "Snow showers";
            case 95 -> "Thunderstorm";
            case 96, 99 -> "Thunderstorm with hail";
            default -> "Variable conditions";
        };
    }

}
