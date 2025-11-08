package com.example.experiment4.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val name: String,
    @SerializedName("weather") val weather: List<Weather> = emptyList(),
    @SerializedName("main") val main: Main
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Float,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)
