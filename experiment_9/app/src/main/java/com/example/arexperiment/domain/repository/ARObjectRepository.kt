package com.example.arexperiment.domain.repository

import com.example.arexperiment.domain.ar.ObjectManager
import com.example.arexperiment.domain.models.ARObject
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ARObjectRepository @Inject constructor(private val objectManager: ObjectManager) {
    suspend fun placeObject(
            modelName: String,
            modelPath: String,
            arSceneView: ArSceneView,
            position: Position? = null
    ): String? = objectManager.loadAndPlaceObject(modelName, modelPath, arSceneView, position)

    fun getObject(id: String): ARObject? = objectManager.getObject(id)

    fun getAllObjects(): List<ARObject> = objectManager.getAllObjects()

    fun updateObjectTransform(
            id: String,
            position: Position? = null,
            rotation: Rotation? = null,
            scale: Scale? = null
    ) = objectManager.updateObjectTransform(id, position, rotation, scale)

    fun removeObject(id: String) = objectManager.removeObject(id)

    fun clearAllObjects() = objectManager.clearAllObjects()
}
