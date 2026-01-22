package com.yintianyan.tallybook.components.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@Composable
fun WheelTimePicker(
    initialTime: LocalTime = LocalTime.now(),
    onTimeSelected: (LocalTime) -> Unit,
    visibleItemsCount: Int = 5,
    modifier: Modifier = Modifier
) {
    val currentOnChange by rememberUpdatedState(onTimeSelected)
    
    var selectedHour by remember { mutableStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }
    var selectedSecond by remember { mutableStateOf(initialTime.second) }

    LaunchedEffect(initialTime) {
        selectedHour = initialTime.hour
        selectedMinute = initialTime.minute
        selectedSecond = initialTime.second
    }

    LaunchedEffect(selectedHour, selectedMinute, selectedSecond) {
        currentOnChange(LocalTime.of(selectedHour, selectedMinute, selectedSecond))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hour
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabel("时")
            PickerColumn(
                items = (0..23).toList(),
                selectedItem = selectedHour,
                onItemSelected = { selectedHour = it },
                visibleItemsCount = visibleItemsCount,
                itemFormatter = { String.format("%02d", it) },
                position = PickerColumnPosition.START
            )
        }

        // Minute
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabel("分")
            PickerColumn(
                items = (0..59).toList(),
                selectedItem = selectedMinute,
                onItemSelected = { selectedMinute = it },
                visibleItemsCount = visibleItemsCount,
                itemFormatter = { String.format("%02d", it) },
                position = PickerColumnPosition.MIDDLE
            )
        }

        // Second
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabel("秒")
            PickerColumn(
                items = (0..59).toList(),
                selectedItem = selectedSecond,
                onItemSelected = { selectedSecond = it },
                visibleItemsCount = visibleItemsCount,
                itemFormatter = { String.format("%02d", it) },
                position = PickerColumnPosition.END
            )
        }
    }
}

@Composable
private fun HeaderLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
