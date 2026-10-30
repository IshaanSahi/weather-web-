// API Configuration
const GEOCODING_API_URL = 'https://geocoding-api.open-meteo.com/v1/search';

// DOM Elements
const cityInput = document.getElementById('cityInput');
const searchBtn = document.getElementById('searchBtn');
const errorMessage = document.getElementById('errorMessage');
const suggestionsContainer = document.getElementById('suggestions');

// Update error message class for new design
if (errorMessage) {
    errorMessage.className = 'modern-error-message';
}
if (suggestionsContainer) {
    suggestionsContainer.className = 'modern-suggestions';
}

// Event Listeners
searchBtn.addEventListener('click', handleSearch);
cityInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        handleSearch();
    }
});

async function handleSearch() {
    const city = cityInput.value.trim();
    
    if (!city) {
        showError('Please enter a city name');
        return;
    }
    
    hideError();
    hideSuggestions();
    
    // Redirect to results page with city parameter
    window.location.href = `results.html?city=${encodeURIComponent(city)}`;
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
    setTimeout(() => {
        errorMessage.classList.add('show');
    }, 10);
}

function hideError() {
    errorMessage.classList.remove('show');
    setTimeout(() => {
        errorMessage.style.display = 'none';
        errorMessage.textContent = '';
    }, 300);
}

// Autocomplete functionality
let debounceTimer;
let isSelectingSuggestion = false;

cityInput.addEventListener('input', async (e) => {
    // Don't show suggestions if user just selected one
    if (isSelectingSuggestion) {
        isSelectingSuggestion = false;
        return;
    }
    
    const value = e.target.value.trim();
    
    // Clear previous timer
    clearTimeout(debounceTimer);
    
    if (value.length < 2) {
        hideSuggestions();
        return;
    }
    
    // Debounce API calls (wait 300ms after user stops typing)
    debounceTimer = setTimeout(async () => {
        await fetchCitySuggestions(value);
    }, 300);
});

async function fetchCitySuggestions(query) {
    try {
        const url = `${GEOCODING_API_URL}?name=${encodeURIComponent(query)}&count=5&language=en&format=json`;
        const response = await fetch(url);
        
        if (!response.ok) {
            hideSuggestions();
            return;
        }
        
        const data = await response.json();
        displaySuggestions(data.results || []);
    } catch (error) {
        console.error('Error fetching city suggestions:', error);
        hideSuggestions();
    }
}

function displaySuggestions(cities) {
    if (!cities || cities.length === 0) {
        hideSuggestions();
        return;
    }
    
    suggestionsContainer.innerHTML = '';
    
    cities.forEach(city => {
        const suggestionItem = document.createElement('div');
        suggestionItem.className = 'suggestion-item';
        
        const cityName = city.name;
        const state = city.state ? `, ${city.state}` : '';
        const country = city.country;
        
        const name = document.createElement('strong');
        name.textContent = `${cityName}${state}`;
        const countryLabel = document.createElement('span');
        countryLabel.textContent = country;
        suggestionItem.append(name, countryLabel);
        
        suggestionItem.addEventListener('click', () => {
            isSelectingSuggestion = true;
            cityInput.value = cityName;
            hideSuggestions();
            handleSearch();
        });
        
        suggestionsContainer.appendChild(suggestionItem);
    });
    
    suggestionsContainer.style.display = 'block';
}

function hideSuggestions() {
    if (suggestionsContainer) {
        suggestionsContainer.style.display = 'none';
        suggestionsContainer.innerHTML = '';
    }
}
 //hello 

// Hide suggestions when clicking outside
document.addEventListener('click', (e) => {
    if (!cityInput.contains(e.target) && !suggestionsContainer.contains(e.target)) {
        hideSuggestions();    
    }
});
   
// Hide suggestions when pressing Escape
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        hideSuggestions();
    }
});
