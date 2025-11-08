# Experiment 2

### Experiment No:
2

### Aim:
Create two Jetpack Compose screens (Home and Details) with navigation between them.

---

### Code:

#### app/src/main/java/com/example/experiment2/HomeScreen.kt
```kotlin
@Composable
fun HomeScreen(onNavigateToDetails: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Experiment 2",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = onNavigateToDetails,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Go to Details")
            }
        }
    }
}
```

#### app/src/main/java/com/example/experiment2/DetailsScreen.kt
```kotlin
@Composable
fun DetailsScreen(onNavigateBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Details Screen",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Go Back")
            }
        }
    }
}
```

#### app/src/main/java/com/example/experiment2/MainActivity.kt
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Experiment2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onNavigateToDetails = {
                                    navController.navigate("details")
                                }
                            )
                        }
                        composable("details") {
                            DetailsScreen(
                                onNavigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
```

#### app/src/main/java/com/example/experiment2/ui/theme/Theme.kt
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
@Composable
fun Experiment2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

---

### Output:
The app displays a Home screen with a button to navigate to Details. The Details screen has a button to go back. Both use Material 3 theming.

---

### Result:
Home and Details screens with navigation were successfully implemented and verified.