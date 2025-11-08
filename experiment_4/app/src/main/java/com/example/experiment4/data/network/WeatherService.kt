package com.example.experiment4.data.network

import com.example.experiment4.data.network.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Return a Retrofit Response so callers can inspect HTTP status and error body.
 */
interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}
