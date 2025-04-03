package com.example.makehabit.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.makehabit.data.HabitRecord
import androidx.compose.foundation.lazy.items

@Composable
fun HabitRecordList(historyList: List<HabitRecord>) {
    LazyColumn {
        items(historyList) { record ->
            Text("タスク: ${record.taskName}, 日付: ${record.date}, 時間: ${record.startTime} - ${record.endTime}")
        }
    }
}