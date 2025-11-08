package com.example.arexperiment

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.arexperiment.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ARIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testARScreenInteraction() {
        // Wait for AR to initialize
        composeTestRule.waitForIdle()

        // Verify initial UI elements
        composeTestRule
            .onNodeWithTag("model_selection_menu")
            .assertIsDisplayed()

        // Test model selection
        composeTestRule
            .onNodeWithText("Cube")
            .performClick()

        // Verify manipulation controls appear after model placement
        composeTestRule
            .onNodeWithTag("object_manipulation_controls")
            .assertIsDisplayed()

        // Test object manipulation
        composeTestRule
            .onNodeWithTag("scale_slider")
            .performTouchInput {
                swipeRight()
            }

        composeTestRule
            .onNodeWithTag("rotation_slider")
            .performTouchInput {
                swipeLeft()
            }
    }

    @Test
    fun testARStateTransitions() {
        // Wait for AR initialization
        composeTestRule.waitForIdle()

        // Verify initial loading state
        composeTestRule
            .onNodeWithTag("ar_loading_indicator")
            .assertIsDisplayed()

        // Wait for AR to become ready
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule
                    .onNodeWithTag("ar_ready_indicator")
                    .isDisplayed()
            } catch (e: Exception) {
                false
            }
        }

        // Verify AR ready state
        composeTestRule
            .onNodeWithTag("ar_ready_indicator")
            .assertIsDisplayed()
    }
}