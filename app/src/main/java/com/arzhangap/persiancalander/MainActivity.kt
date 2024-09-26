package com.arzhangap.persiancalander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.arzhangap.persiancalander.ui.theme.PersianCalanderTheme
import com.arzhangap.compose_persian_date_picker.core.PersianDatePicker
import com.arzhangap.compose_persian_date_picker.util.state.rememberPersianDatePickerState
import com.arzhangap.compose_persian_date_picker.util.state.string

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersianCalanderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // remember state for date picker
                    val calenderState = rememberPersianDatePickerState()

                    PersianDatePicker(persianDatePickerState = calenderState)


                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { calenderState.toggleDialog()}) {
                            Text(text = "Open")
                        }
                        Row(
                           modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(calenderState.chosenDate!=null){
                                // clear state
                                IconButton(onClick = { calenderState.clearDate() }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "clear")
                                }
                            }

                            // access the date user have chosen.
                            // if no date is selected it will be null.
                           Text(text = calenderState.chosenDate.string() , textAlign = TextAlign.Center)
                        }
                    }
                }

            }
        }
    }
}
