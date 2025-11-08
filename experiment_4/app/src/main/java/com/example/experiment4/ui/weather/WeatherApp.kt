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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.window.PopupProperties

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
    var expanded by remember { mutableStateOf(false) }

    // Small local suggestion list; replace with Places API or remote autocomplete as needed.
    val sampleCities = listOf(
        "London", "New York", "San Francisco", "Paris", "Berlin", "Tokyo", "Delhi", "Mumbai",
        "Sydney", "Moscow", "Beijing", "Cairo", "São Paulo"
    )
    val suggestions by remember(city) { mutableStateOf(sampleCities.filter { it.startsWith(city, ignoreCase = true) }.take(6)) }

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
                onValueChange = {
                    city = it
                    expanded = it.isNotBlank() && suggestions.isNotEmpty()
                },
                label = { Text("City e.g. London") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { if (city.isNotBlank()) { expanded = false; onFetch(city) } }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            // Simple Dropdown suggestions under the text field
            // Prevent the dropdown from taking focus so the soft keyboard stays visible while typing.
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false)
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(text = { Text(suggestion) }, onClick = {
                        city = suggestion
                        expanded = false
                        onFetch(suggestion)
                    })
                }
            }

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
                is WeatherUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${(state as WeatherUiState.Error).message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { if (city.isNotBlank()) onFetch(city) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
