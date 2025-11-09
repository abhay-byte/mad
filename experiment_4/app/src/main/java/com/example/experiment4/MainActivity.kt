package com.example.experiment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.experiment4.data.network.WeatherService
import com.example.experiment4.data.repository.WeatherRepository
import com.example.experiment4.ui.theme.Experiment4Theme
import com.example.experiment4.ui.weather.WeatherApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create OkHttpClient with logging
        val loggingInterceptor =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        // Create Retrofit instance
        val retrofit =
                Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

        val service = retrofit.create(WeatherService::class.java)
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        val repository = WeatherRepository(service, apiKey)

        setContent {
            Experiment4Theme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    WeatherApp(repository)
                }
            }
        }
    }
}
