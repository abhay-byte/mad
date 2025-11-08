package com.example.arexperiment.domain.ar.image

import android.content.Context
import android.graphics.BitmapFactory
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.Session
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class AugmentedImageManager @Inject constructor(private val context: Context) {
    private val _trackedImages = MutableStateFlow<List<TrackedImage>>(emptyList())
    val trackedImages: StateFlow<List<TrackedImage>> = _trackedImages.asStateFlow()

    private val imageNodes = mutableMapOf<String, AugmentedImageNode>()

    fun configureSession(session: Session, config: Config) {
        // Add images to track from assets
        context.assets.list("augmented_images")?.forEach { filename ->
            config.addAugmentedImage(
                    session,
                    filename.substringBeforeLast("."),
                    context.assets.open("augmented_images/$filename").use {
                        BitmapFactory.decodeStream(it)
                    }
            )
        }
    }

    fun update(session: Session, frame: com.google.ar.core.Frame) {
        frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
            when (augmentedImage.trackingState) {
                AugmentedImage.TrackingState.TRACKING -> {
                    handleTrackedImage(session, augmentedImage)
                }
                AugmentedImage.TrackingState.STOPPED -> {
                    removeImage(augmentedImage)
                }
                else -> {
                    /* Do nothing */
                }
            }
        }
    }

    private fun handleTrackedImage(session: Session, augmentedImage: AugmentedImage) {
        val imageName = augmentedImage.name ?: return

        if (!imageNodes.containsKey(imageName)) {
            // Create new node for this image
            val node = AugmentedImageNode(session.engine, augmentedImage)
            imageNodes[imageName] = node

            _trackedImages.value =
                    _trackedImages.value +
                            TrackedImage(
                                    name = imageName,
                                    node = node,
                                    centerPose = augmentedImage.centerPose,
                                    extentX = augmentedImage.extentX,
                                    extentZ = augmentedImage.extentZ
                            )
        } else {
            // Update existing node
            imageNodes[imageName]?.updateTrackedPose(augmentedImage)

            _trackedImages.value =
                    _trackedImages.value.map {
                        if (it.name == imageName) {
                            it.copy(
                                    centerPose = augmentedImage.centerPose,
                                    extentX = augmentedImage.extentX,
                                    extentZ = augmentedImage.extentZ
                            )
                        } else it
                    }
        }
    }

    private fun removeImage(augmentedImage: AugmentedImage) {
        val imageName = augmentedImage.name ?: return
        imageNodes.remove(imageName)?.destroy()
        _trackedImages.value = _trackedImages.value.filter { it.name != imageName }
    }

    fun clear() {
        imageNodes.values.forEach { it.destroy() }
        imageNodes.clear()
        _trackedImages.value = emptyList()
    }
}

data class TrackedImage(
        val name: String,
        val node: AugmentedImageNode,
        val centerPose: com.google.ar.core.Pose,
        val extentX: Float,
        val extentZ: Float
)
