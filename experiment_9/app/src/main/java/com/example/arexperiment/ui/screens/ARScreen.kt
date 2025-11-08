package com.example.arexperiment.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene

@Composable
fun ARScreen(viewModel: ARViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val arSceneView = remember { ARScene(context) }

    // Setup AR Scene
    LaunchedEffect(Unit) {
        arSceneView.configureSession { session ->
            session.configure(
                    session.config.apply {
                        planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                        lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
                        updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                    }
            )
        }
    }

    // Main AR View
    AndroidView(factory = { arSceneView }, modifier = androidx.compose.ui.Modifier.fillMaxSize())
}
