package com.example.makehabit

import android.graphics.drawable.Icon
import android.os.Bundle
import android.service.autofill.FillEventHistory
import android.widget.MediaController
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.makehabit.ui.theme.MakeHabitTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class HabitScreen(){
    Home,
    History
}

data class HabitRecord(
    val date: String,
    val startTime: String,
    val endTime: String,
    val taskName: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakeHabitTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    NavigationGraph(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") { MainScreenContent() }
        composable("history") { HistoryScreen() }
        composable("achieve") { AchieveScreen() }
    }
}




@Composable
fun HabitBarChartCanvas(historyList: List<HabitRecord>, modifier: Modifier = Modifier){
    val barHeight = 40.dp // 各バーの高さ
    val chartWidth = 300.dp // バーの横幅
    val barSpacing = 40.dp // バー同士の間隔
    val legendHeight = 50.dp // 凡例の高さ

    val taskColors = mapOf(
        "筋トレ" to Color.Blue,
        "勉強" to Color.Green,
        "読書" to Color.Red,
        "その他" to Color.Gray
    )

    // 日付ごとに記録をまとめる
    val groupedRecords = historyList.groupBy { it.date }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((groupedRecords.size * (barHeight.value + barSpacing.value) + 50).dp) // ラベル用に+50dp
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val hourWidth = canvasWidth / 24f

        // 時間のラベルを描画 (0, 6, 12, 18, 24)
        val labelHours = listOf(0, 6, 12, 18, 24)
        val dateTextPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 30f
            textAlign = android.graphics.Paint.Align.LEFT
        }
        val timeTextPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        var currentY = 0f // Y軸の位置
        // ① 凡例を描画
        taskColors.entries.forEachIndexed { index, (task, color) ->
            val legendX = index * 150f
            drawRect(
                color = color,
                topLeft = Offset(legendX, currentY),
                size = Size(40f, 40f) // 色のマーカー
            )
            drawContext.canvas.nativeCanvas.drawText(
                task,
                legendX + 50f,
                currentY + 30f,
                dateTextPaint
            )
        }

        currentY += legendHeight.toPx() // 凡例の分だけY軸を下げる
        groupedRecords.entries.forEachIndexed { index, (date, records) ->
            val barY = currentY + index * (barHeight.toPx() + barSpacing.toPx()) + 40f

            // 24時間の枠を描画
            drawRect(
                color = Color.Black,
                topLeft = Offset(0f, barY),
                size = Size(canvasWidth, barHeight.toPx()),
                style = Stroke(width = 3f) // 枠線
            )

            records.forEach { record ->
                val startHour = record.startTime.substring(0, 2).toFloat()
                val endHour = record.endTime.substring(0, 2).toFloat()
                val taskColor = when (record.taskName) {
                    "筋トレ" -> Color.Blue
                    "勉強" -> Color.Green
                    "読書" -> Color.Red
                    else -> Color.Gray
                }

                // 記録部分を塗りつぶし
                drawRect(
                    color = taskColor,
                    topLeft = Offset(startHour * hourWidth, barY + 1f),
                    size = Size((endHour - startHour) * hourWidth, (barHeight - 1.dp).toPx())
                )
            }
            // 日付ラベルを左側に表示
            drawContext.canvas.nativeCanvas.drawText(
                date,
                0f,
                barY - 10f,
                dateTextPaint
            )
            // 時間ラベルの表示
            labelHours.forEach { hour ->
                val x = hour * hourWidth
                drawContext.canvas.nativeCanvas.drawText(
                    "$hour:00",
                    x,
                    barY + barHeight.toPx() + 35f,
                    timeTextPaint
                )
            }

        }
    }
}

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

@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavHostController){
    NavigationBar (
        modifier = modifier
    ){
        NavigationBarItem(
            icon = {},
            label = {Text(text = "Home")},
            selected = true,
            onClick = {navController.navigate("home")}
        )
        NavigationBarItem(
            icon = {},
            label = {
                Text(
                    text = "実績"
                )
            },
            selected = false,
            onClick = {navController.navigate("history")}
        )
        NavigationBarItem(
            icon = {},
            label = {
                Text(
                    text = "習慣"
                )
            },
            selected = false,
            onClick = {navController.navigate("achieve")}
        )
    }
}


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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MakeHabitTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            MainScreenContent(modifier = Modifier.padding(innerPadding))
        }

    }
}