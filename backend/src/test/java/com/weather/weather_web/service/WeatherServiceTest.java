package com.weather.weather_web.service;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.weather.weather_web.dto.WeatherDTO;

class WeatherServiceTest {

    @Test
    void geocodesCityAndMapsOpenMeteoCurrentAndDailyData() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        WeatherService service = new WeatherService(restTemplate);
        ReflectionTestUtils.setField(service, "geocodingUrl", "https://geocoding-api.open-meteo.com/v1/search");
        ReflectionTestUtils.setField(service, "apiUrl", "https://api.open-meteo.com/v1/forecast");

        server.expect(requestTo(containsString("name=New%20Delhi")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("""
                [{"name":"New Delhi","country":"India","latitude":28.6139,"longitude":77.2090}]
                """, MediaType.APPLICATION_JSON));
        server.expect(requestTo(containsString("latitude=28.6139")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("""
                {
                  "utc_offset_seconds":19800,
                  "current":{
                    "time":"2026-10-31T01:00",
                    "temperature_2m":20.5,
                    "relative_humidity_2m":88,
                    "apparent_temperature":21.0,
                    "weather_code":0,
                    "wind_speed_10m":1.7,
                    "wind_direction_10m":180,
                    "wind_gusts_10m":2.2,
                    "pressure_msl":1018.0,
                    "visibility":16000.0,
                    "cloud_cover":0
                  },
                  "daily":{
                    "time":["2026-10-31"],
                    "temperature_2m_min":[19.0],
                    "temperature_2m_max":[27.0],
                    "sunrise":["2026-10-31T06:31"],
                    "sunset":["2026-10-31T17:37"]
                  }
                }
                """, MediaType.APPLICATION_JSON));

        WeatherDTO weather = service.getWeatherByCity("New Delhi");

        assertEquals("New Delhi", weather.getCityName());
        assertEquals("India", weather.getCountry());
        assertEquals(20.5, weather.getTemperature(), 0.001);
        assertEquals(21.0, weather.getFeelsLike(), 0.001);
        assertEquals(88, weather.getHumidity());
        assertEquals(1018, weather.getPressure());
        assertEquals(1.7, weather.getWindSpeed(), 0.001);
        assertEquals(180, weather.getWindDeg());
        assertEquals(16000, weather.getVisibility());
        assertEquals(0, weather.getCloudiness());
        assertEquals("Clear sky", weather.getDescription());
        assertEquals("Clear", weather.getWeatherMain());
        assertEquals(19.0, weather.getTempMin(), 0.001);
        assertEquals(27.0, weather.getTempMax(), 0.001);
        assertEquals(1793388600L, weather.getDt());
        assertEquals(1793408460L, weather.getSunrise());
        assertEquals(1793448420L, weather.getSunset());
        assertEquals(19800, weather.getTimezoneOffset());
        server.verify();
    }
}
