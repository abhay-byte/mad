package com.example.arexperiment.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arexperiment.domain.ar.ARState
import com.example.arexperiment.domain.models.ARObject
import com.example.arexperiment.domain.usecase.ARObjectInteractionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(private val arObjectInteractionUseCase: ARObjectInteractionUseCase) : ViewModel() {

    private val _arState = mutableStateOf<ARState>(ARState.NotReady)
    val arState: State<ARState> = _arState

    private val _selectedObjectId = MutableStateFlow<String?>(null)
    val selectedObjectId: StateFlow<String?> = _selectedObjectId.asStateFlow()

    private val _objects = MutableStateFlow<List<ARObject>>(emptyList())
    val objects: StateFlow<List<ARObject>> = _objects.asStateFlow()

    fun updateARState(newState: ARState) {
        _arState.value = newState
    }

    fun placeObject(
            modelName: String,
            modelPath: String,
            arSceneView: ArSceneView,
            position: Position? = null
    ) {
        viewModelScope.launch {
            arObjectInteractionUseCase.placeObject(modelName, modelPath, arSceneView, position)
                    .onSuccess { objectId ->
                        _selectedObjectId.value = objectId
                        refreshObjects()
                    }
        }
    }

    fun updateObjectTransform(
            id: String,
            position: Position? = null,
            rotation: Rotation? = null,
            scale: Scale? = null
    ) {
        viewModelScope.launch {
            arObjectInteractionUseCase.updateObjectTransform(id, position, rotation, scale)
            refreshObjects()
        }
    }

    fun removeObject(id: String) {
        viewModelScope.launch {
            arObjectInteractionUseCase.removeObject(id)
            if (_selectedObjectId.value == id) {
                _selectedObjectId.value = null
            }
            refreshObjects()
        }
    }

    fun selectObject(id: String?) {
        _selectedObjectId.value = id
    }

    fun clearAllObjects() {
        viewModelScope.launch {
            arObjectInteractionUseCase.clearAllObjects()
            _selectedObjectId.value = null
            refreshObjects()
        }
    }

    private fun refreshObjects() {
        viewModelScope.launch {
            arObjectInteractionUseCase.getAllObjects().onSuccess { objectList ->
                _objects.value = objectList
            }
        }
    }
}
