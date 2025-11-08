# Experiment 1

### Experiment No:
1

### Aim:
Set up Android development and display a "Hello World" screen using Jetpack Compose.

---

### Code:

#### app/src/main/java/com/example/experiment1/ui/theme/Theme.kt
```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)
```

#### app/src/main/java/com/example/experiment1/MainActivity.kt
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Experiment1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier.padding(16.dp),
        textAlign = TextAlign.Center
    )
}
```

---

### Output:
The app displays a single screen with a white background and the text "Hello Android!" centered.

---

### Result:
The "Hello World" Jetpack Compose app was successfully created and verified.
