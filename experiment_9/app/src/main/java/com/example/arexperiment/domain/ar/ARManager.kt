package com.example.arexperiment.domain.ar

import android.content.Context
import com.google.ar.core.*
import com.google.ar.core.exceptions.CameraNotAvailableException
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sceneview.ar.ARScene
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class ARManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val _arState = MutableStateFlow&lt;ARState&gt;(ARState.NotReady)
    val arState: StateFlow&lt;ARState&gt; = _arState.asStateFlow()

    private var arScene: ARScene? = null
    private val anchors = mutableListOf&lt;Anchor&gt;()

    fun initializeARScene(scene: ARScene) {
        arScene = scene
        setupARScene()
    }

    private fun setupARScene() {
        arScene?.let { scene ->
            try {
                scene.configureSession { session ->
                    session.configure(
                        Config(session).apply {
                            planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                            lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                        }
                    )
                }
                _arState.value = ARState.Ready()
                
                // Set up frame update listener
                scene.onFrame = { frame -> updateARState(frame) }
            } catch (e: Exception) {
                _arState.value = ARState.Error(e.message ?: "Failed to set up AR session")
            }
        }
    }

    private fun updateARState(frame: Frame) {
        try {
            val camera = frame.camera
            val planes = frame.getUpdatedTrackables(Plane::class.java)
            val lightEstimate = frame.lightEstimate.pixelIntensity

            _arState.value =
                    ARState(
                            isTracking = camera.trackingState == TrackingState.TRACKING,
                            isPlaneDetected =
                                    planes.any { it.trackingState == TrackingState.TRACKING },
                            detectedPlanes =
                                    planes.filter { it.trackingState == TrackingState.TRACKING },
                            lightEstimate = lightEstimate,
                            trackingState = camera.trackingState
                    )
        } catch (e: CameraNotAvailableException) {
            _arState.value =
                    _arState.value.copy(errorMessage = "Camera not available: ${e.message}")
        } catch (e: Exception) {
            _arState.value = _arState.value.copy(errorMessage = "AR Error: ${e.message}")
        }
    }

    fun createAnchor(hitResult: HitResult): Anchor? {
        return try {
            val anchor = hitResult.createAnchor()
            anchors.add(anchor)
            anchor
        } catch (e: Exception) {
            _arState.value =
                    _arState.value.copy(errorMessage = "Failed to create anchor: ${e.message}")
            null
        }
    }

    fun removeAnchor(anchor: Anchor) {
        anchors.remove(anchor)
        anchor.detach()
    }

    fun clearAnchors() {
        anchors.forEach { it.detach() }
        anchors.clear()
    }

    fun getVisiblePlanes(): List<Plane> {
        return _arState.value.detectedPlanes
    }

    fun getLightEstimate(): Float {
        return _arState.value.lightEstimate
    }
}
