package com.yintianyan.tallybook.components.datepicker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.BottomSheetPopup
import com.yintianyan.tallybook.theme.Gray
import com.yintianyan.tallybook.theme.PrimaryBlue
import com.yintianyan.tallybook.theme.White
import java.time.LocalDate

/**
 * 日期选择器模态框
 *
 * @param show 是否显示模态框
 * @param mode 选择器模式
 * @param selectedDate 选中的日期
 * @param onDateSelected 日期选择回调 (year, month, day)
 * @param onDismiss 关闭模态框回调
 * @param title 标题
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    show: Boolean,
    mode: DatePickerMode,
    selectedDate: LocalDate,
    onDateSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit,
    title: String = "选择日期"
) {
    val titleComposable = @Composable {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Gray
        )
    }

    // 本地状态，用于跟踪用户选择的日期
    var localSelectedDate by remember(selectedDate) { mutableStateOf(selectedDate) }

    val dismissButton = @Composable {
        TextButton(onClick = onDismiss) {
            Text(
                text = "取消",
                fontSize = 14.sp,
                color = Gray
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
                containerColor = PrimaryBlue,
                contentColor = White
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
        title = titleComposable,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        disableDragDismiss = true
    ) {
        // 日期选择器
        WheelDatePicker(
            mode = mode,
            defaultValue = selectedDate,
            onChange = { date ->
                // 只更新本地状态，不触发关闭
                localSelectedDate = date
            },
            modifier = Modifier
                .fillMaxWidth()
                // 移除固定高度，允许 DatePicker 根据 visibleItemsCount 决定高度
                // 默认 DatePicker 高度 = 48.dp * 5 = 240.dp
        )
    }
}
