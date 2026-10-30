# Weather Now 🌤️

A responsive weather web application that lets users search for a city and view its current weather conditions. The project uses a vanilla HTML/CSS/JavaScript frontend and a Java Spring Boot backend that retrieves weather data from the OpenWeather API. The root Dockerfile packages both the frontend and backend into one service, so the app can be deployed without a separate frontend host or database.

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

The backend reads `WEATHER_API_KEY` from its environment:

```properties
weather.api.key=${WEATHER_API_KEY}
weather.api.url=https://api.openweathermap.org/data/2.5/weather
```

Set `WEATHER_API_KEY` in your environment before starting the backend. Never commit API keys. If a key has ever been committed, revoke it with the provider and create a replacement; removing it from the latest source does not remove it from Git history.

### 2. City autocomplete

The frontend autocomplete is not configured for deployment because its current implementation would expose the OpenWeather key in browser requests. Do not put a real key in `Frontend/javascript/weather.js`; move geocoding requests behind the backend before enabling autocomplete in production. City searches can still be submitted directly.

### 3. Backend URL

During local development, the results page calls:

```javascript
const API_BASE_URL = 'http://localhost:8080/api/weather';
```

In deployment, the frontend calls `/api/weather` on the same origin, so no frontend API URL or cross-origin setup is needed.

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

Start Spring Boot:

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

## Free deployment on Render

The Render Blueprint in `render.yaml` deploys the root Dockerfile as one web service. The Docker image includes the frontend and Spring Boot API, and the app does not require a database.

1. Push this repository to GitHub and connect it to Render as a Blueprint.
2. When Render prompts for `WEATHER_API_KEY`, enter a newly created OpenWeather API key as a secret environment variable. Do not put the key in this repository.
3. Deploy the service. Render assigns its public URL; open that URL to use the app.

The free web service may spin down after inactivity, which can make its first request slow. Free-tier limits and availability can change; confirm current terms with Render. No paid domain or database is required.

To build locally with Docker:

```bash
docker build -t weather-now .
docker run --rm -p 8080:8080 -e WEATHER_API_KEY="your_openweather_api_key" weather-now
```

Open `http://localhost:8080` to use the app. The container uses Java 21 and honors the `PORT` environment variable set by Render.

## Deployment Notes

The production frontend and API are served from the same Render service over HTTPS. Local development uses a separate static server and permits only the documented localhost origins for API requests.

## Troubleshooting

- **Weather request fails:** Check that the backend is running, the API key is valid, and the provider account has API access.
- **City not found:** Check the spelling and try a city name with its country if needed.
- **Frontend shows a network error:** Verify `API_BASE_URL` and confirm the browser can reach the backend.
- **Autocomplete does not work:** Check the geocoding API key and use an HTTPS URL.
- **Works locally but not after deployment:** Check that Render has a valid `WEATHER_API_KEY` secret and that the deployment completed successfully.

## Security Notes

- Never commit API keys, passwords, or other secrets.
- Keep provider keys on the backend wherever possible.
- The frontend and API are same-origin in production; local development CORS is restricted to localhost.
- The current Spring Security configuration permits all requests; review access controls before adding sensitive endpoints.

## License

No license file is currently included. Add a license before distributing or reusing this project publicly.
