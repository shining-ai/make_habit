package com.example.makehabit.data

import android.content.Context

interface AppContainer {
    val habit_records_Repository: HabitRecordsRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val habit_records_Repository: HabitRecordsRepository by lazy{
        OfflineHabitRecordsRepository(InventoryDatabase.getDatabase(context).habitRecordDao())
    }
}