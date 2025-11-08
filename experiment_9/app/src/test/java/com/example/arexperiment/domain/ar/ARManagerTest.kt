package com.example.arexperiment.domain.ar

import com.google.ar.core.Config
import com.google.ar.core.Session
import io.github.sceneview.ar.ARScene
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ARManagerTest {
    private lateinit var arManager: ARManager
    private lateinit var mockARScene: ARScene
    private lateinit var mockSession: Session

    @Before
    fun setup() {
        mockARScene = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)
        arManager = ARManager()
    }

    @Test
    fun `test AR session configuration`() {
        val config = mockk<Config>(relaxed = true)

        every { mockSession.isDepthModeSupported(any()) } returns true
        
        arManager.configureSession(mockSession, config)

        verify {
            config.depthMode = Config.DepthMode.AUTOMATIC
            config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
            config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
        }
    }

    @Test
    fun `test state management`() {
        // Initial state should be NotReady
        assertEquals(ARState.NotReady, arManager.state.value)

        // Test state transitions
        arManager.updateState(ARState.Ready)
        assertEquals(ARState.Ready, arManager.state.value)

        arManager.updateState(ARState.Error)
        assertEquals(ARState.Error, arManager.state.value)
    }

    @Test
    fun `test object placement`() {
        val objects = arManager.getPlacedObjects()
        assertTrue(objects.isEmpty())

        // Test object placement
        val testObject = ARObject(
            id = "test1",
            modelPath = "models/test.glb",
            position = Vector3(0f, 0f, 0f),
            rotation = Quaternion(0f, 0f, 0f, 1f),
            scale = Vector3(1f, 1f, 1f)
        )

        arManager.placeObject(testObject)
        assertEquals(1, arManager.getPlacedObjects().size)
        assertEquals(testObject, arManager.getPlacedObjects().first())
    }
}