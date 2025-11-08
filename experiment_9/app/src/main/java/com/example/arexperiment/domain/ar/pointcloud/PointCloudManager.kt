package com.example.arexperiment.domain.ar.pointcloud

import com.google.ar.core.Frame
import com.google.ar.core.PointCloud
import io.github.sceneview.math.Position
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class PointCloudManager @Inject constructor() {
    private val _pointCloudState = MutableStateFlow<PointCloudState>(PointCloudState.None)
    val pointCloudState: StateFlow<PointCloudState> = _pointCloudState.asStateFlow()

    private val pointNodes = mutableListOf<PointCloudNode>()
    private var lastPointCloudTimestamp: Long? = null
    private var minConfidence = 0.5f
    private var maxPoints = 1000

    fun updatePointCloud(frame: Frame) {
        frame.acquirePointCloud()?.use { pointCloud ->
            if (pointCloud.timestamp != lastPointCloudTimestamp) {
                processPointCloud(pointCloud)
                lastPointCloudTimestamp = pointCloud.timestamp
            }
        }
    }

    private fun processPointCloud(pointCloud: PointCloud) {
        val idsBuffer = pointCloud.ids ?: return
        val pointsSize = idsBuffer.limit()
        val pointsBuffer = pointCloud.points

        for (index in 0 until pointsSize) {
            val id = idsBuffer[index]
            val pointIndex = index * 4
            val position =
                    Position(
                            pointsBuffer[pointIndex],
                            pointsBuffer[pointIndex + 1],
                            pointsBuffer[pointIndex + 2]
                    )
            val confidence = pointsBuffer[pointIndex + 3]

            if (confidence > minConfidence) {
                addOrUpdatePoint(id, position, confidence)
            }
        }

        // Update state
        val averageConfidence =
                if (pointNodes.isNotEmpty()) {
                    pointNodes.map { it.confidence }.average().toFloat()
                } else 0f

        _pointCloudState.value =
                PointCloudState.Updated(
                        pointCount = pointNodes.size,
                        averageConfidence = averageConfidence
                )
    }

    private fun addOrUpdatePoint(id: Int, position: Position, confidence: Float) {
        val existingNode = pointNodes.firstOrNull { it.pointId == id }
        if (existingNode != null) {
            existingNode.worldPosition = position
            existingNode.confidence = confidence
        } else if (pointNodes.size < maxPoints) {
            // Create new point node
            createPointNode(id, position, confidence)?.let { pointNodes.add(it) }
        }
    }

    private fun createPointNode(id: Int, position: Position, confidence: Float): PointCloudNode? {
        // Create a small sphere or point representation
        // Note: Actual implementation would need proper model creation
        return null // TODO: Implement actual point visualization
    }

    fun setMinConfidence(confidence: Float) {
        minConfidence = confidence
        // Remove points below new confidence threshold
        pointNodes.removeAll { it.confidence < confidence }
    }

    fun setMaxPoints(max: Int) {
        maxPoints = max
        // Remove excess points if needed
        if (pointNodes.size > max) {
            pointNodes.subList(max, pointNodes.size).forEach { it.destroy() }
            pointNodes.subList(max, pointNodes.size).clear()
        }
    }

    fun clear() {
        pointNodes.forEach { it.destroy() }
        pointNodes.clear()
        _pointCloudState.value = PointCloudState.None
    }
}

class PointCloudNode(modelInstance: ModelInstance, val pointId: Int, var confidence: Float) :
        ModelNode(modelInstance)

sealed class PointCloudState {
    object None : PointCloudState()
    data class Updated(val pointCount: Int, val averageConfidence: Float) : PointCloudState()
}
