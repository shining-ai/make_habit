package com.example.makehabit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.makehabit.data.HabitRecord

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