package com.example.experiment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.experiment3.data.database.NoteDatabase
import com.example.experiment3.data.repository.NoteRepository
import com.example.experiment3.ui.screens.NoteEditScreen
import com.example.experiment3.ui.screens.NoteListScreen
import com.example.experiment3.ui.theme.Experiment3Theme
import com.example.experiment3.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database and repository
        val database = NoteDatabase.getDatabase(applicationContext)
        val repository = NoteRepository(database.noteDao())
        
        setContent {
            Experiment3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesApp(repository)
                }
            }
        }
    }
}

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