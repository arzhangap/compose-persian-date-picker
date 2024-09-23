package com.github.arzhangap.compose_persian_date.util.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.arzhangap.compose_persian_date.jalaicalender.JalaliCalendar
import com.github.arzhangap.compose_persian_date.model.PersianDate
import com.github.arzhangap.compose_persian_date.util.toJalali
import com.github.arzhangap.compose_persian_date.util.toPersianDate

class PersianDatePickerState internal constructor() {
    // currently selected Date
    var selectedDate by mutableStateOf(JalaliCalendar().toPersianDate())
        private set
    // currently showingDate
    var currentDate by mutableStateOf(selectedDate.toJalali())
        private set
    // final chosen date
    var chosenDate: PersianDate? by mutableStateOf(null)

    var isDialogOpen by mutableStateOf(false)
        private set

    fun toggleDialog() {
        isDialogOpen = !isDialogOpen
    }

    fun updateCurrentDate(date: JalaliCalendar) {
        currentDate = date
    }
    fun updateSelectedDate(date: PersianDate) {
        selectedDate = date
    }

    fun updateCurrentDateYear(year: Int) {
        currentDate = JalaliCalendar(year, currentDate.month,1)
    }
    fun updateCurrentDateMonth(month: Int) {
        currentDate = JalaliCalendar(currentDate.year,month,1)
    }
    fun setDateToToday() {
        currentDate = JalaliCalendar()
    }
    fun clearDate() {
        if (chosenDate!=null) {
            currentDate = JalaliCalendar()
            selectedDate = currentDate.toPersianDate()
            chosenDate = null
        }
    }
}

fun PersianDate?.string(
    nullMessage: String = "تاریخی انتخاب نشده است."
) : String {
    return if(this != null)"$year/$month/$day"
    else nullMessage
}

@Composable
fun rememberPersianDatePickerState(key1: Any? = Unit) : PersianDatePickerState {
    return remember(key1) {
        PersianDatePickerState()
    }
}