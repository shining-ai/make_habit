package com.example.makehabit.data

import kotlinx.coroutines.flow.Flow

class OfflineHabitRecordsRepository(private val habit_record_Dao: HabitRecordDao) : HabitRecordsRepository {
    override fun getAllRecordsStream(): Flow<List<HabitRecord>> = habit_record_Dao.getAllRecords()
    override fun getHabitRecordStream(id: Int): Flow<HabitRecord?> = habit_record_Dao.getHabitRecord(id)
    override suspend fun insertHabitRecord(habit_record: HabitRecord) = habit_record_Dao.insert(habit_record)
    override suspend fun updateHabitRecord(habit_record: HabitRecord) = habit_record_Dao.update(habit_record)
    override suspend fun deleteHabitRecord(habit_record: HabitRecord) = habit_record_Dao.delete(habit_record)
}