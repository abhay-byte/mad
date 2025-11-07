# Project General Coding Guidelines

## Core Principles

- **Clean Code Philosophy:** All code must be written with the primary goal of being clean, simple, and highly readable. Follow the "Boy Scout Rule": always leave the code cleaner than you found it. Your code's logic should be straightforward and easy to follow. Avoid clever tricks or overly complex abstractions that obscure the code's intent.

- **Code Conciseness and Efficiency:** Aim to minimize the Lines of Code (LOC) as much as possible, **but never at the expense of clarity or readability**.
    -   Leverage Kotlin's powerful features like scope functions (`let`, `apply`, `run`), extension functions, and concise expression syntax to write expressive yet brief code.
    -   Avoid redundancy by strictly following the Don't Repeat Yourself (DRY) principle. Abstract repeated logic into reusable functions.

## Code Style
-   Use modern Kotlin features, including coroutines, flows, and higher-order functions where appropriate, to write more efficient and readable asynchronous code.
-   Follow the official [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html) and [Android Kotlin style guide](https://developer.android.com/kotlin/style-guide).
-   For Jetpack Compose, adhere to the official [Compose API guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md).

## Naming Conventions
-   **Composables:** Use `PascalCase` for Composable functions.
-   **Classes & Interfaces:** Use `PascalCase`.
-   **Functions & Methods:** Use `camelCase`.
-   **Variables:** Use `camelCase`.
-   **Constants (`const val`):** Use `ALL_CAPS` with underscore separators (e.g., `const val MAX_RETRIES = 3`).
-   **Private Members:** Prefix private class properties and methods with an underscore (`_`).
    ```kotlin
    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()
    ```

## Code Quality
-   **Clarity and Readability:** Use meaningful, descriptive names for variables, functions, and classes that clearly describe their purpose. The code should be as self-documenting as possible.
-   **Comments:** Write comments only to explain *why* a piece of complex logic exists, not *what* the code is doing. Clean code should make the "what" obvious.
-   **Error Handling:** Implement robust error handling for user inputs, API calls (using mechanisms like `Result` or `try-catch` blocks within coroutines), and database transactions.
-   **Single Responsibility Principle (SRP):** Functions, classes, and Composables must have a single, well-defined responsibility. Keep your functions and Composables small and focused. This is fundamental to writing clean, testable, and maintainable code.