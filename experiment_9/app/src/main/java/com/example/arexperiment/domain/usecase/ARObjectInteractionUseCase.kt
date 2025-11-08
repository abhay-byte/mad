package com.example.arexperiment.domain.usecase

import com.example.arexperiment.domain.models.ARObject
import com.example.arexperiment.domain.repository.ARObjectRepository
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import javax.inject.Inject

class ARObjectInteractionUseCase @Inject constructor(private val repository: ARObjectRepository) {
    suspend fun placeObject(
            modelName: String,
            modelPath: String,
            arSceneView: ArSceneView,
            position: Position? = null
    ): Result<String> =
            try {
                val objectId = repository.placeObject(modelName, modelPath, arSceneView, position)
                if (objectId != null) {
                    Result.success(objectId)
                } else {
                    Result.failure(Exception("Failed to place object"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

    fun getObject(id: String): Result<ARObject> =
            try {
                val arObject = repository.getObject(id)
                if (arObject != null) {
                    Result.success(arObject)
                } else {
                    Result.failure(Exception("Object not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

    fun getAllObjects(): Result<List<ARObject>> =
            try {
                Result.success(repository.getAllObjects())
            } catch (e: Exception) {
                Result.failure(e)
            }

    fun updateObjectTransform(
            id: String,
            position: Position? = null,
            rotation: Rotation? = null,
            scale: Scale? = null
    ): Result<Unit> =
            try {
                repository.updateObjectTransform(id, position, rotation, scale)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }

    fun removeObject(id: String): Result<Unit> =
            try {
                repository.removeObject(id)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }

    fun clearAllObjects(): Result<Unit> =
            try {
                repository.clearAllObjects()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
}
