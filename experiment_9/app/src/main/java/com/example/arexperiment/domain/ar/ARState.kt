package com.example.arexperiment.domain.ar

import com.google.ar.core.Plane
import com.google.ar.core.TrackingState

sealed class ARState {
    object NotReady : ARState()
    
    data class Ready(
        val isTracking: Boolean = false,
        val isPlaneDetected: Boolean = false,
        val detectedPlanes: List<Plane> = emptyList(),
        val lightEstimate: Float = 1.0f,
        val trackingState: TrackingState = TrackingState.STOPPED
    ) : ARState()
    
    data class Error(val message: String) : ARState()
}
