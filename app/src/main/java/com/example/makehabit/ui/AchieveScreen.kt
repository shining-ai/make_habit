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
import com.example.makehabit.viewmodel.HabitViewModel

@Composable
fun AchieveScreen(modifier: Modifier = Modifier, viewModel: HabitViewModel){
    val historyList = viewModel.recordList

    val commonTimesByTask = getCommonTimeForTasks(historyList)

//    val achieveList =
//        listOf(
//        HabitRecord(1,"2024-03-25", "07:30", "08:30", "筋トレ"),
//        HabitRecord("2024-03-26", "07:00", "08:00", "筋トレ"),
//    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("習慣化達成状況", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        HabitBarChartCanvas(commonTimesByTask)
    }
}