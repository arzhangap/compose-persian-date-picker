package com.arzhangap.persiancalander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arzhangap.persiancalander.ui.theme.PersianCalanderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersianCalanderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val calenderState = rememberPersianDatePickerState()
                    var openDialog by remember {
                        mutableStateOf(false)
                    }
                    var date: PersianDate? by remember { mutableStateOf(null) }

                    if (openDialog) {
                        PersianDatePicker(
                            persianDatePickerState = calenderState,
                            onDismissRequest = { openDialog = false },
                            onConfirmation = {
                                openDialog = false
                                date = it
                            }
                        )
                    }
                    Column(
                        Modifier.padding(innerPadding)
                    ) {
                        Button(onClick = { openDialog = !openDialog }) {
                            Text(text = "Open")
                        }
                        if (date != null) {
                            Text(text = "${date!!.year}/${date!!.month}/${date!!.day}")
                        }
                    }
                }

            }
        }
    }
}

data class PersianDate(
    val year: Int,
    val month: Int,
    val day: Int
)
