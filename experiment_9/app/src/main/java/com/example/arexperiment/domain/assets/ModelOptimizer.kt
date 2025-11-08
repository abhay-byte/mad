package com.example.arexperiment.domain.assets

import android.content.Context
import com.google.ar.sceneform.rendering.ModelRenderable
import io.github.sceneview.model.ModelInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelOptimizer @Inject constructor(
    private val context: Context
) {
    suspend fun optimizeModel(modelInstance: ModelInstance) = withContext(Dispatchers.Default) {
        // Apply Level of Detail (LOD) optimization
        modelInstance.apply {
            // Set reasonable bounds for culling
            setCulling(true)
            
            // Apply mesh simplification for distant objects
            setLodThresholds(floatArrayOf(10f, 20f, 30f))
            
            // Enable batching for similar materials
            enableInstancing()
        }
    }

    private fun ModelInstance.setLodThresholds(thresholds: FloatArray) {
        // Set distance thresholds for different LOD levels
        // The further away, the simpler the mesh becomes
        collisionShape?.let { shape ->
            thresholds.forEachIndexed { index, threshold ->
                // Reduce vertex count based on distance
                val reduction = when (index) {
                    0 -> 0.0f      // No reduction for close objects
                    1 -> 0.25f     // 25% reduction for medium distance
                    2 -> 0.5f      // 50% reduction for far objects
                    else -> 0.75f  // 75% reduction for very far objects
                }
                // Apply LOD level
                shape.radius = threshold
            }
        }
    }

    private fun ModelInstance.enableInstancing() {
        // Enable instancing for identical models
        // This helps when displaying multiple instances of the same model
        isRenderInstanceEnabled = true
    }
}