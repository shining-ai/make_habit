package com.example.makehabit.ui.habitrecord

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.makehabit.data.HabitRecord
import com.example.makehabit.data.HabitRecordsRepository


/**
 * ViewModel to validate and insert habitRecords in the Room database.
 */
class HabitRecordEntryViewModel(private val habitRecordsRepository: HabitRecordsRepository) : ViewModel() {

    /**
     * Holds current habitRecord ui state
     */
    var habitRecordUiState by mutableStateOf(HabitRecordUiState())
        private set

    /**
     * Updates the [habitRecordUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(habitRecordDetails: HabitRecordDetails) {
        habitRecordUiState =
            HabitRecordUiState(habitRecordDetails = habitRecordDetails, isEntryValid = validateInput(habitRecordDetails))
    }

    suspend fun saveHabitRecord() {
        if (validateInput()) {
            habitRecordsRepository.insertHabitRecord(habitRecordUiState.habitRecordDetails.toHabitRecord())
        }
    }

    private fun validateInput(uiState: HabitRecordDetails = habitRecordUiState.habitRecordDetails): Boolean {
        return with(uiState) {
            date.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank() && taskName.isBlank()
        }
    }
}

/**
 * Represents Ui State for an HabitRecord.
 */
data class HabitRecordUiState(
    val habitRecordDetails: HabitRecordDetails = HabitRecordDetails(),
    val isEntryValid: Boolean = false
)


data class HabitRecordDetails(
    val id: Int = 0,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val taskName: String = ""
)


fun HabitRecordDetails.toHabitRecord(): HabitRecord = HabitRecord(
//    id = id,
    date = date,
    startTime = startTime,
    endTime = endTime,
    taskName = taskName
)

//fun HabitRecord.formatedPrice(): String {
//    return NumberFormat.getCurrencyInstance().format(price)
//}


fun HabitRecord.toHabitRecordUiState(isEntryValid: Boolean = false): HabitRecordUiState = HabitRecordUiState(
    habitRecordDetails = this.toHabitRecordDetails(),
    isEntryValid = isEntryValid
)

fun HabitRecord.toHabitRecordDetails(): HabitRecordDetails = HabitRecordDetails(
//    id = id,
    date = date,
    startTime = startTime,
    endTime = endTime,
    taskName = taskName
)