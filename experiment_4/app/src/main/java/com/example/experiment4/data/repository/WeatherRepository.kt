package com.example.experiment4.data.repository

import com.example.experiment4.data.network.WeatherService
import com.example.experiment4.data.network.model.WeatherResponse
import java.io.IOException
import java.net.UnknownHostException

/**
 * Repository inspects the HTTP response and throws a clear exception when the API returns an error
 * (for example: invalid API key or city not found). This prevents Gson from trying to parse an error
 * payload into the success model and causing a low-level parsing exception.
 */
class WeatherRepository(
    private val service: WeatherService,
    private val apiKey: String
) {
    suspend fun getWeatherForCity(city: String): WeatherResponse {
        try {
            val resp = service.getCurrentWeather(city = city, appid = apiKey)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) return body
                throw Exception("Empty response body")
            }

            // Try to include the server-provided message when possible
            val errMsg = try {
                resp.errorBody()?.string()
            } catch (e: Exception) {
                null
            }
            throw Exception(errMsg?.ifBlank { "HTTP ${resp.code()} ${resp.message()}" } ?: "HTTP ${resp.code()} ${resp.message()}")
        } catch (e: UnknownHostException) {
            // Hostname could not be resolved (DNS/network problem)
            throw Exception("Network error: Unable to resolve host. Check device network or DNS. (${e.message})")
        } catch (e: IOException) {
            // General I/O errors (connectivity, timeout, etc.)
            throw Exception("Network I/O error: ${e.message}")
        }
    }
}
