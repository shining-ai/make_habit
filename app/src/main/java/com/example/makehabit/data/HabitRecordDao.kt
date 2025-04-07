package com.example.makehabit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Item → HabitRecord
@Dao
interface HabitRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit_record: HabitRecord)

    @Update
    suspend fun update(habit_record: HabitRecord)

    @Delete
    suspend fun delete(habit_record: HabitRecord)

    @Query("SELECT * from habit_records WHERE id = :id")
    fun getHabitRecord(id: Int): Flow<HabitRecord>

    @Query("SELECT * FROM habit_records ORDER BY date DESC, startTime DESC")
    fun getAllRecords(): Flow<List<HabitRecord>>
}