package com.example.arexperiment.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ModelItem(val name: String, val path: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionMenu(
        models: List<ModelItem>,
        onModelSelected: (ModelItem) -> Unit,
        modifier: Modifier = Modifier
) {
    LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
    ) {
        items(models) { model ->
            ElevatedCard(onClick = { onModelSelected(model) }) {
                Text(text = model.name, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
