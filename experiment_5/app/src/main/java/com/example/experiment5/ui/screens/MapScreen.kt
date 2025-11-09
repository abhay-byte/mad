package com.example.experiment5.ui.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.example.experiment5.util.PermissionUtils

@Composable
fun MapScreen() {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var myLocationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    // Initialize OSMDroid configuration
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        )
    }

    // Map view
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                
                // Add location overlay if we have permissions
                if (PermissionUtils.hasRequiredPermissions(context as Activity)) {
                    myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this).apply {
                        enableMyLocation()
                        enableFollowLocation()
                        isDrawAccuracyEnabled = true
                    }
                    overlays.add(myLocationOverlay)

                    // Add compass
                    overlays.add(CompassOverlay(context, this).apply {
                        enableCompass()
                    })
                }

                mapView = this
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { map ->
            // Update map center to current location if available
            myLocationOverlay?.myLocation?.let { location ->
                map.controller.setCenter(location)
            }
        }
    )

    // Clean up
    DisposableEffect(Unit) {
        onDispose {
            myLocationOverlay?.disableMyLocation()
            mapView?.onPause()
        }
    }
}