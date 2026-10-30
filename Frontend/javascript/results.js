// API Configuration
const API_BASE_URL = ['localhost', '127.0.0.1'].includes(window.location.hostname)
    ? 'http://localhost:8080/api/weather'
    : '/api/weather';

// DOM Elements
const loadingSpinner = document.getElementById('loadingSpinner');
const errorMessage = document.getElementById('errorMessage');
const errorText = document.getElementById('errorText');
const weatherContainer = document.getElementById('weatherContainer');

// Update class names for new design
if (loadingSpinner) {
    loadingSpinner.className = 'modern-loading-container';
}
if (errorMessage) {
    errorMessage.className = 'modern-error-container';
}
if (weatherContainer) {
    weatherContainer.className = 'modern-weather-container';
}

// Weather data elements
const cityNameEl = document.getElementById('cityName');
const countryEl = document.getElementById('country');
const lastUpdatedEl = document.getElementById('lastUpdated');
const weatherIconEl = document.getElementById('weatherIcon');
const temperatureEl = document.getElementById('temperature');
const weatherDescEl = document.getElementById('weatherDescription');
const tempMinEl = document.getElementById('tempMin');
const tempMaxEl = document.getElementById('tempMax');
const feelsLikeEl = document.getElementById('feelsLike');
const windSpeedEl = document.getElementById('windSpeed');
const windDirectionEl = document.getElementById('windDirection');
const windGustEl = document.getElementById('windGust');
const humidityEl = document.getElementById('humidity');
const pressureEl = document.getElementById('pressure');
const visibilityEl = document.getElementById('visibility');
const cloudCoverEl = document.getElementById('cloudCover');
const sunriseEl = document.getElementById('sunrise');
const sunsetEl = document.getElementById('sunset');

// Get city from URL parameters
function getCityFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('city');
}

// Initialize page
window.addEventListener('DOMContentLoaded', async () => {
    const city = getCityFromURL();
    
    if (!city) {
        showError('No city specified. Please go back and search for a city.');
        return;
    }
    
    await fetchAndDisplayWeather(city);
});

// Fetch and display weather data
async function fetchAndDisplayWeather(city) {
    showLoading();
    
    try {
        const weatherData = await fetchWeather(city);
        hideLoading();
        displayWeather(weatherData);
    } catch (error) {
        hideLoading();
        showError(error.message);
    }
}

// Fetch weather from API
async function fetchWeather(city) {
    try {
        const response = await fetch(`${API_BASE_URL}?city=${encodeURIComponent(city)}`);
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to fetch weather data');
        }
        
        const data = await response.json();
        return data;
    } catch (error) {
        if (error.message.includes('Failed to fetch')) {
            throw new Error('Unable to connect to the server. Please make sure the backend is running.');
        }
        throw error;
    }
}

// Display weather data
function displayWeather(data) {
    if (!data) {
        showError('No weather data received');
        return;
    }
    
    // Update city info
    cityNameEl.textContent = data.cityName || 'Unknown';
    countryEl.textContent = data.country || '';
    
    // Update last updated time
    if (data.dt) {
        const date = getCityLocalDate(data.dt, data.timezoneOffset);
        lastUpdatedEl.textContent = `Updated: ${formatTime(date)} on ${formatDate(date)}`;
    } else {
        lastUpdatedEl.textContent = 'Updated: Just now';
    }
    
    // Update weather icon
    const iconUrl = `https://openweathermap.org/img/wn/${data.icon}@4x.png`;
    weatherIconEl.src = iconUrl;
    weatherIconEl.alt = data.description || 'Weather icon';
    
    // Update temperature
    temperatureEl.textContent = `${formatTemperature(data.temperature)}°C`;
    weatherDescEl.textContent = data.description || 'No description';
    
    // Update min/max temperature
    if (data.tempMin !== undefined) {
        tempMinEl.textContent = `${formatTemperature(data.tempMin)}°C`;
    } else {
        tempMinEl.textContent = '--°C';
    }
    
    if (data.tempMax !== undefined) {
        tempMaxEl.textContent = `${formatTemperature(data.tempMax)}°C`;
    } else {
        tempMaxEl.textContent = '--°C';
    }
    
    // Update feels like
    feelsLikeEl.textContent = `${formatTemperature(data.feelsLike)}°C`;
    
    // Update wind data
    windSpeedEl.textContent = `${data.windSpeed} m/s`;
    
    if (data.windDeg !== undefined) {
        const direction = getWindDirection(data.windDeg);
        windDirectionEl.textContent = `${direction} (${data.windDeg}°)`;
    } else {
        windDirectionEl.textContent = 'N/A';
    }
    
    if (data.windGust !== undefined && data.windGust > 0) {
        windGustEl.textContent = `${data.windGust} m/s`;
    } else {
        windGustEl.textContent = 'N/A';
    }
    
    // Update humidity and pressure
    humidityEl.textContent = `${data.humidity}%`;
    pressureEl.textContent = `${data.pressure} hPa`;
    
    // Update visibility
    if (data.visibility !== undefined) {
        const visibilityKm = (data.visibility / 1000).toFixed(1);
        visibilityEl.textContent = `${visibilityKm} km`;
    } else {
        visibilityEl.textContent = 'N/A';
    }
    
    // Update cloud cover
    if (data.cloudiness !== undefined) {
        cloudCoverEl.textContent = `${data.cloudiness}%`;
    } else {
        cloudCoverEl.textContent = 'N/A';
    }
    
    // Update sunrise/sunset times
    if (data.sunrise) {
        const sunriseDate = getCityLocalDate(data.sunrise, data.timezoneOffset);
        sunriseEl.textContent = formatTime(sunriseDate);
    } else {
        sunriseEl.textContent = '--:--';
    }
    
    if (data.sunset) {
        const sunsetDate = getCityLocalDate(data.sunset, data.timezoneOffset);
        sunsetEl.textContent = formatTime(sunsetDate);
    } else {
        sunsetEl.textContent = '--:--';
    }
    
    // Show weather container with animation
    showWeatherContainer();
}

function getCityLocalDate(timestamp, timezoneOffset = 0) {
    return new Date((timestamp + timezoneOffset) * 1000);
}

function formatTemperature(value) {
    return Number(value).toFixed(1);
}

// Helper function to convert wind degrees to direction
function getWindDirection(degrees) {
    const directions = ['N', 'NNE', 'NE', 'ENE', 'E', 'ESE', 'SE', 'SSE', 'S', 'SSW', 'SW', 'WSW', 'W', 'WNW', 'NW', 'NNW'];
    const index = Math.round(((degrees %= 360) < 0 ? degrees + 360 : degrees) / 22.5) % 16;
    return directions[index];
}

// Helper function to format time
function formatTime(date) {
    const hours = date.getUTCHours().toString().padStart(2, '0');
    const minutes = date.getUTCMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
}

// Helper function to format date
function formatDate(date) {
    const options = { month: 'short', day: 'numeric', year: 'numeric', timeZone: 'UTC' };
    return date.toLocaleDateString('en-US', options);
}

// Show/Hide functions
function showLoading() {
    loadingSpinner.style.display = 'flex';
    errorMessage.classList.remove('show');
    weatherContainer.classList.remove('show');
}

function hideLoading() {
    loadingSpinner.style.display = 'none';
}

function showError(message) {
    errorText.textContent = message;
    errorMessage.classList.add('show');
    loadingSpinner.style.display = 'none';
    weatherContainer.classList.remove('show');
}

function showWeatherContainer() {
    weatherContainer.classList.add('show');
    errorMessage.classList.remove('show');
    loadingSpinner.style.display = 'none';
}
