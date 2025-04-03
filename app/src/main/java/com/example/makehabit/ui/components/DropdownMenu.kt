package com.example.makehabit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MinimalDropdownMenu(
    taskOptions: List<String>,
    selectedTask: String,
    onTaskSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text("勉強", Modifier.clickable{expanded = !expanded})
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            taskOptions.forEach { task ->
                DropdownMenuItem(
                    text = { Text(task) },
                    onClick = { onTaskSelected(task); expanded = false }
                )
            }
        }
    }
}

