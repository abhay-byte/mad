package com.example.arexperiment.domain.ar.environment

import android.content.Context
import com.google.android.filament.Engine
import io.github.sceneview.environment.Environment
import io.github.sceneview.environment.HDREnvironment
import io.github.sceneview.loaders.EnvironmentLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnvironmentManager @Inject constructor(
    private val context: Context,
    private val engine: Engine
) {
    private var currentEnvironment: Environment? = null
    private val environmentLoader = EnvironmentLoader(engine)

    suspend fun loadEnvironment(hdrAssetPath: String): HDREnvironment? {
        return try {
            environmentLoader.createHDREnvironment(context.assets, hdrAssetPath)?.also { 
                currentEnvironment?.destroy()
                currentEnvironment = it
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentEnvironment(): Environment? = currentEnvironment

    fun cleanup() {
        currentEnvironment?.destroy()
        currentEnvironment = null
    }

    companion object {
        const val DEFAULT_HDR_ASSET = "environments/sky_2k.hdr"
    }
}