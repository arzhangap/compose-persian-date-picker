package com.github.arzhangap.compose_persian_date.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MonthPicker(
    modifier: Modifier = Modifier,
    itemsPerRow: Int = 3,
    onMonthClicked: (Int) -> Unit,
    textColor: Color
) {
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
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            monthsName.chunked(itemsPerRow).forEachIndexed { rowIndex, group ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // to make order right we use Reversed on each row
                    group.forEachIndexed { index, monthName ->
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = {
                                onMonthClicked((rowIndex * itemsPerRow) + index + 1)
                            }
                        ) {
                            Text(
                                text = monthName,
                                fontSize = 17.sp,
                                color = textColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
