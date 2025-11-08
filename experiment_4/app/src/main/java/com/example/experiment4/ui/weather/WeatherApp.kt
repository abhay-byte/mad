package com.example.experiment4.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.experiment4.data.repository.WeatherRepository

@Composable
fun WeatherApp(repository: WeatherRepository) {
    val vm: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory(repository))
    val state by vm.uiState.collectAsState()

    WeatherScreen(
        state = state,
        onFetch = { city -> vm.fetchWeather(city) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(state: WeatherUiState, onFetch: (String) -> Unit) {
    var city by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City e.g. London") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { if (city.isNotBlank()) onFetch(city) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (state) {
                is WeatherUiState.Idle -> Text("Enter a city and press search")
                is WeatherUiState.Loading -> CircularProgressIndicator()
                is WeatherUiState.Success -> {
                    val data = (state as WeatherUiState.Success).data
                    Text("City: ${data.name}", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Temp: ${data.main.temp} °C", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${data.weather.firstOrNull()?.description ?: "-"}", style = MaterialTheme.typography.bodyLarge)
                }
                is WeatherUiState.Error -> Text("Error: ${(state as WeatherUiState.Error).message}")
            }
        }
    }
}
