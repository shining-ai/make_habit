package com.example.makehabit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.makehabit.data.HabitRecord
import com.example.makehabit.ui.components.MinimalDropdownMenu
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MainScreenContent(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf("選択してください") }
    var taskOptions by remember { mutableStateOf(listOf("勉強", "読書", "運動")) }
    var newTask by remember { mutableStateOf("") }

    // 時間記録用
    var isRecording by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var historyList by remember { mutableStateOf(mutableListOf<HabitRecord>()) }

    // 時間フォーマット
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun startRecording() {
        startTime = timeFormat.format(Date())
        isRecording = true
    }

    fun stopRecording() {
        endTime = timeFormat.format(Date())
        isRecording = false

        // 新しい記録をhistoryListに追加
        val newRecord = HabitRecord(currentDate, startTime, endTime, selectedTask)
        historyList.add(newRecord)
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // アプリ名
        Text(
            text = "習慣化アプリ",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))


        // 新しいタスク追加用
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("タスクを追加") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (newTask.isNotBlank()) {
                        taskOptions = taskOptions + newTask
                        newTask = ""
                    }
                }
            ) {
                Text(text = "追加")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        MinimalDropdownMenu(
            taskOptions = taskOptions,
            selectedTask = selectedTask,
            onTaskSelected = { selectedTask = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 記録開始・終了
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(onClick = {if (!isRecording) startRecording()}) {
                Text(text = "開始")
            }
            Button(onClick = {if (isRecording) stopRecording()}){
                Text(text = "終了")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 記録した時間の表示
        if (isRecording) {
            Text(text = "記録中... 開始時刻: $startTime")
        } else if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            Text(text = "記録終了 時刻: $startTime - $endTime")
        }
        // 履歴表示
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "過去の実績")
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(historyList) { record ->
                Text("タスク: ${record.taskName}, 日付: ${record.date}, 時間: ${record.startTime} - ${record.endTime}")
            }

        }
    }
}