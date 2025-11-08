package com.example.arexperiment.domain.ar.camera

import com.google.android.filament.Engine
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GltfCameraManager @Inject constructor(
    private val engine: Engine
) {
    private val _currentCamera = MutableStateFlow<CameraNode?>(null)
    val currentCamera: StateFlow<CameraNode?> = _currentCamera.asStateFlow()

    private val _availableCameras = MutableStateFlow<List<CameraNode>>(emptyList())
    val availableCameras: StateFlow<List<CameraNode>> = _availableCameras.asStateFlow()

    fun initializeFromModel(modelNode: ModelNode) {
        // Extract cameras from gltf model
        val cameras = modelNode.cameraNodes

        // Configure each camera
        cameras.forEach { camera ->
            camera.apply {
                setExposure(DEFAULT_APERTURE, DEFAULT_SHUTTER_SPEED, DEFAULT_SENSITIVITY)
                updateProjection(
                    aspect = DEFAULT_ASPECT_RATIO,
                    near = DEFAULT_NEAR,
                    far = DEFAULT_FAR
                )
            }
        }

        _availableCameras.value = cameras
        _currentCamera.value = cameras.firstOrNull()
    }

    fun switchToCamera(cameraNode: CameraNode?) {
        if (_availableCameras.value.contains(cameraNode)) {
            _currentCamera.value = cameraNode
        }
    }

    fun switchToCamera(index: Int) {
        _availableCameras.value.getOrNull(index)?.let { camera ->
            _currentCamera.value = camera
        }
    }

    fun updateCameraProjections(viewportWidth: Int, viewportHeight: Int) {
        val aspect = viewportWidth.toDouble() / viewportHeight.toDouble()
        _availableCameras.value.forEach { camera ->
            camera.updateProjection(
                aspect = aspect,
                near = DEFAULT_NEAR,
                far = DEFAULT_FAR
            )
        }
    }

    companion object {
        private const val DEFAULT_APERTURE = 16f
        private const val DEFAULT_SHUTTER_SPEED = 1f / 125f
        private const val DEFAULT_SENSITIVITY = 100f
        private const val DEFAULT_ASPECT_RATIO = 16.0 / 9.0
        private const val DEFAULT_NEAR = 0.05f
        private const val DEFAULT_FAR = 5000.0f
    }
}