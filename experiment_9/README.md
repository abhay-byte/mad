# Experiment 9: Android 3D Scene Viewer

This experiment demonstrates the implementation of 3D scene viewing capabilities in Android using the SceneView library. The implementation is based on samples from the SceneView Android project, specifically focusing on the model viewer example.

## Sample Implementation

### Activity.kt - Main Activity
```kotlin
class Activity : AppCompatActivity(R.layout.activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFullScreen(
            findViewById(R.id.rootView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar)?.apply {
            doOnApplyWindowInsets { systemBarsInsets ->
                (layoutParams as ViewGroup.MarginLayoutParams).topMargin = systemBarsInsets.top
            }
            title = ""
        })

        supportFragmentManager.commit {
            add(R.id.containerFragment, MainFragment::class.java, Bundle())
        }
    }
}
```

### MainFragment.kt - 3D Scene Implementation
```kotlin
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var sceneView: SceneView
    private lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)
        loadingView = view.findViewById(R.id.loadingView)

        viewLifecycleOwner.lifecycleScope.launch {
            val hdrFile = "environments/studio_small_09_2k.hdr"

            sceneView.environmentLoader.loadHDREnvironment(hdrFile).apply {
                sceneView.indirectLight = this?.indirectLight
                sceneView.skybox = this?.skybox
            }
            sceneView.cameraNode.apply {
                position = Position(z = 4.0f)
            }
            val modelFile = "models/MaterialSuite.glb"
            val modelInstance = sceneView.modelLoader.createModelInstance(modelFile)

            val modelNode = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 2.0f,
            )
            modelNode.scale = Scale(0.05f)
            sceneView.addChildNode(modelNode)
            loadingView.isGone = true
        }
    }
}
```

## Key Features Demonstrated

1. **Scene Setup**
   - Loading HDR environment for lighting
   - Camera positioning
   - Model loading and scaling

2. **UI Integration**
   - Full-screen display
   - Loading state management
   - System UI integration

3. **3D Model Handling**
   - GLB model loading
   - Node transformation
   - Scene hierarchy management

## Original Project Credits

This implementation is based on the [SceneView Android](https://github.com/SceneView/sceneview-android) project:

* **Original Project:** [SceneView/sceneview-android](https://github.com/SceneView/sceneview-android)
* **License:** Apache License 2.0
* **Authors:** The SceneView Authors and Contributors

## Features Demonstrated

- 3D model loading and rendering
- Scene manipulation
- Camera controls
- Lighting and environment

## Implementation Notes

This experiment focuses on the sample implementations provided by the SceneView library, specifically:

1. Basic 3D model viewing
2. Scene interaction
3. Camera manipulation

## Dependencies

```gradle
dependencies {
    implementation 'io.github.sceneview:sceneview:1.0.10'
}
```

## Sample Usage

The implementation demonstrates basic usage of SceneView's core features:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // SceneView implementation
        }
    }
}
```

## Original License

```
Copyright 2023 SceneView

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Learning Outcomes

- Understanding 3D scene rendering in Android
- Working with SceneView library
- Implementing camera and model controls
- Managing 3D assets in Android applications

## References

1. [SceneView Documentation](https://github.com/SceneView/sceneview-android)
2. [Android 3D Graphics Guide](https://developer.android.com/develop/ui/views/graphics)
3. [Filament Documentation](https://google.github.io/filament/)