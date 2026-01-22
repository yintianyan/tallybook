package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.BottomSheetPopup
import com.yintianyan.tallybook.components.DatePicker
import com.yintianyan.tallybook.components.DatePickerMode
import java.time.LocalDate

/**
 * 日期选择器模态框
 *
 * @param show 是否显示模态框
 * @param mode 选择器模式
 * @param selectedDate 选中的日期
 * @param onDateSelected 日期选择回调
 * @param onDismiss 关闭模态框回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    show: Boolean,
    mode: DatePickerMode,
    selectedDate: LocalDate,
    onDateSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val title = @Composable {
        Text(
            text = "选择日期",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    // 本地状态，用于跟踪用户选择的日期
    var localSelectedDate by remember(selectedDate) { mutableStateOf(selectedDate) }

    val dismissButton = @Composable {
        TextButton(onClick = onDismiss) {
            Text(
                text = "取消",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    val confirmButton = @Composable {
        Button(
            onClick = {
                onDateSelected(localSelectedDate.year, localSelectedDate.monthValue, localSelectedDate.dayOfMonth)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "确认",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    BottomSheetPopup(
        show = show,
        onDismiss = onDismiss,
        title = title,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        disableDragDismiss = true
    ) {
        // 日期选择器
        DatePicker(
            mode = mode,
            defaultValue = selectedDate,
            onChange = { date ->
                // 只更新本地状态，不触发关闭
                localSelectedDate = date
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
