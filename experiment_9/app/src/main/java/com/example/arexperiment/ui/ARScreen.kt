package com.example.arexperiment.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.arexperiment.domain.ar.ARState
import com.example.arexperiment.domain.ar.ARManager
import com.example.arexperiment.ui.components.ModelItem
import com.example.arexperiment.ui.components.ModelSelectionMenu
import com.example.arexperiment.ui.components.ObjectManipulationControls
import com.example.arexperiment.ui.theme.spacing
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import kotlinx.coroutines.launch

@Composable
fun ARScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val arState by viewModel.arState
    val selectedObjectId by viewModel.selectedObjectId.collectAsState()
    val objects by viewModel.objects.collectAsState()

    var arScene by remember { mutableStateOf<ARScene?>(null) }

    // Sample 3D models - in a real app, this would come from a repository
    val availableModels = remember {
        listOf(
                ModelItem("Cube", "models/cube.glb"),
                ModelItem("Sphere", "models/sphere.glb"),
                ModelItem("Chair", "models/chair.glb")
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, 
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // AR Scene View
            AndroidView(
                    factory = { context ->
                        ARScene(context)
                                .apply {
                                    lifecycle.addObserver(this)
                                    configureSession { session, config ->
                                        config.depthMode =
                                                when {
                                                    session.isDepthModeSupported(
                                                            Config.DepthMode.AUTOMATIC
                                                    ) -> Config.DepthMode.AUTOMATIC
                                                    else -> Config.DepthMode.DISABLED
                                                }
                                        config.instantPlacementMode =
                                                Config.InstantPlacementMode.DISABLED
                                        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                                    }
                                    onSessionCreate = { session ->
                                        viewModel.updateARState(ARState.Ready)
                                    }
                                    onSessionResume = { viewModel.updateARState(ARState.Ready) }
                                    onSessionPause = { viewModel.updateARState(ARState.NotReady) }
                                    onTapPlane = { hitResult ->
                                        selectedObjectId?.let { id ->
                                            viewModel.updateObjectTransform(
                                                    id,
                                                    position = hitResult.hitPose.position
                                            )
                                        }
                                    }
                                }
                                .also { arScene = it }
                    },
                    modifier = Modifier.fillMaxSize()
            )

            // Loading indicator
            if (arState == ARState.NotReady) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Model selection menu
            ModelSelectionMenu(
                    models = availableModels,
                    onModelSelected = { model ->
                        arScene?.let { scene ->
                            viewModel.placeObject(
                                    modelName = model.name,
                                    modelPath = model.path,
                                    arSceneView = scene
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = MaterialTheme.spacing.large)
            )

            // Object manipulation controls
            if (selectedObjectId != null) {
                ObjectManipulationControls(
                        selectedObject = objects.find { it.id == selectedObjectId },
                        onUpdateTransform = { position, rotation, scale ->
                            selectedObjectId?.let { id ->
                                viewModel.updateObjectTransform(id, position, rotation, scale)
                            }
                        },
                        onRemoveObject = {
                            selectedObjectId?.let { id -> viewModel.removeObject(id) }
                        },
                        onResetTransform = {
                            selectedObjectId?.let { id ->
                                viewModel.updateObjectTransform(
                                        id,
                                        rotation = io.github.sceneview.math.Rotation(0f, 0f, 0f),
                                        scale = io.github.sceneview.math.Scale(1f)
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = MaterialTheme.spacing.large)
                )
            }
        }
    }

    LaunchedEffect(arState) {
        when (arState) {
            ARState.NotReady -> {
                scope.launch { snackbarHostState.showSnackbar("Initializing AR... Please wait") }
            }
            ARState.Ready -> {
                scope.launch { snackbarHostState.showSnackbar("AR Ready! Tap to place objects") }
            }
            ARState.Error -> {
                scope.launch { snackbarHostState.showSnackbar("Error initializing AR") }
                (context as? Activity)?.finish()
            }
        }
    }
}
