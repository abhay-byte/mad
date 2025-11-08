package com.example.arexperiment.domain.ar.cloud

import android.content.Context
import com.google.ar.core.Anchor
import com.google.ar.core.Session
import io.github.sceneview.ar.node.CloudAnchorNode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class CloudAnchorManager @Inject constructor(private val context: Context) {
    private val _cloudAnchorState = MutableStateFlow<CloudAnchorState>(CloudAnchorState.None)
    val cloudAnchorState: StateFlow<CloudAnchorState> = _cloudAnchorState.asStateFlow()

    private var cloudAnchorNode: CloudAnchorNode? = null

    fun hostAnchor(session: Session, anchor: Anchor, onComplete: (String?) -> Unit) {
        _cloudAnchorState.value = CloudAnchorState.Hosting

        cloudAnchorNode =
                CloudAnchorNode(session.engine, anchor).apply {
                    host(session) { cloudAnchorId, state ->
                        when {
                            state.isError -> {
                                _cloudAnchorState.value = CloudAnchorState.Error(state.toString())
                                onComplete(null)
                            }
                            cloudAnchorId != null -> {
                                _cloudAnchorState.value = CloudAnchorState.Hosted(cloudAnchorId)
                                onComplete(cloudAnchorId)
                            }
                        }
                    }
                }
    }

    fun resolveAnchor(
            session: Session,
            cloudAnchorId: String,
            onComplete: (CloudAnchorNode?) -> Unit
    ) {
        _cloudAnchorState.value = CloudAnchorState.Resolving

        CloudAnchorNode.resolve(session.engine, session, cloudAnchorId) { state, node ->
            if (state.isError || node == null) {
                _cloudAnchorState.value = CloudAnchorState.Error(state.toString())
                onComplete(null)
            } else {
                cloudAnchorNode = node
                _cloudAnchorState.value = CloudAnchorState.Resolved(node)
                onComplete(node)
            }
        }
    }

    fun clear() {
        cloudAnchorNode?.detachAnchor()
        cloudAnchorNode = null
        _cloudAnchorState.value = CloudAnchorState.None
    }
}

sealed class CloudAnchorState {
    object None : CloudAnchorState()
    object Hosting : CloudAnchorState()
    data class Hosted(val cloudAnchorId: String) : CloudAnchorState()
    object Resolving : CloudAnchorState()
    data class Resolved(val node: CloudAnchorNode) : CloudAnchorState()
    data class Error(val message: String) : CloudAnchorState()
}
