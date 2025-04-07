package com.example.makehabit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.makehabit.data.HabitRecord
import com.example.makehabit.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecordListScreen(viewModel: HabitViewModel) {
    val records = viewModel.recordList

    LazyColumn {
        items(records) { record ->
            Column(modifier = Modifier.padding(8.dp)) {
                Text("日付: ${record.date}")
                Text("タスク: ${record.taskName}")
                Text("時間: ${record.startTime} ~ ${record.endTime}")
            }
        }
    }
}


// タスクのオートコンプリート
@Composable
fun CustomAutoCompleteTextField(
    modifier: Modifier = Modifier,
    taskOptions: List<String>,
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    onTaskSelected: (String) -> Unit
) {
    var showSuggestions by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // 候補フィルター
    val filteredOptions = remember(taskText) {
        taskOptions.filter {
            it.contains(taskText, ignoreCase = true)
        }
    }

    Column(modifier = modifier) {
        // テキスト入力
        TextField(
            value = taskText,
            onValueChange = {
                onTaskTextChange(it)
                showSuggestions = it.isNotEmpty() || it.isBlank()
            },
            label = { Text("タスクを入力または選択") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    // 入力欄がフォーカスされていれば候補を表示
                    showSuggestions = focusState.isFocused
                },
            singleLine = true
        )

        // 候補表示
        if (showSuggestions && filteredOptions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray)
                    .background(Color.White)
                    .heightIn(max = 150.dp)
            ) {
                items(filteredOptions) { option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTaskTextChange(option)
                                onTaskSelected(option)
                                showSuggestions = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

//記録開始・終了ボタン
@Composable
fun RecordingButtons(
    isRecording: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            if (isRecording) onStop() else onStart()
        },
        modifier = modifier
    ) {
        Text(text = if (isRecording) "終了" else "開始")
    }
}

// 記録中のステータス表示
@Composable
fun RecordingStatus(isRecording: Boolean, startTime: String, endTime: String) {
    if (isRecording) {
        Text(text = "記録中... 開始時刻: $startTime")
    } else if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
        Text(text = "記録終了 時刻: $startTime - $endTime")
    }
}


// 履歴表示
@Composable
fun HistoryList(historyList: List<HabitRecord>) {
    Text(text = "過去の実績")
    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn {
        items(historyList) { record ->
            Text("${record.taskName}, ${record.date}, ${record.startTime} - ${record.endTime}")
        }
    }
}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier, viewModel: HabitViewModel) {
    val records = viewModel.recordList

    var expanded by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf("") }
    var taskOptions by remember { mutableStateOf(listOf("勉強", "読書", "運動")) }
    var newTask by remember { mutableStateOf("") }
    var taskText by remember { mutableStateOf("") }

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


        // 入力されたタスクが一覧にない場合、追加する
        if (selectedTask.isNotBlank() && selectedTask !in taskOptions) {
            taskOptions = taskOptions + selectedTask
        }

        // 新しい記録をhistoryListに追加
        val newRecord = HabitRecord(currentDate, startTime, endTime, selectedTask)
        viewModel.addRecord(newRecord)
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // アプリ名
        Text(text = "習慣化アプリ", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        CustomAutoCompleteTextField(
            taskOptions = taskOptions,
            taskText = selectedTask,
            onTaskTextChange = { selectedTask = it },
            onTaskSelected = { selectedTask = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 記録開始・終了ボタン
        RecordingButtons(
            isRecording = isRecording,
            onStart = { startRecording() },
            onStop = { stopRecording() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // 記録した時間の表示
        RecordingStatus(isRecording, startTime, endTime)
        // 履歴表示
        Spacer(modifier = Modifier.height(32.dp))
        // 履歴表示
        HistoryList(records)

    }
}