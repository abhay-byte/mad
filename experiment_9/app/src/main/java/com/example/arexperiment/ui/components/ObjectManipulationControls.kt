package com.example.arexperiment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arexperiment.domain.models.ARObject
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale

@Composable
fun ObjectManipulationControls(
        selectedObject: ARObject?,
        onUpdateTransform:
                (
                        position: io.github.sceneview.math.Position?,
                        rotation: Rotation?,
                        scale: Scale?) -> Unit,
        onRemoveObject: () -> Unit,
        onResetTransform: () -> Unit,
        modifier: Modifier = Modifier
) {
    var rotationValue by remember { mutableFloatStateOf(0f) }
    var scaleValue by remember { mutableFloatStateOf(1f) }

    ElevatedCard(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Rotation controls
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Rotation")
                Row {
                    IconButton(
                            onClick = {
                                rotationValue -= 45f
                                onUpdateTransform(null, Rotation(y = rotationValue), null)
                            }
                    ) { Icon(Icons.Default.RotateLeft, "Rotate left") }
                    IconButton(
                            onClick = {
                                rotationValue += 45f
                                onUpdateTransform(null, Rotation(y = rotationValue), null)
                            }
                    ) { Icon(Icons.Default.RotateRight, "Rotate right") }
                }
            }

            // Scale controls
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Scale")
                Row {
                    IconButton(
                            onClick = {
                                scaleValue = (scaleValue - 0.1f).coerceAtLeast(0.1f)
                                onUpdateTransform(null, null, Scale(scaleValue))
                            }
                    ) { Icon(Icons.Default.ZoomOut, "Scale down") }
                    IconButton(
                            onClick = {
                                scaleValue = (scaleValue + 0.1f).coerceAtMost(3f)
                                onUpdateTransform(null, null, Scale(scaleValue))
                            }
                    ) { Icon(Icons.Default.ZoomIn, "Scale up") }
                }
            }

            Slider(
                    value = scaleValue,
                    onValueChange = { newValue ->
                        scaleValue = newValue
                        onUpdateTransform(null, null, Scale(newValue))
                    },
                    valueRange = 0.1f..3f,
                    modifier = Modifier.fillMaxWidth()
            )

            // Action buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onRemoveObject) { Icon(Icons.Default.Delete, "Remove object") }
                IconButton(
                        onClick = {
                            rotationValue = 0f
                            scaleValue = 1f
                            onResetTransform()
                        }
                ) { Icon(Icons.Default.Refresh, "Reset transform") }
            }
        }
    }
}
