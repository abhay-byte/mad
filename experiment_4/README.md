# Experiment 4: Weather App using OpenWeatherMap

## Aim
Build a simple weather app that fetches current weather for a city using the OpenWeatherMap API and displays it with Jetpack Compose.

## What I created
- `MainActivity.kt` — creates Retrofit, the repository, and starts the Compose app.
- `WeatherService.kt` — Retrofit interface for OpenWeatherMap current weather endpoint.
- `WeatherResponse.kt` — minimal data models for parsing the API response.
- `WeatherRepository.kt` — repository that wraps the network call.
- `WeatherViewModel.kt` — ViewModel exposing UI state flows and fetch logic.
- `WeatherApp.kt` — Compose UI: text field for city, search button, loading and result states.
- Gradle files and a placeholder `strings.xml` with `openweather_api_key`.

## How to set your API key
1. Get an API key from https://openweathermap.org/ (free tier available).
2. Replace the placeholder in `app/src/main/res/values/strings.xml`:

```xml
<string name="openweather_api_key">YOUR_API_KEY</string>
```

Replace `YOUR_API_KEY` with the key you obtained.

IMPORTANT - keep the API key out of version control

Preferred (safe) method — local.properties (recommended):

1. At the root of the repository (or the root of `experiment_4`) create or edit `local.properties` (this file is normally ignored by Git):

```
OPENWEATHER_API_KEY=your_real_api_key_here
```

2. The project is configured to read `OPENWEATHER_API_KEY` from `local.properties` and inject it into `BuildConfig.OPENWEATHER_API_KEY` at build time. The app reads the key from `BuildConfig` so no API key needs to be committed.

Alternative (not recommended):
- You can keep the key in `gradle.properties` on your local machine (again, do NOT commit it), or use CI secret management for build servers.

Make sure `local.properties` is in your `.gitignore`. A `.gitignore` has been added to `experiment_4/` that ignores `local.properties` and common sensitive files.

## How to build & run (Windows PowerShell)
From the repo root:

```powershell
cd C:\Users\abhay\temp\MAD\experiment_4
./gradlew assembleDebug
./gradlew installDebug
```

After install, launch the app on a connected device or emulator. The app provides a text field where you can enter a city (e.g. "London") and press the search icon to fetch current weather.

## Notes and next steps
- This is a minimal, educational implementation. For production:
  - Move the API key to a secure source (NDK, server or build-time injection not committed to VCS).
  - Add error handling and retry logic.
  - Add caching (Room or local storage) and offline support.
  - Add unit tests for the repository and ViewModel.

