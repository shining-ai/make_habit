package com.example.makehabit.ui

import com.example.makehabit.data.HabitRecord

fun timeRangeToIndices(start: String, end: String): Set<Int> {
    val startIndex = timeToBlockIndex(start)
    val endIndex = timeToBlockIndex(end)
    return (startIndex until endIndex).toSet()
}


fun timeToBlockIndex(time: String): Int {
    val parts = time.split(":")
    val hour = parts[0].toInt()
    val minute = parts[1].toInt()
    return hour * 6 + (minute / 10)
}
fun getCommonTimeBlocks(records: List<HabitRecord>): Set<Int> {
    return records
        .map { timeRangeToIndices(it.startTime, it.endTime) }
        .reduce { acc, set -> acc.intersect(set) }
}

fun indicesToTimeRanges(indices: Set<Int>): List<Pair<String, String>> {
    if (indices.isEmpty()) return emptyList()

    val sorted = indices.sorted()
    val ranges = mutableListOf<Pair<String, String>>()

    var start = sorted.first()
    var prev = start

    for (i in 1 until sorted.size) {
        if (sorted[i] != prev + 1) {
            ranges.add(blockIndexToTime(start) to blockIndexToTime(prev + 1))
            start = sorted[i]
        }
        prev = sorted[i]
    }
    ranges.add(blockIndexToTime(start) to blockIndexToTime(prev + 1))
    return ranges
}

fun blockIndexToTime(index: Int): String {
    val hour = index / 6
    val minute = (index % 6) * 10
    return "%02d:%02d".format(hour, minute)
}

fun getCommonTimeForTask(records: List<HabitRecord>): List<HabitRecord> {
    val indices = getCommonTimeBlocks(records)
    val timeRanges = indicesToTimeRanges(indices)

    return timeRanges.map { (start, end) ->
        HabitRecord(
            date = "共通日付", // 適切な日付を設定
            startTime = start,
            endTime = end,
            taskName = records.first().taskName // 同じタスク名を使う
        )
    }
}

fun getCommonTimeForTasks(records: List<HabitRecord>): List<HabitRecord> {
    // タスク名でグループ化
    val groupedByTask = records.groupBy { it.taskName }

    // 各タスクに対して共通の時間を計算
    return groupedByTask.flatMap { (_, taskRecords) ->
        getCommonTimeForTask(taskRecords)
    }
}