# Weather Now 🌤️

A responsive weather web application that lets users search for a city and view its current weather conditions. The project uses a vanilla HTML/CSS/JavaScript frontend and a Java Spring Boot backend that retrieves weather data from the OpenWeather API.

## Features

- Search for weather by city name.
- Explore popular cities from the home page.
- View current temperature and feels-like temperature.
- View weather conditions and an associated weather icon.
- View minimum and maximum temperature, humidity, pressure, wind speed and direction, wind gust, visibility, cloud cover, sunrise, and sunset when provided by the API.
- Animated sky, sun, and cloud visuals.
- Loading and error states for weather requests.
- Backend endpoint that returns weather data as JSON.
- Docker configuration for containerized deployment and a Render deployment manifest.

## Tech Stack

**Frontend**
- HTML5
- CSS3, including animations and responsive styling
- Vanilla JavaScript and Fetch API

**Backend**
- Java 21
- Spring Boot 3.5.7
- Spring Web / REST
- Spring Security and CORS configuration
- Gradle

**External service**
- [OpenWeather API](https://openweathermap.org/api)

## Project Structure

```text
weather-web-main/
├── Frontend/
│   ├── html/
│   │   ├── index.html          # City search page
│   │   └── results.html        # Weather results page
│   ├── css/
│   │   ├── styles.css          # Search page styling
│   │   └── results.css         # Results page styling
│   └── javascript/
│       ├── weather.js          # Search and city suggestions
│       └── results.js          # Calls backend and renders weather data
├── backend/
│   ├── src/main/java/com/weather/weather_web/
│   │   ├── config/             # Security and CORS configuration
│   │   ├── controller/         # REST endpoint
│   │   ├── dto/                # Weather response/data objects
│   │   ├── service/             # OpenWeather API integration
│   │   └── WeatherWebApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── build.gradle
│   └── Dockerfile
├── Dockerfile
└── render.yaml
```

## Prerequisites

- Java Development Kit (JDK) 21, if running the backend locally.
- An OpenWeather API key.
- A modern browser.
- Docker, if using the containerized setup.
- Gradle Wrapper is included in the backend folder.

## Configuration

### 1. OpenWeather API key

Create an API key in your [OpenWeather account](https://home.openweathermap.org/api_keys).

The backend reads `weather.api.key` from `backend/src/main/resources/application.properties`. For safer configuration, change the property to use an environment variable:

```properties
weather.api.key=${WEATHER_API_KEY}
weather.api.url=https://api.openweathermap.org/data/2.5/weather
```

Then set `WEATHER_API_KEY` in your environment before starting the backend. Do not commit real API keys to GitHub. If a key has already been committed, revoke or rotate it in the provider dashboard.

### 2. City autocomplete

The frontend autocomplete in `Frontend/javascript/weather.js` uses the OpenWeather Geocoding API. Replace its `API_KEY` placeholder with a valid key for local testing, or preferably move autocomplete requests behind the backend so the key is not exposed in browser code. The current geocoding URL uses HTTP; use HTTPS.

### 3. Backend URL

The results page currently calls:

```javascript
const API_BASE_URL = 'http://localhost:8080/api/weather';
```

This is suitable for local development only. For deployment, change it to the deployed backend URL or configure a shared environment-based API URL in the frontend.

## Run Locally

### 1. Start the backend

Open a terminal and run:

```bash
cd backend
```

Set the API key in the same terminal session:

```bash
export WEATHER_API_KEY="your_openweather_api_key"
```

After updating `application.properties` as shown above, start Spring Boot:

```bash
./gradlew bootRun
```

On Windows, use:

```bat
gradlew.bat bootRun
```

The backend should start at `http://localhost:8080` by default.

### 2. Start the frontend

Serve the `Frontend` directory using a local static web server. For example, if Python is installed:

```bash
cd Frontend
python3 -m http.server 5500
```

Open:

```text
http://localhost:5500/html/index.html
```

You can also use a VS Code live server extension. Avoid opening the HTML file directly from the filesystem if browser restrictions interfere with requests.

### 3. Test the backend endpoint

With the backend running and a valid API key configured:

```bash
curl "http://localhost:8080/api/weather?city=London"
```

A successful response is JSON containing fields such as `cityName`, `country`, `temperature`, `feelsLike`, `description`, `humidity`, `windSpeed`, `pressure`, `tempMin`, `tempMax`, `visibility`, `cloudiness`, `sunrise`, and `sunset`.

An invalid or unknown city should return an error response.

## API Reference

### Get current weather for a city

```http
GET /api/weather?city={cityName}
```

Example:

```http
GET http://localhost:8080/api/weather?city=New%20Delhi
```

**Success response:** `200 OK` with a `WeatherDTO` JSON object.

**Possible errors:**
- `400 Bad Request` — the city parameter is empty or invalid.
- `404 Not Found` — the city could not be found.
- `500 Internal Server Error` — the weather service could not retrieve data.

The backend requests metric units from OpenWeather, so temperature is returned in Celsius and wind speed follows the provider's metric-unit convention.

## Docker

The repository includes a root-level Dockerfile and a backend Dockerfile. From the repository root, build the image:

```bash
docker build -t weather-now .
```

Run it with the API key configured. This assumes the backend property has been changed to read `WEATHER_API_KEY` as described above:

```bash
docker run --rm -p 8080:8080 \
  -e WEATHER_API_KEY="your_openweather_api_key" \
  weather-now
```

The container uses Java 21. The application supports the `PORT` environment variable for deployment platforms that assign a port dynamically.

## Deployment Notes

A `render.yaml` file is included for Render deployment. Before deploying:

1. Configure `WEATHER_API_KEY` as a secret environment variable in the hosting dashboard.
2. Update `application.properties` to read the key from that variable.
3. Update the frontend API URL from `localhost` to the deployed backend URL.
4. Configure city autocomplete securely; browser-side API keys are visible to users.
5. Restrict CORS to the actual frontend domain for production. The current backend configuration allows all origins.
6. Ensure the frontend is served over HTTPS and uses HTTPS API endpoints.

## Troubleshooting

- **Weather request fails:** Check that the backend is running, the API key is valid, and the provider account has API access.
- **City not found:** Check the spelling and try a city name with its country if needed.
- **Frontend shows a network error:** Verify `API_BASE_URL` and confirm the browser can reach the backend.
- **Autocomplete does not work:** Check the geocoding API key and use an HTTPS URL.
- **Works locally but not after deployment:** Replace `localhost` with the deployed backend URL and review CORS settings.

## Security Notes

- Never commit API keys, passwords, or other secrets.
- Keep provider keys on the backend wherever possible.
- Restrict CORS to trusted frontend origins in production.
- The current Spring Security configuration permits all requests; review access controls before adding sensitive endpoints.

## License

No license file is currently included. Add a license before distributing or reusing this project publicly.
