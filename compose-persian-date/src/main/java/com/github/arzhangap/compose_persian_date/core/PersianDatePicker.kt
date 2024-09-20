package com.github.arzhangap.compose_persian_date.core

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.arzhangap.compose_persian_date.R
import com.github.arzhangap.compose_persian_date.jalaicalender.JalaliCalendar
import com.github.arzhangap.compose_persian_date.model.PersianDate
import com.github.arzhangap.compose_persian_date.model.PickerType
import com.github.arzhangap.compose_persian_date.util.dateToMonthOffset
import com.github.arzhangap.compose_persian_date.util.formatNumberToPersian
import com.github.arzhangap.compose_persian_date.util.numberOfMonthsInRange
import com.github.arzhangap.compose_persian_date.util.state.PersianDatePickerState
import com.github.arzhangap.compose_persian_date.util.toJalali
import com.github.arzhangap.compose_persian_date.util.toPersianDate
import kotlinx.coroutines.launch

/**
 * Opens a Jalali DatePicker dialog with the given content.
 *
 * Example usage:
 *
 * @param persianDatePickerState holds all needed states of Dialog.
 * @param onConfirmation Called when confirm button is clicked and returns selected Date.
 * @param onDismissRequest Called when user closes Dialog.
 * @param dialogColor Background color of the dialog.
 * @param textColor Color of the text.
 * @param dayIconColor Color of day icon button.
 * @param selectedDayIconColor Color of selected day (month, year) circular icon.
 * @param textColorHighlight Color of current day (month, year) text.
 * @param actionBtnTextColor Color of action buttons text
 * @param dayOfWeekLabelColor Color for the day of the week label text.
 * @param fontFamily changing font for all the text.
 * @param actionBtnFontSize size of three action buttons.
 *
 */

@Composable
fun PersianDatePicker(
    persianDatePickerState: PersianDatePickerState,
    onConfirmation: (PersianDate) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    dialogColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    dayIconColor: Color = Color.Transparent,
    selectedDayIconColor: Color = MaterialTheme.colorScheme.primary,
    textColorHighlight: Color = Color.Unspecified,
    actionBtnTextColor: Color = MaterialTheme.colorScheme.primary,
    dayOfWeekLabelColor: Color = MaterialTheme.colorScheme.primary,
    fontFamily: FontFamily = FontFamily.Default,
    actionBtnFontSize: TextUnit = 16.sp
) {
    val dismissRequestAndClose = {
        onDismissRequest()
        persianDatePickerState.toggleDialog()
    }

    if (persianDatePickerState.isDialogOpen) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { onDismissRequest() }) {
            Dialog(onDismissRequest = dismissRequestAndClose) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = dialogColor
                ) {
                    PersianDatePickerContent(
                        persianDatePickerState = persianDatePickerState,
                        onDismissRequest = dismissRequestAndClose,
                        onConfirmation = {
                            persianDatePickerState.chosenDate = it
                            persianDatePickerState.toggleDialog()
                            onConfirmation(it)
                        },
                        textColor = textColor,
                        dayIconColor = dayIconColor,
                        textColorHighlight = textColorHighlight,
                        selectedDayIconColor = selectedDayIconColor,
                        actionBtnTextColor = actionBtnTextColor,
                        dayOfWeekLabelColor = dayOfWeekLabelColor,
                        fontFamily = fontFamily,
                        actionBtnFontSize = actionBtnFontSize
                    )
                }
            }
        }
    }
}

/**
 * Content of Date Picker Dialog
 *
 * @param persianDatePickerState holds all needed states of Dialog.
 * @param onConfirmation Called when confirm button is clicked and returns selected Date.
 * @param onDismissRequest Called when user closes Dialog.
 * @param textColor Color of the text.
 * @param dayIconColor Color of day icon button.
 * @param selectedDayIconColor Color of selected day (month, year) circular icon.
 * @param textColorHighlight Color of current day (month, year) text.
 * @param actionBtnTextColor Color of action buttons text
 * @param dayOfWeekLabelColor Color for the day of the week label text.
 * @param fontFamily changing font for all the text.
 * @param scrollableRange number of years to be scrollable
 * @param actionBtnFontSize size of three action buttons.
 *
 */
@Composable
fun PersianDatePickerContent(
    persianDatePickerState: PersianDatePickerState,
    onConfirmation: (PersianDate) -> Unit,
    onDismissRequest: () -> Unit,
    textColor: Color,
    dayIconColor: Color,
    selectedDayIconColor: Color,
    textColorHighlight: Color,
    actionBtnTextColor: Color,
    dayOfWeekLabelColor: Color,
    fontFamily: FontFamily,
    scrollableRange: Int = 5,
    actionBtnFontSize: TextUnit
) {
    // current pickerType
    var pickerType by remember { mutableStateOf(PickerType.DAY) }
    // Default scrollable Range
    var range by remember {
        mutableStateOf(
            IntRange(
                persianDatePickerState.currentDate.year - scrollableRange,
                persianDatePickerState.currentDate.year + scrollableRange
            )
        )
    }

    Column(verticalArrangement = Arrangement.Center) {
        DateHeader(
            headerDate = persianDatePickerState.currentDate,
            onMonthClicked = {
                pickerType =
                    if (pickerType != PickerType.MONTH) PickerType.MONTH else PickerType.DAY
            },
            onYearClicked = {
                pickerType = if (pickerType != PickerType.YEAR) PickerType.YEAR else PickerType.DAY
            },
            headerTextColor = actionBtnTextColor
        )
        HorizontalDivider()
        when (pickerType) {
            PickerType.DAY -> {
                MonthDaysSlider(
                    persianDatePickerState = persianDatePickerState,
                    range = range,
                    onScroll = {
                        persianDatePickerState.updateCurrentDate(it)
                    },
                    onDismissRequest = onDismissRequest,
                    onConfirmation = { onConfirmation(it) },
                    onGoToToday = {
                        persianDatePickerState.setDateToToday()
                    },
                    textColor = textColor,
                    textColorHighlight = textColorHighlight,
                    dayIconColor = dayIconColor,
                    selectedIconColor = selectedDayIconColor,
                    actionBtnTextColor = actionBtnTextColor,
                    dayOfWeekLabelColor = dayOfWeekLabelColor,
                    fontFamily = fontFamily,
                    actionBtnFontSize = actionBtnFontSize
                )
            }

            PickerType.MONTH -> {
                MonthPicker(
                    onMonthClicked = {
                        persianDatePickerState.updateCurrentDateMonth(it)
                        pickerType = PickerType.DAY
                    },
                    textColor = textColor
                )
            }

            PickerType.YEAR -> {
                YearPicker(
                    date = persianDatePickerState.currentDate,
                    textColor = textColor
                ) {
                    persianDatePickerState.updateCurrentDateYear(it)
                    range = IntRange(
                        persianDatePickerState.currentDate.year - scrollableRange,
                        persianDatePickerState.currentDate.year + scrollableRange
                    )
                    pickerType = PickerType.DAY
                }
            }
        }
    }
}

@Composable
fun DateHeader(
    modifier: Modifier = Modifier,
    headerDate: JalaliCalendar,
    onMonthClicked: () -> Unit,
    onYearClicked: () -> Unit,
    headerTextColor: Color,
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
                    text = headerDate.year.formatNumberToPersian(),
                    color = headerTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        TextButton(onClick = onMonthClicked, modifier = Modifier.weight(1f)) {
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
                    color = headerTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
