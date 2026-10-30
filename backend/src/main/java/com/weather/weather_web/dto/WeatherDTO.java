package com.weather.weather_web.dto;

public class WeatherDTO {
    
    private String cityName;
    private String country;
    private double temperature;
    private double feelsLike;
    private String description;
    private String weatherMain;
    private int humidity;
    private double windSpeed;
    private int pressure;
    private String icon;
    
    // New fields
    private double tempMin;
    private double tempMax;
    private int visibility;
    private int cloudiness;
    private Integer windDeg;
    private Double windGust;
    private long dt;
    private long sunrise;
    private long sunset;
    private int timezoneOffset;
    
    public WeatherDTO() {
    }
    
    public WeatherDTO(String cityName, String country, double temperature, 
                     double feelsLike, String description, String weatherMain,
                     int humidity, double windSpeed, int pressure, String icon) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.description = description;
        this.weatherMain = weatherMain;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.icon = icon;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getFeelsLike() {
        return feelsLike;
    }
    
    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getWeatherMain() {
        return weatherMain;
    }
    
    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }
    
    public int getHumidity() {
        return humidity;
    }
    
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public int getPressure() {
        return pressure;
    }
    
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public double getTempMin() {
        return tempMin;
    }
    
    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }
    
    public double getTempMax() {
        return tempMax;
    }
    
    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }
    
    public int getVisibility() {
        return visibility;
    }
    
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    
    public int getCloudiness() {
        return cloudiness;
    }
    
    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }
    
    public Integer getWindDeg() {
        return windDeg;
    }
    
    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }
    
    public Double getWindGust() {
        return windGust;
    }
    
    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }
    
    public long getDt() {
        return dt;
    }
    
    public void setDt(long dt) {
        this.dt = dt;
    }
    
    public long getSunrise() {
        return sunrise;
    }
    
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    
    public long getSunset() {
        return sunset;
    }
    
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
