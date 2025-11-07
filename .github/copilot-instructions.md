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

**Step 4: Run and Verify on a Device (The Functional Check)**
*   After a successful compilation, deploy and run the application.
*   You can do this either by using the "Run" button in Android Studio or by using ADB commands from the terminal.
*   **Purpose:** This step is for functional verification. You must interact with the app on an Android emulator or a physical device to confirm that the changes you made work exactly as intended and have not introduced any visual or logical bugs.

**Step 5: Commit the Changes (The Logging Step)**
*   Once the change has been compiled and functionally verified, you must commit it to the Git repository.
*   Your commit messages must be descriptive and follow the conventional commit format (`type(scope): message`).
    *   **`feat`**: for a new feature.
    *   **`fix`**: for a bug fix.
    *   **`refactor`**: for code changes that neither fix a bug nor add a feature.
    *   **`style`**: for formatting changes, missing semicolons, etc.
    *   **`docs`**: for changes to documentation.
*   **Example Commit Messages:**
    *   `git commit -m "feat(exp2): Create initial HomeScreen and DetailsScreen Composables"`
    *   `git commit -m "fix(exp4): Handle null response from user API endpoint"`
    *   `git commit -m "refactor(exp3): Abstract database operations into a repository"`

**Step 6: Repeat the Cycle**
*   After a successful commit, await my next instruction for the current experiment. You will continue this **Implement -> Compile -> Run -> Commit** loop until all tasks for the given experiment are completed. Then, and only then, will we move to the next experiment.