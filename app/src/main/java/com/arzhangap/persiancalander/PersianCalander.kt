package com.arzhangap.persiancalander

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ir.huri.jcal.JalaliCalendar
import kotlinx.coroutines.launch

@Composable
fun PersianDatePicker(
    selectedDate: PersianDate,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    onDayClicked: (PersianDate) -> Unit,
    chosenDate: PersianDate?
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { onDismissRequest() }) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth(),
                shape = RoundedCornerShape(20.dp),
            ) {
                PersianDatePickerContent(
                    onDayClicked = onDayClicked,
                    selectedDate = selectedDate,
                    onDismissRequest = onDismissRequest,
                    onConfirmation = onConfirmation,
                    chosenDate = chosenDate
                )
            }
        }
    }
}

@Composable
fun PersianDatePickerContent(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onDayClicked: (PersianDate) -> Unit,
    selectedDate: PersianDate,
    chosenDate: PersianDate?
) {
    // currently shown date
    var currentDate by remember {
        if(chosenDate== null) {
            mutableStateOf(JalaliCalendar())
        } else {
            mutableStateOf(chosenDate.toJalali())
        }

    }
    // current pickerType
    var pickerType by remember { mutableStateOf(PickerType.DAY) }
    val today = JalaliCalendar()
    var range by remember { mutableStateOf(IntRange(today.year-20,today.year+20)) }

    Column( verticalArrangement = Arrangement.Center) {
        DateHeader(
            headerDate = currentDate,
            onMonthClicked = {
                pickerType =
                    if (pickerType != PickerType.MONTH) PickerType.MONTH else PickerType.DAY
            },
            onYearClicked = {
                pickerType = if (pickerType != PickerType.YEAR) PickerType.YEAR else PickerType.DAY
            }
        )
        HorizontalDivider()
        when (pickerType) {
            PickerType.DAY -> {
                MonthDaysSlider(
                    currentDate = currentDate,
                    onDayClicked = onDayClicked,
                    selectedDate = selectedDate,
                    range = range,
                    onScroll = {
                        currentDate = it
                    },
                    onDismissRequest = onDismissRequest,
                    onConfirmation = onConfirmation,
                    onGoToToday = {
                        currentDate = today
                        onDayClicked(today.toPersianDate())
                                  }
                )
            }

            PickerType.MONTH -> {
                MonthPicker(
                    onMonthClicked = {
                        currentDate = JalaliCalendar(currentDate.year, it, 1)
                        pickerType = PickerType.DAY
                    }
                )
            }

            PickerType.YEAR -> {
                YearPicker(date = currentDate) {
                    currentDate = JalaliCalendar(it, currentDate.month, 1)
                    range = IntRange(currentDate.year - 20, currentDate.year + 20)
                    pickerType = PickerType.DAY
                }
            }
        }
    }
}

@Composable
fun YearPicker(modifier: Modifier = Modifier, date: JalaliCalendar, onYearClicked: (Int) -> Unit) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = date.year - 2)

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "انتخاب سال",
            fontSize = 20.sp,
        )
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState,
        modifier = Modifier.height(200.dp)
    ) {
        items(3000) { index ->
            HorizontalDivider()
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onYearClicked(index)
                }
                .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.formatNumberToPersian(),
                    fontSize = 30.sp,
                )
            }
        }
    }
}

@Composable
fun MonthPicker(modifier: Modifier = Modifier, onMonthClicked: (Int) -> Unit) {
    val monthsName = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند",
    )
    val itemsPerRow = 3
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        monthsName.chunked(itemsPerRow).forEachIndexed { rowIndex, group ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                group.forEachIndexed {index, monthName ->
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = {
                        onMonthClicked((rowIndex*itemsPerRow)+index+1)
                    }
                    ) {
                        Text(
                            text = monthName,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthDaysSlider(
    currentDate: JalaliCalendar,
    onDayClicked: (PersianDate) -> Unit,
    selectedDate: PersianDate,
    onScroll: (JalaliCalendar) -> Unit,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    onGoToToday: () -> Unit,
    range: IntRange,
) {
    val scope = rememberCoroutineScope()
    val currentPage =
        dateToMonthOffset(currentDate = currentDate, yearRange = range)
    //pagerState
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = {numberOfMonthsInRange(range)},
    )
    LaunchedEffect(key1 = pagerState.currentPage) {
        onScroll(dateToMonthOffset(range = range,monthOffset = pagerState.currentPage))
    }
    Column {
    DayNameRow(Modifier.padding(vertical = 5.dp))
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        HorizontalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapAnimationSpec = tween(durationMillis = 200),
            ),
        ) { monthOffset ->
            val pageDate =
                dateToMonthOffset(range = range, monthOffset = monthOffset)
            DaysGrid(date = pageDate, onDayClicked = onDayClicked, selectedDate = selectedDate)
        }
    }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = { onConfirmation() }
                ) {
                    Text(text = "تایید", fontSize = 16.sp)
                }
            }
            TextButton(onClick = {
                onGoToToday()
                scope.launch {
                    pagerState.scrollToPage(dateToMonthOffset(yearRange = range,currentDate = JalaliCalendar()))
                }
            }) {
                Text(text = "امروز", fontSize = 16.sp)
            }
            TextButton(onClick = onDismissRequest) {
                Text(text = "انصراف", fontSize = 16.sp)
            }

        }
    }
}

@Composable
fun DaysGrid(
    date: JalaliCalendar,
    modifier: Modifier = Modifier,
    onDayClicked: (PersianDate) -> Unit,
    selectedDate: PersianDate
) {

    val daysList = remember(date) {
        mutableListOf<Int?>().apply {
            // Add empty spaces for days before the first of the month
            repeat(date.dayOfWeek - 1) { add(null) }
            // Add actual days of the month
            for (day in 1..date.monthLength) add(day)
            // Add empty for days after the end of the month
            val endDayOffset = (7 - size % 7) % 7
            repeat(endDayOffset) { add(null) }
        }
    }

    Column(Modifier.height(280.dp)) {
        daysList.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { day ->
                    Box(Modifier.size(45.dp), contentAlignment = Alignment.Center) {
                    FilledIconButton(
                        onClick = {
                            day?.let {
                                onDayClicked(
                                    PersianDate(
                                        date.year,
                                        date.month,
                                        day.toInt()
                                    )
                                )
                            }
                        },
                        modifier = if (day == null) {
                            Modifier
                                .alpha(0f)
                        } else {
                            Modifier
                        }.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (day != null && selectedDate.year == date.year &&
                                selectedDate.month == date.month && selectedDate.day == day
                            ) {
                                MaterialTheme.colorScheme.primary // Highlight selected date
                            } else {
                                Color.Transparent
                            }
                        )
                    ) {
                        Text(
                            text = day?.formatNumberToPersian() ?: "",
                            fontSize = 18.sp,
                            color = if (day != null && selectedDate.year == date.year &&
                                selectedDate.month == date.month && selectedDate.day == day
                            ) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                Color.Unspecified
                            }
                        )
                    }
                }
                }
            }
        }
    }
}

@Composable
fun DayNameRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        val dayNameList = listOf("ج","پ","چ","س","د","ی","ش")
        dayNameList.forEach {
            Box(modifier = Modifier.width(45.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
        }
    }

@Composable
fun DateHeader(
    modifier: Modifier = Modifier,
    headerDate: JalaliCalendar,
    onMonthClicked: () -> Unit,
    onYearClicked: () -> Unit
) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onYearClicked, modifier = Modifier.weight(1f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        modifier = Modifier.align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "selection",
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp),
                        textAlign = TextAlign.Center,
                        text = headerDate.year.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
                TextButton(onClick = onMonthClicked,modifier = Modifier.weight(1f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            modifier = Modifier.align(Alignment.CenterStart),
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = "selection"
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = headerDate.monthString,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
}

internal fun numberOfMonthsInRange(yearRange: IntRange) =
    (yearRange.last - yearRange.first) * 12

// convert current date to MonthOffset
internal fun dateToMonthOffset(yearRange: IntRange, currentDate: JalaliCalendar): Int {
    return (currentDate.year - yearRange.first) * 12 + currentDate.month
}

// returns date from monthOffset
internal fun dateToMonthOffset(range: IntRange, monthOffset: Int): JalaliCalendar {
    val newYear = range.first + ((monthOffset - 1) / 12)
    val newMonth = ((monthOffset - 1) % 12) + 1
    return JalaliCalendar(newYear, newMonth, 1)
}


fun Int.formatNumberToPersian(): String {
    val arabicDigits = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
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

fun JalaliCalendar.toPersianDate() : PersianDate {
    return PersianDate(year, month, day)
}
fun PersianDate.toJalali() : JalaliCalendar {
    return JalaliCalendar(year, month, day)
}

enum class PickerType {
    DAY, MONTH, YEAR
}