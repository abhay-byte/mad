# Project Configuration and Versions

This document specifies the exact versions for the Android Gradle Plugin, SDK, and other core build tools. These versions **must** be used consistently across all ten experiment projects to ensure a stable and predictable build environment.

---

## **Android SDK Versions**

- **Compile SDK (`compileSdk`)**: `35`  
  Use API level 35 (Android 15) for compilation.

- **Target SDK (`targetSdk`)**: `35`  
  New apps should target API level 35 or higher.

- **Minimum SDK (`minSdk`)**: `24`  
  Android 7.0 (API level 24) as the minimum baseline.

- **Build Tools Version (`buildToolsVersion`)**: `35.0.0`  
  Use build tools associated with API 35.

---

## **Gradle and Build Tool Versions**

- **Android Gradle Plugin (AGP) Version**: `8.13.0`  
  Current stable release of the Android Gradle plugin.

- **Gradle Version**: `8.14.3`  
  Latest stable Gradle version compatible with AGP 8.13.0.

---

## **Kotlin and Jetpack Compose Versions**

- **Kotlin Version**: `2.0.20`  
  Kotlin 2.0.x is the latest stable major release.

- **Jetpack Compose Compiler Version**: `2.0.20`  
  Compose compiler version aligned with Kotlin 2.0.20.  
  Since Kotlin 2.0, Compose compiler is shipped within the Kotlin repository.

---

## **Summary Table**

| Component                     | Version       |
|-------------------------------|---------------|
| compileSdk                    | 35            |
| targetSdk                     | 35            |
| minSdk                        | 24            |
| buildToolsVersion             | 35.0.0        |
| Android Gradle Plugin (AGP)   | 8.13.0        |
| Gradle Wrapper / Gradle       | 8.14.3        |
| Kotlin                        | 2.0.20        |
| Compose Compiler              | 2.0.20        |

---

## **Notes**

- Google Play now requires new and updated apps to target **API level 35** or higher.
- With Kotlin 2.0+, use the Compose Compiler Gradle plugin approach—Compose compiler versions now match Kotlin versions.
- Ensure Android Studio (Hedgehog / Iguana or later) supports AGP 8.x and Kotlin 2.0.
- Maintain consistent versions across all experiment modules to prevent build issues.
