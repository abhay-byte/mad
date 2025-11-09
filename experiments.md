### **Experiment 1: Setting up the Development Environment**

The essential first step in mobile app development is establishing a functional and robust development environment. This experiment focuses on the installation and configuration of the necessary tools for creating Android applications.

*   **Android Development Environment:** This involves installing Android Studio, the official Integrated Development Environment (IDE) for Android app development. You will also set up the Android SDK (Software Development Kit), which contains the tools and libraries necessary to build, test, and debug Android apps.
*   **"Hello World" Application:** To verify that your environment is configured correctly, you will create a fundamental "Hello World" application for the Android platform. This simple app will display the text "Hello World" on the screen, serving as a successful baseline for all future projects.

### **Experiment 2: User Interface Design and Development**

This experiment delves into creating intuitive and engaging user interfaces (UIs), a critical aspect of a mobile app's success. You will learn the fundamentals of designing and building layouts for Android devices.

*   **Android UI Design:** You will learn to design a simple user interface for an Android app using XML (eXtensible Markup Language). This involves working with various layout managers (like `ConstraintLayout`, `LinearLayout`, `RelativeLayout`) and UI components (like `TextView`, `Button`, `EditText`) to create a visually appealing and functional design.
*   **Multi-Screen Navigation:** A key aspect of user experience is seamless navigation between different screens or `Activities`. This part of the experiment will guide you through implementing navigation within an Android app using `Intents` to launch new `Activities` and pass data between them.

### **Experiment 3: Database Integration in Mobile Apps**

Most applications require the ability to store and retrieve data locally on the device. This experiment focuses on integrating a local database into your Android app for persistent data storage.

*   **SQLite on Android:** You will learn how to create and manage a SQLite database in an Android application. This includes performing basic CRUD (Create, Read, Update, Delete) operations to handle data efficiently. You will work with the `SQLiteOpenHelper` class to manage database creation and versioning.

### **Experiment 4: Web Services and API Consumption**

Modern mobile applications often interact with web services to fetch and display dynamic data from the internet. This experiment covers how to consume data from remote APIs in an Android app.

*   **RESTful API in Android:** You will learn how to retrieve data from a RESTful API and display it within your Android app. This will involve making network requests using libraries like Retrofit or Volley and parsing the received JSON or XML data into usable objects.

### **Experiment 5: Location-Based Services and Mapping**

This experiment explores the integration of location-based services and maps into mobile applications, a popular feature in many modern apps from navigation to social media.

*   **Google Maps API on Android:** You will implement location tracking to get the device's current location and display it on a map within an Android app. This involves using the Google Maps API to embed maps and the Fused Location Provider API to efficiently retrieve location updates.

### **Experiment 6: Cross-Platform App Development with React Native or Flutter**

This experiment introduces cross-platform development, allowing you to build apps for multiple platforms (like Android and iOS) from a single codebase, saving time and resources.

*   **React Native Development:** You will develop a simple mobile app using React Native, a popular framework from Meta. You will utilize its JavaScript/TypeScript and component-based structure to build a functional cross-platform application.
*   **Flutter Development:** You will also create a mobile app using Flutter, Google's UI toolkit for building natively compiled applications. You will learn to implement various UI elements (widgets) and handle user input in the Dart programming language.

# **Experiment 7: Testing and Debugging a Basic HackerNews App**

## **Objective**
This experiment focuses on implementing, testing, and debugging a simple mobile application that fetches and displays data from the [HackerNews API](https://github.com/HackerNews/API?tab=readme-ov-file). The goal is to gain hands-on experience in applying testing frameworks, identifying bugs, and ensuring app stability through both unit and instrumental testing.

---

## **Scope**
In this experiment, you will **build a basic HackerNews Android application** that retrieves and displays top stories from HackerNews. The app will include essential features such as data fetching from a public REST API, rendering news titles in a list, and navigating to detailed story views.

Once the app is implemented, you will **perform systematic testing and debugging** using Android’s testing frameworks to ensure correct functionality and performance.

---

## **Key Activities**

### **1. App Development**
- Develop a **basic Android app** using **Kotlin** or **Java**.
- Integrate the **HackerNews API** to fetch and display top stories.
- Implement simple UI components such as a **RecyclerView** for the story list and an **Activity or Fragment** for detailed story content.
- Handle basic error cases (e.g., no internet, failed API calls).

### **2. Testing**
- Perform **unit testing** using **JUnit** to validate data-handling logic and API response parsing.
- Implement **instrumented tests** using **Espresso** to verify UI functionality on an emulator or physical device.
- Use **mock data** to test UI behavior without requiring live API calls.

### **3. Debugging**
- Use **Android Studio’s debugging tools** (breakpoints, Logcat, etc.) to identify and fix runtime errors.
- Analyze and resolve issues related to UI rendering, API integration, and app lifecycle.

---

## **Evaluation Criteria**
- App correctly fetches and displays HackerNews stories.  
- All implemented tests pass successfully.  
- Bugs and issues are identified, documented, and resolved.

---

## **Deliverables**
- A functional **basic HackerNews Android app**.  
- A set of **unit and instrumented test cases**.  
- A brief **report/documentation** summarizing the testing process, identified bugs, and debugging steps taken.

---

## **References**
- [HackerNews API Documentation](https://github.com/HackerNews/API?tab=readme-ov-file)
- [Android Testing Documentation](https://developer.android.com/training/testing)
- [JUnit 4 User Guide](https://junit.org/junit4/)
- [Espresso Testing Guide](https://developer.android.com/training/testing/espresso)


### **Experiment 8: App Deployment and Performance Optimization**

Once an app is built and tested, the next steps are to deploy it to users and ensure it runs smoothly. This experiment covers the final stages of the development lifecycle.

*   **Deployment to Google Play Store:** You will learn the process of packaging an Android app into an Android App Bundle (`.aab`) or APK (`.apk`), creating a production signing key, and deploying it to the Google Play Store for public distribution.
*   **Performance Optimization:** You will focus on optimizing app performance by analyzing and improving resource usage. This includes using tools like the Android Profiler to detect memory leaks, reduce CPU usage, improve network efficiency, and ensure a responsive user experience.

---

# **Experiment 9: Augmented Reality (AR) Integration**

## **Objective**  
This experiment explores the field of Augmented Reality (AR) by building a simple mobile application that either **places a virtual object into the real-world scene**, or **identifies a day-to-day object** using the camera. You will learn how to integrate AR features into an Android app (using ARCore) to detect surfaces, track motion, and overlay or recognise objects via the device’s camera.

## **Scope**  
In this experiment you will **build a basic AR Android application** with one of the following functionalities (you may choose one or implement both):

- **Object Placement Mode**: The app detects a horizontal or vertical surface in the camera view, and allows the user to tap to place a virtual 3D object (e.g., a simple model) at that location.  
- **Object Identification Mode**: The app uses the camera and AR capabilities to recognise a day-to-day real-world object (e.g., a chair, table, or a cup) and then overlays additional information or a virtual label on it.

The focus is on integrating ARCore features for surface detection, anchor placement, motion tracking, and possibly basic object recognition/labeling.

## **Key Activities**

### 1. AR App Development  
- Set up an Android app (in Kotlin or Java) using ARCore SDK.  
- For Object Placement Mode: implement surface detection (plane detection), and upon tap place a 3D model anchored to the real world.  
- For Object Identification Mode: integrate (or stub) a simple object-recognition mechanism (could be basic, e.g., pre-defined objects) and overlay a label or virtual object when the recognized real object is seen.  
- Provide UI/UX instructions to the user (e.g., “Move device to detect surface”, “Tap to place object”, or “Point camera at object to identify”).  
- Handle device permissions (camera, motion sensors) and AR session lifecycle.

### 2. AR Testing & Debugging  
- Test the app on a supported physical Android device (or emulator if supported).  
- Validate that surface detection works, objects are placed and remain anchored when the user moves the device.  
- In the identification mode, verify correct recognition (as implemented) and overlay behaviour.  
- Use logging, ARCore session state checks, and debugging tools (e.g., in Android Studio: Logcat, breakpoints) to find and fix issues (e.g., anchor drifting, object mis-placement, recognition failures).  
- Check for usability issues: too far/too close placement, objects placed in odd orientations, incorrect overlays.

### 3. Evaluation Criteria  
- The app successfully detects surfaces (placing mode) or identifies a real-world day-to-day object (identification mode).  
- Virtual object is correctly anchored and remains stable during device movement (placement mode).  
- Overlay/label appears correctly on recognised object (identification mode).  
- UX flows are clear and user-friendly (instructions visible, appropriate feedback).  
- Issues/bugs are documented and resolved.

## **Deliverables**  
- A functional **basic AR Android app** (either placement mode or identification mode, or both).  
- Screenshots or short demo video of the app in action.  
- A short **report/documentation** summarizing:  
  * Implementation approach (which mode you chose)  
  * Key AR features used (plane detection, anchors, recognition)  
  * Testing done and bugs found + how you fixed them  
  * Limitations and possible improvements.  
- Repository link (e.g., GitHub) with your app source code and README.


## **Reference Repository**  
You may use the following publicly-available repository as a starting point and reference:  
- ARCore Android SDK by Google — includes sample code for surface detection and object placement. (GitHub: [google-ar/arcore-android-sdk](https://github.com/google-ar/arcore-android-sdk)) :contentReference[oaicite:3]{index=3}  
- Alternatively, use the library SceneView for Android which simplifies placing 3D/AR content using ARCore. (GitHub: [SceneView/sceneview-android](https://github.com/SceneView/sceneview-android)) :contentReference[oaicite:5]{index=5}


## **References**  
- [ARCore Quickstart for Android](https://developers.google.com/ar/develop/java/quickstart) :contentReference[oaicite:6]{index=6}  
- [Content Placement Guidelines for AR](https://developers.google.com/ar/design/content/content-placement) :contentReference[oaicite:7]{index=7}  
- GitHub sample projects for ARCore :contentReference[oaicite:8]{index=8}  

---



### **Experiment 10: IoT Integration in Mobile Apps**

This experiment connects your mobile application to the Internet of Things (IoT), enabling your app to communicate with and control physical devices.

*   **Connecting with IoT Devices:** You will explore how to connect an Android app with various IoT devices, such as smart home appliances, environmental sensors, or wearables. This will typically involve using communication protocols like Bluetooth, Wi-Fi, or MQTT to send and receive data between the app and the physical hardware.