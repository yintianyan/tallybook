package com.yintianyan.tallybook.components.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.BottomSheetPopup
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.PrimaryBlue
import java.time.LocalTime

@Composable
fun TimePickerModal(
    show: Boolean,
    initialTime: LocalTime = LocalTime.now(),
    title: String = "选择时间",
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    var selectedTime by remember(initialTime) { mutableStateOf(initialTime) }

    BottomSheetPopup(
        show = show,
        onDismiss = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
            )

            WheelTimePicker(
                initialTime = initialTime,
                onTimeSelected = { selectedTime = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onTimeSelected(selectedTime)
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "确定", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
