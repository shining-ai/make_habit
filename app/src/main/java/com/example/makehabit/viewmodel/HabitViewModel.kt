package com.example.makehabit.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.makehabit.data.HabitRecord

class HabitViewModel : ViewModel() {
//  本来のコード
//    var recordList = mutableStateListOf<HabitRecord>()
//        private set
//
//    fun addRecord(record: HabitRecord) {
//        recordList.add(record)
//    }

//    デバッグ用
    private val _recordList = mutableStateListOf<HabitRecord>()
    val recordList: List<HabitRecord> = _recordList

    init {
        // デバッグ用の初期データを3件追加
        val now = System.currentTimeMillis()
        _recordList.addAll(
            listOf(
                HabitRecord(date = "2025-04-01", startTime = "07:00", endTime = "08:00", taskName = "筋トレ"),
                HabitRecord("2025-04-01", "20:00", "24:00", "勉強"),
                HabitRecord("2025-04-02", "07:30", "08:30", "筋トレ")

            )
        )
    }

    fun addRecord(record: HabitRecord) {
        _recordList.add(record)
    }
}