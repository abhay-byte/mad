package com.example.experiment4.data.network

import com.example.experiment4.data.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
