# Component Design Specifications

## 1. LocationTracker Component

```kotlin
class LocationTracker(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val scope: CoroutineScope
) {
    private val _locationFlow = MutableStateFlow<LocationState>(LocationState.Initial)
    val locationFlow: StateFlow<LocationState> = _locationFlow.asStateFlow()
    
    sealed class LocationState {
        object Initial : LocationState()
        object Loading : LocationState()
        data class Success(val location: Location) : LocationState()
        data class Error(val message: String) : LocationState()
    }

    data class LocationSettings(
        val interval: Long = 5000L,
        val fastestInterval: Long = 3000L,
        val priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    )
}

interface LocationRepository {
    fun startLocationUpdates(settings: LocationSettings)
    fun stopLocationUpdates()
    suspend fun getLastLocation(): Location?
    fun getLocationFlow(): Flow<LocationState>
}
```

## 2. MapViewModel Architecture

```kotlin
class MapViewModel(
    private val locationTracker: LocationTracker,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    data class MapUiState(
        val currentLocation: LatLng? = null,
        val searchQuery: String = "",
        val searchResults: List<Place> = emptyList(),
        val selectedPlace: Place? = null,
        val mapMarkers: List<MarkerData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    sealed class MapEvent {
        data class OnSearchQueryChanged(val query: String) : MapEvent()
        data class OnMarkerClicked(val markerId: String) : MapEvent()
        object OnCurrentLocationRequested : MapEvent()
        data class OnMapMoved(val newBounds: LatLngBounds) : MapEvent()
    }
}
```

## 3. Repository Interfaces

```kotlin
interface PlacesRepository {
    suspend fun searchNearbyPlaces(
        query: String,
        location: LatLng,
        radius: Int
    ): Result<List<Place>>

    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetails>
    
    suspend fun searchAutoComplete(
        query: String,
        bounds: LatLngBounds?
    ): Result<List<AutocompletePrediction>>
}

data class Place(
    val id: String,
    val name: String,
    val location: LatLng,
    val type: PlaceType,
    val rating: Float?,
    val address: String,
    val distance: Int? = null
)

data class PlaceDetails(
    val id: String,
    val name: String,
    val location: LatLng,
    val formattedAddress: String,
    val phoneNumber: String?,
    val rating: Float?,
    val website: String?,
    val openingHours: List<String>?,
    val photos: List<PhotoMetadata>,
    val priceLevel: Int?,
    val types: List<String>
)
```

## 4. Error Handling Strategy

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

sealed class LocationError : Exception() {
    object PermissionDenied : LocationError()
    object LocationDisabled : LocationError()
    data class UpdateError(override val message: String?) : LocationError()
}

sealed class PlacesError : Exception() {
    data class NetworkError(override val message: String?) : PlacesError()
    data class ApiError(override val message: String?) : PlacesError()
    object NoResults : PlacesError()
}
```

## 5. State Management Structure

```kotlin
// UI States
data class SearchBarState(
    val query: String = "",
    val suggestions: List<AutocompletePrediction> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null
)

data class MapState(
    val cameraPosition: CameraPosition? = null,
    val markers: List<MarkerData> = emptyList(),
    val selectedMarkerId: String? = null,
    val isLoading: Boolean = false
)

data class BottomSheetState(
    val placeDetails: PlaceDetails? = null,
    val isExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

// Events
sealed class UserEvent {
    data class SearchQueryChanged(val query: String) : UserEvent()
    data class MarkerClicked(val markerId: String) : UserEvent()
    data class MapMoved(val bounds: LatLngBounds) : UserEvent()
    object CurrentLocationClicked : UserEvent()
    object RetryClicked : UserEvent()
}
```

## 6. Permission Handling

```kotlin
object PermissionManager {
    sealed class PermissionState {
        object Granted : PermissionState()
        data class Denied(val shouldShowRationale: Boolean) : PermissionState()
        object NotRequested : PermissionState()
    }

    fun checkLocationPermission(context: Context): PermissionState
    suspend fun requestLocationPermission(activity: Activity): PermissionState
}
```

## 7. Data Models

```kotlin
// Location Models
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)

// Marker Models
data class MarkerData(
    val id: String,
    val position: LatLng,
    val title: String,
    val snippet: String?,
    val type: MarkerType,
    val zIndex: Float = 0f
)

enum class MarkerType {
    CURRENT_LOCATION,
    PLACE,
    SELECTED_PLACE
}

// Search Models
data class AutocompletePrediction(
    val placeId: String,
    val description: String,
    val primaryText: String,
    val secondaryText: String,
    val distanceMeters: Int?
)
```

## 8. Cache Strategy

```kotlin
interface PlacesCache {
    suspend fun getPlace(id: String): Place?
    suspend fun savePlace(place: Place)
    suspend fun getPlaceDetails(id: String): PlaceDetails?
    suspend fun savePlaceDetails(details: PlaceDetails)
    suspend fun clearOldEntries()
}

class PlacesCacheImpl(
    private val scope: CoroutineScope,
    private val database: PlacesDatabase
) : PlacesCache {
    private val cacheTimeout = 1.hours
    private val maxCacheSize = 100
}
```