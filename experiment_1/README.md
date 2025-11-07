# Experiment 1 — Setup & Hello Compose

This folder contains a minimal skeleton for Experiment 1: a Jetpack Compose "Hello Compose" application written in Kotlin.

What this is:
- A minimal `app` module with a Compose `MainActivity` and AndroidManifest.
- Placeholder Gradle files (`build.gradle.kts`, `app/build.gradle.kts`) with typical Compose dependencies.

Important notes before building:
1. This skeleton does not include an Android Gradle wrapper (`gradlew`). Use Android Studio to open the `experiment_1/` folder — Android Studio will configure the recommended Gradle plugin and can generate the wrapper for you.
2. Ensure you have the Android SDK and Kotlin plugin installed via Android Studio.

## Build and Run Instructions

### Option 1: Using Android Studio (Recommended)
1. Open Android Studio
2. Choose "Open" and select the `c:\Users\abhay\temp\MAD\experiment_1` folder
3. Allow Android Studio to sync Gradle, suggest plugin versions, and generate the wrapper
4. Run the `app` configuration on an emulator or device

### Option 2: Command Line Build and Install
1. Build the debug APK:
```powershell
.\gradlew assembleDebug
```

2. Install and run on a connected device/emulator using ADB:
```powershell
# List connected devices
adb devices

# Install the APK
adb install .\app\build\outputs\apk\debug\app-debug.apk

# Start the app
adb shell am start -n "com.example.experiment1/.MainActivity"

# View logs (optional)
adb logcat | Select-String -Pattern "experiment1"
```

### Option 3: Direct ADB Commands After Build
Once the APK is built successfully, you can use these ADB commands for development:

```powershell
# Uninstall existing app (if needed)
adb uninstall com.example.experiment1

# Install and run in one command
adb install -r .\app\build\outputs\apk\debug\app-debug.apk && adb shell am start -n "com.example.experiment1/.MainActivity"

# Stop the app (if needed)
adb shell am force-stop com.example.experiment1
```

Note: Make sure:
1. USB debugging is enabled on your device
2. Device is connected and authorized
3. ADB is in your system PATH
4. Run `adb devices` to verify device connection
