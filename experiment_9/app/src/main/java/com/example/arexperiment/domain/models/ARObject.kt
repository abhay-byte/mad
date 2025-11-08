package com.example.arexperiment.domain.models

import com.google.ar.core.Anchor
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale

data class ARObject(
        val id: String,
        val modelName: String,
        val modelPath: String,
        var position: Position = Position(x = 0f, y = 0f, z = 0f),
        var rotation: Rotation = Rotation(x = 0f, y = 0f, z = 0f),
        var scale: Scale = Scale(x = 1f, y = 1f, z = 1f),
        var anchor: Anchor? = null
)
