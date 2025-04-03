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
fun AchieveScreen(modifier: Modifier = Modifier){
    val achieveList = listOf(
        HabitRecord("2024-03-25", "07:30", "08:00", "筋トレ"),
    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("習慣化達成状況", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        HabitBarChartCanvas(achieveList)
    }
}