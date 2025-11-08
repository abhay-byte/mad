package com.example.arexperiment.domain.ar.camera

import io.github.sceneview.collision.CollisionSystem
import io.github.sceneview.gesture.CameraGestureDetector
import io.github.sceneview.math.Position
import io.github.sceneview.math.toVector3
import io.github.sceneview.node.CameraNode
import kotlin.math.sign

class AdvancedCameraManipulator(
        private val cameraNode: CameraNode,
        private val collisionSystem: CollisionSystem,
        orbitHomePosition: Position? = null,
        targetPosition: Position? = null
) :
        CameraGestureDetector.DefaultCameraManipulator(
                orbitHomePosition = orbitHomePosition,
                targetPosition = targetPosition
        ) {
    private var scrollBeginCameraPosition = Position()
    private var scrollBeginDistance: Float? = 0f
    private var scrollBeginSeparation = 0f
    private var isLocked = false

    override fun scrollBegin(x: Int, y: Int, separation: Float) {
        if (isLocked) return

        val hitResults = collisionSystem.hitTest(x.toFloat(), y.toFloat())
        scrollBeginDistance =
                hitResults.firstOrNull()?.node?.position?.let {
                    (cameraNode.position - it).toVector3().length()
                }
        scrollBeginCameraPosition = cameraNode.position
        scrollBeginSeparation = separation
    }

    override fun scrollUpdate(x: Int, y: Int, prevSeparation: Float, currSeparation: Float) {
        if (isLocked) return

        val beginDistance = scrollBeginDistance
        if (beginDistance == null) {
            super.scrollUpdate(x, y, prevSeparation, currSeparation)
            return
        }

        val movedVector = (cameraNode.position - scrollBeginCameraPosition).toVector3()
        val movedDirection =
                listOf(movedVector.x.sign, movedVector.y.sign, movedVector.z.sign)
                        .firstOrNull { it != 0f }
                        ?.sign
                        ?: 1f

        val ratio = scrollBeginSeparation / currSeparation
        val moved = movedVector.length() * movedDirection
        val target = (beginDistance * ratio)
        val adjust = target - (beginDistance - moved)

        manipulator.scroll(x, y, adjust)
    }

    fun setLocked(locked: Bool) {
        isLocked = locked
    }

    // Additional camera control methods
    fun zoomTo(target: Position, duration: Float = 1.0f) {
        // Implement smooth camera zoom animation
    }

    fun orbit(angleX: Float, angleY: Float, duration: Float = 1.0f) {
        // Implement smooth camera orbit animation
    }

    fun flyTo(destination: Position, duration: Float = 1.0f) {
        // Implement smooth camera movement animation
    }
}
