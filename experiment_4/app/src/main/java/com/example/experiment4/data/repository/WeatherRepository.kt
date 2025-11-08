package com.example.experiment4.data.repository

import com.example.experiment4.data.network.WeatherService
import com.example.experiment4.data.network.model.WeatherResponse

class WeatherRepository(
    private val service: WeatherService,
    private val apiKey: String
) {
    suspend fun getWeatherForCity(city: String): WeatherResponse {
        return service.getCurrentWeather(city = city, appid = apiKey)
    }
}
