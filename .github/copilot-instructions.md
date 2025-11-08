### **Project Instructions for Android Experiments**

This document provides a comprehensive and detailed guide for the development process of the Android experiments. It is crucial that you follow these instructions precisely to ensure project consistency, quality, and a smooth workflow.

#### **1. Core Principle: A Methodical, Step-by-Step Approach**

You will operate on a strict, one-at-a-time basis. I will provide a specific task for a single experiment. You will not move on to the next experiment or a different task until the current one is completed, verified, and I provide the next instruction. This ensures a focused and linear progression through the learning objectives.

#### **2. Detailed Project & Directory Structure**

The entire project must be organized as follows. This structure is not a suggestion; it is a requirement.

*   **Root Directory (`/`)**:
    *   **`experiments.md`**: This is the master document containing the complete description, goals, and requirements for all ten experiments. You will refer to this file to understand the objectives for the experiment you are currently working on.
    *   **`code_guidelines.md`**: This file is the single source of truth for all coding standards, naming conventions, and code quality expectations. Before writing any code, you must be familiar with its contents.
    *   **`experiment_1/`**: A self-contained Android Studio project folder for Experiment 1. This folder will include its own `build.gradle` files, `src` directory, and all other necessary project files.
    *   **`experiment_2/`**: A separate, self-contained Android Studio project folder for Experiment 2.
    *   ...and so on for each of the ten experiments, resulting in folders `experiment_1` through `experiment_10`.

#### **3. Non-Negotiable Technology Mandates**

*   **UI Development with Jetpack Compose:** All user interfaces, from the simplest "Hello World" screen to complex data displays, **must** be built using Jetpack Compose. You are explicitly forbidden from using Android XML layouts. The goal is to build proficiency in modern Android UI development.
*   **Language:** All code must be written in Kotlin, following the best practices outlined in the guidelines.

#### **4. The Iterative Development and Version Control Workflow**

For every single task I give you, you must follow this exact cycle. This is the most critical part of the instructions.

**Step 1: Await Instruction**
*   Do nothing until I provide a clear directive. The directive will specify which experiment to work on and what task to accomplish.
*   **Example Directive:** "Begin Experiment 2. Your first task is to create two Composable screens: a `HomeScreen` and a `DetailsScreen`."

**Step 2: Implement the Change**
*   Navigate into the correct project directory (e.g., `cd experiment_2/`).
*   Open the project and implement the requested feature or change.
*   While coding, constantly refer to `code_guidelines.md` to ensure every line of code conforms to the project's standards.
*   Break down the task into the smallest logical, functional chunk. For example, if asked to build a form, the first chunk might be creating the UI elements, the next might be adding state management, and the next adding the submit button logic.

**Step 3: Compile the Project (The Sanity Check)**
*   After implementing a logical chunk of code, save all files.
*   From the terminal, inside the specific experiment's root directory (e.g., inside `experiment_2/`), run the following command to compile the application:
    ```bash
    ./gradlew assembleDebug
    ```
*   **Purpose:** This step is a crucial quality gate. It ensures your code has no syntax errors and the project can be successfully built before you proceed. If the build fails, you must fix the errors before moving to the next step.

**Step 4: Run and Verify on a Device (Mandatory)**
*   After a successful compilation, deploy and run the application.
*   You have to run the apk build by using ADB commands from the terminal.
*   **Purpose:** This step is for functional verification. You must interact with the app on an Android emulator or a physical device to confirm that the changes you made work exactly as intended and have not introduced any visual or logical bugs.

**Step 5: git add . git commit and git push,the Changes(The Logging Step)**
*   Once the change has been compiled and functionally verified, you must commit it to the Git repository.
*   Your commit messages must be descriptive and follow the conventional commit format (`type(scope): message`).
    *   **`feat`**: for a new feature.
    *   **`fix`**: for a bug fix.
    *   **`refactor`**: for code changes that neither fix a bug nor add a feature.
    *   **`style`**: for formatting changes, missing semicolons, etc.
    *   **`docs`**: for changes to documentation.
*   **Example Commit Messages:**
    *   `git add .`
    *   `git commit -m "feat(exp2): Create initial HomeScreen and DetailsScreen Composables"`
    *   `git commit -m "fix(exp4): Handle null response from user API endpoint"`
    *   `git commit -m "refactor(exp3): Abstract database operations into a repository"`
    *   `git push origin main`
    

**Step 6: Repeat the Cycle**
*   After a successful commit, await my next instruction for the current experiment. You will continue this **Implement -> Compile -> Run -> Commit** loop until all tasks for the given experiment are completed. Then, and only then, will we move to the next experiment.

#### **5. Workflow Management: Updating These Instructions**

To maintain flexibility, we may need to update these operational instructions. When a change to our workflow or guidelines is required, you will use the following command and process:

1.  **Initiate the Update:** Start your prompt with the exact phrase **"UPDATE INSTRUCTIONS:"**.
2.  **Provide the New Content:** Immediately following the command, provide the full, complete text for the section that needs to be changed or added.
3.  **Confirmation:** I will acknowledge the update and will operate under the new instructions from that point forward.

**Example Scenario:**

*   **Your Prompt:**
    > **UPDATE INSTRUCTIONS:**
    >
    > Let's modify the commit message format. All commit messages must now be prefixed with the experiment number, like this: `git commit -m "exp-4: Implement API data fetching"`

*   **My Response:**
    > Acknowledged. The instructions for commit messages have been updated. I will now use the "exp-X:" prefix for all future commits.

#### **6. Environment and Version Control: The `project_configuration.md` Mandate**

To prevent build errors and ensure absolute consistency across all projects, you **must** strictly adhere to the versions specified in the `project_configuration.md` file.

*   **Single Source of Truth:** This file is the definitive guide for all SDK, Gradle, and library versions. Do not use any other version unless explicitly instructed to via an **UPDATE INSTRUCTIONS** command.
*   **Project Setup:** When creating a new project for an experiment, your first step after project generation is to verify and align all versions in your `build.gradle.kts` files and `gradle-wrapper.properties` with what is documented in `project_configuration.md`.
*   **No Deviation:** Any deviation from these versions is considered a failure to follow instructions and must be corrected immediately. This rule is in place to minimize "it works on my machine" issues and streamline the development process.

#### **7. Experiment Completion: The Summary `README.md`**

Upon the successful completion of all tasks for a given experiment, you must perform one final action before awaiting the instruction to begin the next experiment: **generate a summary README file.**

This file serves as a consolidated report covering the experiment's aim, key code, and final outcome, making it easy to review and document the project's progress.

**Workflow:**

1.  **Final Task:** After the final feature of an experiment is implemented, verified, and committed, this becomes your next and last task for that experiment.
2.  **Create the File:** Create a new file named `README.md` inside the root directory of the completed experiment's folder (e.g., `/experiment_1/README.md`).
3.  **Populate the Content:** The `README.md` must be structured with the following sections in this exact order:
    *   **Experiment No:** The number of the experiment.
    *   **Aim:** A clear, one-line statement describing the main objective of the experiment.
    *   **Code:** A collection of the most important code snippets written.
    *   **Output:** A textual description of the final application's state and appearance.
    *   **Result:** A concluding statement confirming the successful completion of the aim.

**Code Section Rules:**

For each significant file you created or modified within the `Code` section, you must follow this exact format:
*   A level-3 Markdown header (`###`) with the full path to the file from the module root (e.g., `app/src/main/java/com/example/experiment1/MainActivity.kt`).
*   A Markdown code block using triple backticks and the `kotlin` language identifier.
*   Inside the code block, paste **only the main, relevant code** from that file.

**What is "Main, Relevant Code"?**

Your goal is to be concise. Exclude boilerplate like package declarations and most imports. Focus on the core logic:

*   **For UI files:** The primary `@Composable` functions.
*   **For ViewModels:** The class definition, public state holders (`StateFlow`, `MutableState`), and public functions.
*   **For Data Classes:** The complete class definition.
*   **For Repositories/Services:** The interface or class definition and its public methods.

**Example `README.md` for a hypothetical Experiment 1:**


# Experiment 1: Summary

### **Experiment No:**
1

### **Aim:**
To set up an Android development environment and create a basic "Hello World" application using Jetpack Compose.

---

### **Code:**

#### `app/src/main/java/com/example/experiment1/ui/theme/Theme.kt`

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

#### `app/src/main/java/com/example/experiment1/MainActivity.kt`

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Experiment1Theme {
        Greeting("Android")
    }
}
```

---

### **Output:**

The final application, when run, displays a single screen with a white background. Centered on the screen is the text "Hello Android!".

---

### **Result:**

Thus, the experiment to create a basic Jetpack Compose application and display a greeting was successfully completed.