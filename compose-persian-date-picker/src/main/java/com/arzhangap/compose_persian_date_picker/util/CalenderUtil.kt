package com.arzhangap.compose_persian_date_picker.util

import com.arzhangap.compose_persian_date_picker.jalaicalender.JalaliCalendar
import com.arzhangap.compose_persian_date_picker.model.PersianDate

fun numberOfMonthsInRange(yearRange: IntRange) =
    (yearRange.last - yearRange.first) * 12

// convert current date to MonthOffset
fun dateToMonthOffset(yearRange: IntRange, currentDate: JalaliCalendar): Int {
    return (currentDate.year - yearRange.first) * 12 + currentDate.month
}

// returns date from monthOffset
fun dateToMonthOffset(range: IntRange, monthOffset: Int): JalaliCalendar {
    val newYear = range.first + ((monthOffset - 1) / 12)
    val newMonth = ((monthOffset - 1) % 12) + 1
    return JalaliCalendar(newYear, newMonth, 1)
}

fun JalaliCalendar.toPersianDate() : PersianDate {
    return PersianDate(year, month, day)
}
fun PersianDate.toJalali() : JalaliCalendar {
    return JalaliCalendar(year, month, day)
}

fun Int.formatNumberToPersian(): String {
    val arabicDigits = arrayOf('٠', '١', '٢', '٣', '۴', '۵', '۶', '٧', '٨', '٩')
    val stringBuilder = StringBuilder()

    for (char in this.toString()) {
        if (char.isDigit()) {
            stringBuilder.append(arabicDigits[char.toString().toInt()])
        } else {
            stringBuilder.append(char)
        }
    }
    return stringBuilder.toString()
}

