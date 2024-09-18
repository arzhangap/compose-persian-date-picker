package com.arzhangap.persiancalander

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ir.huri.jcal.JalaliCalendar

class PersianDatePickerState internal constructor() {
    // currently selected Date
    var selectedDate by mutableStateOf(JalaliCalendar().toPersianDate())
    // currently showingDate
    var currentDate by mutableStateOf(selectedDate.toJalali())

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
}

@Composable
fun rememberPersianDatePickerState(key1: Any? = Unit) : PersianDatePickerState {
    return remember(key1) {
        PersianDatePickerState()
    }
}