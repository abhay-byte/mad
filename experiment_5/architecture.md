# Architecture Design Document

## 1. System Overview

### 1.1 Core Features Definition

#### Location Services
- **Current Location Tracking**
  - Continuous location updates using FusedLocationProvider
  - Configurable update intervals (default: 5 seconds)
  - Location accuracy modes (high accuracy/balanced/low power)
  - Background location updates support

#### Map Functionality
- **Google Maps Integration**
  - Interactive map view with standard controls
  - Custom map styling
  - Marker clustering for multiple locations
  - Custom info windows
  - Camera position management

#### Search & Places
- **Place Search**
  - Text-based location search
  - Nearby places search
  - Place autocomplete
  - Place details retrieval
  - Custom place markers

#### Permissions & Settings
- **Runtime Permissions**
  - Fine location permission
  - Background location permission
  - Graceful permission denial handling
  - Settings redirect for denied permissions

## 2. Technical Architecture

### 2.1 Layer Description

```
┌─────────────────────────────────────┐
│              UI Layer              │
│  ┌─────────────┐    ┌────────────┐  │
│  │   MapScreen │    │ SearchBar  │  │
│  └─────────────┘    └────────────┘  │
│  ┌─────────────┐    ┌────────────┐  │
│  │ BottomSheet │    │Permissions │  │
│  └─────────────┘    └────────────┘  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         ViewModel Layer              │
│  ┌─────────────┐    ┌────────────┐  │
│  │MapViewModel │    │SearchState │  │
│  └─────────────┘    └────────────┘  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Domain Layer                │
│  ┌─────────────┐    ┌────────────┐  │
│  │LocationRepo │    │PlacesRepo  │  │
│  └─────────────┘    └────────────┘  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Data Layer                  │
│  ┌─────────────┐    ┌────────────┐  │
│  │ Google Maps │    │  Places    │  │
│  │    SDK      │    │    API     │  │
│  └─────────────┘    └────────────┘  │
└─────────────────────────────────────┘
```

### 2.2 Components

#### UI Layer (Jetpack Compose)
- **MapScreen**: Main container composable
- **MapView**: Google Maps composable integration
- **SearchBar**: Search interface with suggestions
- **PermissionRequest**: Permission handling UI
- **BottomSheet**: Place details display
- **LoadingStates**: Progress indicators

#### ViewModel Layer
- **MapViewModel**
  - Manages map state and user interactions
  - Handles location updates
  - Controls map camera position
  - Manages markers and clustering

- **SearchViewModel**
  - Manages search queries and results
  - Handles place suggestions
  - Controls search filters

#### Repository Layer
- **LocationRepository**
  - Manages FusedLocationProvider
  - Handles location permissions
  - Provides location updates

- **PlacesRepository**
  - Manages Google Places API calls
  - Handles place search and details
  - Caches recent searches

#### Data Models
```kotlin
data class LocationState(
    val currentLocation: LatLng?,
    val isTracking: Boolean,
    val error: String?
)

data class MapState(
    val cameraPosition: CameraPosition,
    val markers: List<MarkerData>,
    val selectedMarker: MarkerData?,
    val isLoading: Boolean
)

data class SearchState(
    val query: String,
    val suggestions: List<PlaceSuggestion>,
    val results: List<Place>,
    val isSearching: Boolean,
    val error: String?
)
```

## 3. Data Flow

### 3.1 Location Permission Flow
```
User Launch
     │
     ▼
Check Permission ─────┐
     │               │
     ▼               ▼
Not Granted      Granted
     │               │
     ▼               ▼
Show Request    Start Location
Dialog          Updates
     │               │
     ▼               ▼
User Action     Update UI
     │
     └─────────────►◄─┘
```

### 3.2 Place Search Flow
```
User Input
     │
     ▼
Update Search Query
     │
     ▼
Debounce (300ms)
     │
     ▼
Fetch Suggestions ◄─── Cache Check
     │                    ▲
     ▼                    │
Update UI ──────────────►┘
```

## 4. Key Technical Decisions

### 4.1 State Management
- Using Kotlin StateFlow for reactive state updates
- Unidirectional data flow pattern
- State hoisting for composables

### 4.2 Location Updates
- Default update interval: 5 seconds
- High accuracy mode for initial fix
- Balanced mode for continuous tracking
- Background updates only when needed

### 4.3 Maps Configuration
- Default zoom level: 15f
- Marker clustering threshold: 10 markers
- Custom marker assets for different states
- Map style: Standard with custom colors

### 4.4 Caching Strategy
- In-memory cache for recent searches
- Disk cache for map tiles
- Place details cache duration: 1 hour
- Location history: Last 100 points

### 4.5 Error Handling
- Graceful degradation for missing permissions
- Retry mechanism for network failures
- User-friendly error messages
- Offline mode support where possible
