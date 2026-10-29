package com.weather.weather_web.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private Clouds clouds;
    private String name;
    private Sys sys;
    private Integer visibility;
    private Long dt;
    
    // Getters and Setters
    public Main getMain() {
        return main;
    }
    
    public void setMain(Main main) {
        this.main = main;
    }
    
    public List<Weather> getWeather() {
        return weather;
    }
    
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
    
    public Wind getWind() {
        return wind;
    }
    
    public void setWind(Wind wind) {
        this.wind = wind;
    }
    
    public Clouds getClouds() {
        return clouds;
    }
    
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Sys getSys() {
        return sys;
    }
    
    public void setSys(Sys sys) {
        this.sys = sys;
    }
    
    public Integer getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
    
    public Long getDt() {
        return dt;
    }
    
    public void setDt(Long dt) {
        this.dt = dt;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
        
        public double getTemp() {
            return temp;
        }
        
        public void setTemp(double temp) {
            this.temp = temp;
        }
        
        public double getFeels_like() {
            return feels_like;
        }
        
        public void setFeels_like(double feels_like) {
            this.feels_like = feels_like;
        }
        
        public double getTemp_min() {
            return temp_min;
        }
        
        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }
        
        public double getTemp_max() {
            return temp_max;
        }
        
        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }
        
        public int getPressure() {
            return pressure;
        }
        
        public void setPressure(int pressure) {
            this.pressure = pressure;
        }
        
        public int getHumidity() {
            return humidity;
        }
        
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String main;
        private String description;
        private String icon;
        
        public String getMain() {
            return main;
        }
        
        public void setMain(String main) {
            this.main = main;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getIcon() {
            return icon;
        }
        
        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        private double speed;
        private Integer deg;
        private Double gust;
        
        public double getSpeed() {
            return speed;
        }
        
        public void setSpeed(double speed) {
            this.speed = speed;
        }
        
        public Integer getDeg() {
            return deg;
        }
        
        public void setDeg(Integer deg) {
            this.deg = deg;
        }
        
        public Double getGust() {
            return gust;
        }
        
        public void setGust(Double gust) {
            this.gust = gust;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        private int all;
        
        public int getAll() {
            return all;
        }
        
        public void setAll(int all) {
            this.all = all;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        private String country;
        private Long sunrise;
        private Long sunset;
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public Long getSunrise() {
            return sunrise;
        }
        
        public void setSunrise(Long sunrise) {
            this.sunrise = sunrise;
        }
        
        public Long getSunset() {
            return sunset;
        }
        
        public void setSunset(Long sunset) {
            this.sunset = sunset;
        }
    }
}
