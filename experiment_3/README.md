# Experiment 3: SQLite Database with Room

## Experiment No:
3

## Aim:
To implement a Note-Taking app using SQLite database with Room persistence library, showcasing CRUD operations and navigation using Jetpack Compose.

## Code:

### `app/src/main/java/com/example/experiment3/data/entity/Note.kt`
```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Date = Date()
)
```

### `app/src/main/java/com/example/experiment3/data/dao/NoteDao.kt`
```kotlin
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?
}
```

### `app/src/main/java/com/example/experiment3/ui/viewmodel/NoteViewModel.kt`
```kotlin
class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val allNotes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            val note = Note(title = title, content = content)
            repository.insertNote(note)
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            val note = Note(id = id, title = title, content = content)
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }
}
```

### `app/src/main/java/com/example/experiment3/MainActivity.kt`
```kotlin
@Composable
fun NotesApp(repository: NoteRepository) {
    val navController = rememberNavController()
    val viewModel: NoteViewModel = viewModel(
        factory = NoteViewModel.Factory(repository)
    )
    val notes by viewModel.allNotes.collectAsState()
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NoteListScreen(
                notes = notes,
                onNoteClick = { noteId ->
                    navController.navigate("note/edit/$noteId")
                },
                onAddClick = {
                    navController.navigate("note/add")
                },
                onDeleteClick = { note ->
                    viewModel.deleteNote(note)
                }
            )
        }
        
        composable(
            route = "note/edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
            val note = viewModel.allNotes.value.find { it.id == noteId }
            NoteEditScreen(
                note = note,
                onSaveClick = { title, content ->
                    scope.launch {
                        viewModel.updateNote(noteId, title, content)
                        navController.navigateUp()
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable("note/add") {
            NoteEditScreen(
                note = null,
                onSaveClick = { title, content ->
                    scope.launch {
                        viewModel.addNote(title, content)
                        navController.navigateUp()
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
```

## Output:
The app successfully implements a fully functional note-taking application with the following features:
1. List view of all notes sorted by timestamp
2. Ability to add new notes
3. Edit existing notes
4. Delete notes
5. Material 3 design with proper navigation
6. Data persistence using Room database
7. MVVM architecture with Repository pattern
8. Kotlin coroutines and Flow for asynchronous operations

## Result:
The experiment was successfully completed, demonstrating the implementation of a SQLite database using Room persistence library in an Android application. The app showcases CRUD operations, proper architecture, and modern Android development practices using Jetpack Compose.