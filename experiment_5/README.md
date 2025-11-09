# Experiment 5: Interactive Map Application

### **Experiment No:** 
5

### **Aim:**
To create an interactive map application using OpenStreetMap with Jetpack Compose, demonstrating location services, permissions handling, and map interactions.

### **Code:**

#### `app/src/main/java/com/example/experiment5/MainActivity.kt`
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (!PermissionUtils.hasRequiredPermissions(this)) {
            PermissionUtils.requestPermissions(this)
        }
        
        setContent {
            MaterialTheme {
                Surface {
                    MapScreen()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.PERMISSION_REQUEST_CODE -> {
                if (!(grantResults.isNotEmpty() && 
                    grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                    Toast.makeText(
                        this,
                        "Location and storage permissions are required for the map to work properly",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
```

### **Output:**
The application displays an interactive OpenStreetMap interface where users can:
- View their current location on the map
- Pan and zoom the map
- Handle location permissions appropriately
- Display map tiles from OpenStreetMap

### **Result:**
Successfully implemented an interactive map application using OpenStreetMap with proper permission handling and location services integration. The app demonstrates effective use of:
- Jetpack Compose for UI
- Android permissions system
- OpenStreetMap integration
- Location services
- Material Design components