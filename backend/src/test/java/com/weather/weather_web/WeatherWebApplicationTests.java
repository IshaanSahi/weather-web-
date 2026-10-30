package com.weather.weather_web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "weather.api.key=test-key")
class WeatherWebApplicationTests {

	@Test
	void contextLoads() {
	}

}
