package com.example.makehabit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "habit_records")
data class HabitRecord(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String,
    val taskName: String
)