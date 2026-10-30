package com.weather.weather_web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.weather_web.dto.WeatherDTO;
import com.weather.weather_web.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    
    private final WeatherService weatherService;
    
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("City name cannot be empty"));
            }
            
            WeatherDTO weather = weatherService.getWeatherByCity(city.trim());
            return ResponseEntity.ok(weather);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("City not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("City not found. Please check the spelling and try again."));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Unable to fetch weather data. Please try again later."));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
