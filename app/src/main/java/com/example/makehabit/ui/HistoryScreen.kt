package com.example.makehabit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.makehabit.data.HabitRecord

@Composable
fun HistoryScreen(modifier: Modifier = Modifier){
    val historyList = listOf(
        HabitRecord("2024-03-25", "07:00", "08:30", "筋トレ"),
        HabitRecord("2024-03-25", "20:00", "24:00", "勉強"),
        HabitRecord("2024-03-24", "07:30", "08:30", "筋トレ")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("過去の実績", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        HabitBarChartCanvas(historyList)
    }
}