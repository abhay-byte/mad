package com.example.arexperiment.domain.ar

import android.content.Context
import com.example.arexperiment.domain.models.ARObject
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObjectManager @Inject constructor(private val context: Context) {
    private val objects = mutableMapOf<String, Pair<ARObject, ArModelNode>>()

    suspend fun loadAndPlaceObject(
            modelName: String,
            modelPath: String,
            arSceneView: ArSceneView,
            position: Position? = null
    ): String? {
        try {
            // Create AR object
            val objectId = UUID.randomUUID().toString()
            val arObject = ARObject(id = objectId, modelName = modelName, modelPath = modelPath)

            // Create and configure model node
            val modelNode =
                    ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                        loadModelGlbAsync(context = context, glbFileLocation = modelPath)
                        position = position ?: Position(0f, 0f, 0f)
                    }

            // Add node to scene
            arSceneView.addChild(modelNode)

            // Store object and node
            objects[objectId] = Pair(arObject, modelNode)

            return objectId
        } catch (e: Exception) {
            return null
        }
    }

    fun getObject(id: String): ARObject? = objects[id]?.first

    fun updateObjectTransform(
            id: String,
            position: Position? = null,
            rotation: io.github.sceneview.math.Rotation? = null,
            scale: io.github.sceneview.math.Scale? = null
    ) {
        objects[id]?.let { (arObject, modelNode) ->
            position?.let {
                arObject.position = it
                modelNode.position = it
            }
            rotation?.let {
                arObject.rotation = it
                modelNode.rotation = it
            }
            scale?.let {
                arObject.scale = it
                modelNode.scale = it
            }
        }
    }

    fun removeObject(id: String) {
        objects[id]?.let { (_, modelNode) ->
            modelNode.parent = null
            objects.remove(id)
        }
    }

    fun clearAllObjects() {
        objects.values.forEach { (_, modelNode) -> modelNode.parent = null }
        objects.clear()
    }

    fun getAllObjects(): List<ARObject> = objects.values.map { it.first }
}
