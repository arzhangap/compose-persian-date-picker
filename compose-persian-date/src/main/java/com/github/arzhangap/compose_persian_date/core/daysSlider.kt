package com.github.arzhangap.compose_persian_date.core

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.arzhangap.compose_persian_date.R
import com.github.arzhangap.compose_persian_date.jalaicalender.JalaliCalendar
import com.github.arzhangap.compose_persian_date.model.PersianDate
import com.github.arzhangap.compose_persian_date.util.dateToMonthOffset
import com.github.arzhangap.compose_persian_date.util.formatNumberToPersian
import com.github.arzhangap.compose_persian_date.util.numberOfMonthsInRange
import com.github.arzhangap.compose_persian_date.util.state.PersianDatePickerState
import com.github.arzhangap.compose_persian_date.util.toJalali
import com.github.arzhangap.compose_persian_date.util.toPersianDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthDaysSlider(
    persianDatePickerState: PersianDatePickerState,
    onScroll: (JalaliCalendar) -> Unit,
    onConfirmation: (PersianDate) -> Unit,
    onDismissRequest: () -> Unit,
    onGoToToday: () -> Unit,
    range: IntRange,
    textColor: Color,
    dayIconColor: Color,
    selectedIconColor: Color,
    textColorHighlight: Color,
    actionBtnTextColor: Color,
    dayOfWeekLabelColor: Color,
    fontFamily: FontFamily,
    actionBtnFontSize: TextUnit
) {
    // by Default selected day is today
    var selectedDay by remember { mutableStateOf(persianDatePickerState.selectedDate.toJalali()) }
    val scope = rememberCoroutineScope()
    val currentPage =
        dateToMonthOffset(currentDate = persianDatePickerState.currentDate, yearRange = range)
    //pagerState
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { numberOfMonthsInRange(range) },
    )

    // Update Date on Scroll
    LaunchedEffect(key1 = pagerState.currentPage) {
        onScroll(dateToMonthOffset(range = range, monthOffset = pagerState.currentPage))
    }

    Column {
        DayNameRow(Modifier.padding(vertical = 5.dp), dayOfWeekLabelColor = dayOfWeekLabelColor)
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
                DaysGrid(
                    date = pageDate,
                    onDayClicked = { selectedDay = it.toJalali() },
                    selectedDate = selectedDay.toPersianDate(),
                    textColor = textColor,
                    textColorHighlight = textColorHighlight,
                    dayIconColor = dayIconColor,
                    selectedDayIconColor = selectedIconColor,
                    fontFamily = fontFamily,
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = {
                        onConfirmation(selectedDay.toPersianDate())
                        persianDatePickerState.updateSelectedDate(selectedDay.toPersianDate())
                    }
                ) {
                    Text(text = stringResource(R.string.confirm), fontSize = actionBtnFontSize, color = actionBtnTextColor)
                }
            }
            TextButton(onClick = {
                onGoToToday()
                selectedDay = JalaliCalendar()
                scope.launch {
                    pagerState.scrollToPage(
                        dateToMonthOffset(
                            yearRange = range,
                            currentDate = JalaliCalendar()
                        )
                    )
                }
            }) {
                Text(text = stringResource(R.string.today), fontSize = actionBtnFontSize, color = actionBtnTextColor)
            }
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel), fontSize = actionBtnFontSize, color = actionBtnTextColor)
            }

        }
    }
}

@Composable
fun DaysGrid(
    date: JalaliCalendar,
    modifier: Modifier = Modifier,
    onDayClicked: (PersianDate) -> Unit,
    selectedDate: PersianDate,
    textColor: Color,
    dayIconColor: Color,
    selectedDayIconColor: Color,
    textColorHighlight: Color,
    fontFamily: FontFamily
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

    Column(modifier.height(280.dp)) {
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
                                    selectedDayIconColor
                                } else {
                                    dayIconColor
                                }
                            )
                        ) {
                            Text(
                                text = day?.formatNumberToPersian() ?: "",
                                fontSize = 18.sp,
                                color = if (day != null && selectedDate.year == date.year &&
                                    selectedDate.month == date.month && selectedDate.day == day
                                ) {
                                    textColorHighlight
                                } else {
                                    textColor
                                },
                                fontFamily = fontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayNameRow(modifier: Modifier = Modifier, dayOfWeekLabelColor: Color) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        val dayNameList = listOf("ج", "پ", "چ", "س", "د", "ی", "ش")
        dayNameList.forEach {
            Box(modifier = Modifier.width(45.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = dayOfWeekLabelColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
    }
}