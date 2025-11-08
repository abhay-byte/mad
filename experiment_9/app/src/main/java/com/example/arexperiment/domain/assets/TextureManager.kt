package com.example.arexperiment.domain.assets

import android.content.Context
import android.graphics.BitmapFactory
import com.google.ar.sceneform.rendering.Texture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextureManager @Inject constructor(
    private val context: Context
) {
    private val textureCache = mutableMapOf<String, Texture>()

    suspend fun loadTexture(assetPath: String): Texture? = withContext(Dispatchers.IO) {
        try {
            textureCache.getOrPut(assetPath) {
                context.assets.open(assetPath).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    Texture.builder()
                        .setSource(bitmap)
                        .build()
                        .get()
                }
            }
        } catch (e: IOException) {
            null
        }
    }

    fun clearCache() {
        textureCache.clear()
    }

    fun releaseTexture(assetPath: String) {
        textureCache.remove(assetPath)?.apply {
            // Ensure proper cleanup
            release()
        }
    }
}