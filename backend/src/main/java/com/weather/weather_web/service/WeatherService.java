package com.weather.weather_web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.weather.weather_web.dto.WeatherDTO;
import com.weather.weather_web.dto.WeatherResponse;

@Service
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }
    
    public WeatherDTO getWeatherByCity(String city) {
        try {
            String url = String.format("%s?q=%s&appid=%s&units=metric", 
                                      apiUrl, city, apiKey);
            
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            
            if (response == null) {
                throw new RuntimeException("No response from weather API");
            }
            
            return convertToDTO(response);
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("City not found: " + city);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage());
        }
    }
    
    private WeatherDTO convertToDTO(WeatherResponse response) {
        WeatherDTO dto = new WeatherDTO();
        dto.setCityName(response.getName());
        dto.setCountry(response.getSys() != null ? response.getSys().getCountry() : "");
        
        if (response.getMain() != null) {
            dto.setTemperature(Math.round(response.getMain().getTemp() * 10.0) / 10.0);
            dto.setFeelsLike(Math.round(response.getMain().getFeels_like() * 10.0) / 10.0);
            dto.setHumidity(response.getMain().getHumidity());
            dto.setPressure(response.getMain().getPressure());
            dto.setTempMin(Math.round(response.getMain().getTemp_min() * 10.0) / 10.0);
            dto.setTempMax(Math.round(response.getMain().getTemp_max() * 10.0) / 10.0);
        }
        
        if (response.getWeather() != null && !response.getWeather().isEmpty()) {
            WeatherResponse.Weather weather = response.getWeather().get(0);
            dto.setWeatherMain(weather.getMain());
            dto.setDescription(capitalizeFirstLetter(weather.getDescription()));
            dto.setIcon(weather.getIcon());
        }
        
        if (response.getWind() != null) {
            dto.setWindSpeed(Math.round(response.getWind().getSpeed() * 10.0) / 10.0);
            dto.setWindDeg(response.getWind().getDeg());
            if (response.getWind().getGust() != null) {
                dto.setWindGust(Math.round(response.getWind().getGust() * 10.0) / 10.0);
            }
        }
        
        // Set visibility
        if (response.getVisibility() != null) {
            dto.setVisibility(response.getVisibility());
        }
        
        // Set cloudiness
        if (response.getClouds() != null) {
            dto.setCloudiness(response.getClouds().getAll());
        }
        
        // Set timestamp
        if (response.getDt() != null) {
            dto.setDt(response.getDt());
        }
        if (response.getTimezone() != null) {
            dto.setTimezoneOffset(response.getTimezone());
        }
        
        // Set sunrise and sunset
        if (response.getSys() != null) {
            if (response.getSys().getSunrise() != null) {
                dto.setSunrise(response.getSys().getSunrise());
            }
            if (response.getSys().getSunset() != null) {
                dto.setSunset(response.getSys().getSunset());
            }
        }
        
        return dto;
    }
    
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
