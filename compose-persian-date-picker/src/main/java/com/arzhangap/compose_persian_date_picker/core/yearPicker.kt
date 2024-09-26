package com.arzhangap.compose_persian_date_picker.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arzhangap.compose_persian_date_picker.R
import com.arzhangap.compose_persian_date_picker.jalaicalender.JalaliCalendar
import com.arzhangap.compose_persian_date_picker.util.formatNumberToPersian

@Composable
fun YearPicker(
    modifier: Modifier = Modifier,
    date: JalaliCalendar,
    textColor: Color,
    textColorHighlight: Color = MaterialTheme.colorScheme.primary,
    onYearClicked: (Int) -> Unit
) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = date.year - 1)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.pick_the_year),
            color = textColor,
            fontSize = 20.sp
        )
    }
    HorizontalDivider()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState,
        modifier = Modifier.height(165.dp)
    ) {
        items(1500) { index ->
            HorizontalDivider()
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable {
                    if (0 < index) {
                        onYearClicked(index)
                    }
                }
                .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (0 < index) {
                    Text(
                        text = index.formatNumberToPersian(),
                        fontSize = 30.sp,
                        color = if(date.year != index) textColor else textColorHighlight,
                    )
                } else {
                    // easter egg
                    Text(
                        text = "به جای اینکارا کتاب بخون :)",
                        fontSize = 18.sp,
                        color = textColor,
                    )
                }
            }
        }
    }
}
