package com.example.makehabit.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface HabitRecordsRepository{
    fun getAllRecordsStream(): Flow<List<HabitRecord>>
    fun getHabitRecordStream(id: Int): Flow<HabitRecord?>
    suspend fun insertHabitRecord(habit_record: HabitRecord)
    suspend fun updateHabitRecord(habit_record: HabitRecord)
    suspend fun deleteHabitRecord(habit_record: HabitRecord)




//class HabitRecordsRepository(private val habitDao: HabitRecordDao) {
//
//    val allHabits: Flow<List<HabitRecordEntity>> = habitDao.getAllRecords()
//
//    suspend fun insert(habit: HabitRecordEntity) {
//        habitDao.insertRecord(habit)
//    }
//
//    suspend fun delete(habit: HabitRecordEntity) {
//        habitDao.deleteRecord(habit)
//    }

//    suspend fun update(habit: HabitRecordEntity) {
//        habitDao.update(habit)
//    }
}