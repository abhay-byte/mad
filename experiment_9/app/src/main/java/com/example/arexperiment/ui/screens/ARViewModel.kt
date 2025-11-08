package com.example.arexperiment.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arexperiment.domain.ar.ARManager
import com.example.arexperiment.domain.ar.ARState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.ar.ARScene
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ARViewModel @Inject constructor(private val arManager: ARManager) : ViewModel() {

    val arState: StateFlow<ARState> = arManager.arState

    fun initializeAR(arScene: ARScene) {
        viewModelScope.launch { arManager.initializeARScene(arScene) }
    }

    fun getVisiblePlanes() = arManager.getVisiblePlanes()

    fun getLightEstimate() = arManager.getLightEstimate()

    override fun onCleared() {
        super.onCleared()
        arManager.clearAnchors()
    }
}
