# Experiment 8: Deploying HackerNews App to Google Play Store

This document outlines the process of deploying our HackerNews app (from Experiment 7) to the Google Play Store and implementing performance optimizations.

## Part 1: App Deployment

### 1. Preparing the App for Release

#### 1.1. Update App Configuration
```kotlin
// In app/build.gradle.kts
android {
    defaultConfig {
        applicationId = "com.example.hackernews"
        versionCode = 1
        versionName = "1.0.0"
        // ... other configs
    }
}
```

#### 1.2. Generate Release Keystore
```bash
# Generate a keystore file
keytool -genkey -v -keystore hackernews.keystore -alias hackernews -keyalg RSA -keysize 2048 -validity 10000
```

#### 1.3. Configure Signing Config
```kotlin
// In app/build.gradle.kts
android {
    signingConfigs {
        create("release") {
            storeFile = file("hackernews.keystore")
            storePassword = "your_store_password"
            keyAlias = "hackernews"
            keyPassword = "your_key_password"
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### 2. Build Release Bundle/APK

#### 2.1. Generate App Bundle (Recommended)
```bash
# Using Gradle
./gradlew bundleRelease

# Output location: app/build/outputs/bundle/release/app-release.aab
```

#### 2.2. Generate APK (Alternative)
```bash
# Using Gradle
./gradlew assembleRelease

# Output location: app/build/outputs/apk/release/app-release.apk
```

### 3. Google Play Store Deployment

1. Create Developer Account
   - Visit [Google Play Console](https://play.google.com/console)
   - Pay one-time registration fee ($25)

2. Create App Listing
   ```plaintext
   App Name: HackerNews Reader
   Short Description: A clean, simple HackerNews client
   Full Description: Read the latest tech news...
   ```

3. Required Assets:
   - Screenshots (phone, tablet)
   - Feature graphic (1024x500px)
   - App icon (512x512px)
   - Privacy policy URL

4. Content Rating Questionnaire
   ```json
   {
     "violence": "NONE",
     "sexuality": "NONE",
     "language": "NONE",
     "controlled_substance": "NONE",
     "misc": "NONE"
   }
   ```

## Part 2: Performance Optimization

### 1. Memory Optimization

#### 1.1. ViewHolder Pattern Implementation
```kotlin
class StoryViewHolder(
    private val binding: ItemStoryBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(story: Story) {
        binding.apply {
            titleText.text = story.title
            // Use binding for better memory management
        }
    }
}
```

#### 1.2. Image Loading Optimization
```kotlin
// Using Coil for efficient image loading
implementation("io.coil-kt:coil:2.5.0")

// Usage
imageView.load(url) {
    crossfade(true)
    placeholder(R.drawable.placeholder)
    memoryCache(true)
    diskCache(true)
}
```

### 2. Network Optimization

#### 2.1. Implement Caching
```kotlin
// OkHttp Cache Configuration
val cacheSize = 10 * 1024 * 1024L // 10MB
val cache = Cache(context.cacheDir, cacheSize)

val client = OkHttpClient.Builder()
    .cache(cache)
    .addInterceptor { chain ->
        var request = chain.request()
        request = if (hasNetwork())
            request.newBuilder()
                .header("Cache-Control", "public, max-age=5")
                .build()
        else
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=604800")
                .build()
        chain.proceed(request)
    }
    .build()
```

#### 2.2. Data Pagination
```kotlin
class StoryPagingSource : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val page = params.key ?: 1
        return try {
            val stories = api.getTopStories(page, PAGE_SIZE)
            LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
```

### 3. CPU Optimization

#### 3.1. Worker Threads for Heavy Operations
```kotlin
class StoryRepository @Inject constructor(
    private val api: HackerNewsApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getTopStories() = withContext(dispatcher) {
        api.getTopStories().map { storyId ->
            async { api.getStory(storyId) }
        }.awaitAll()
    }
}
```

#### 3.2. Lazy Loading for RecyclerView
```kotlin
binding.recyclerView.apply {
    layoutManager = LinearLayoutManager(context)
    setHasFixedSize(true)
    recycledViewPool.setMaxRecycledViews(
        R.layout.item_story,
        DEFAULT_MAX_RECYCLED_VIEWS
    )
}
```

### 4. Battery Optimization

#### 4.1. Efficient Location Updates
```kotlin
private fun startLocationUpdates() {
    val request = LocationRequest.create().apply {
        priority = Priority.BALANCED_POWER_ACCURACY
        interval = TimeUnit.MINUTES.toMillis(15)
    }
}
```

#### 4.2. Background Work Optimization
```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED)
    .setRequiresBatteryNotLow(true)
    .build()

val workRequest = PeriodicWorkRequestBuilder<RefreshStoriesWorker>(
    1, TimeUnit.HOURS
).setConstraints(constraints)
 .build()
```

## Testing Performance

### 1. Using Android Profiler
```bash
# Launch Android Profiler
# Monitor:
- Memory usage
- CPU usage
- Network calls
- Battery consumption
```

### 2. Running Performance Tests
```kotlin
@LargeTest
class PerformanceTest {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun scrollPerformance() = benchmarkRule.measureRepeated {
        // Scroll through RecyclerView
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToPosition<ViewHolder>(50))
    }
}
```

## Monitoring Post-Release

1. Firebase Crashlytics Integration
```kotlin
// Add to build.gradle.kts
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}
```

2. Performance Monitoring
```kotlin
// Track custom traces
Firebase.performance.newTrace("story_load_trace").use { trace ->
    trace.start()
    // Load stories
    trace.stop()
}
```

---

This document serves as a guide for deploying the HackerNews app to the Play Store and implementing various performance optimizations. The actual implementation should be done step by step, testing thoroughly at each stage.