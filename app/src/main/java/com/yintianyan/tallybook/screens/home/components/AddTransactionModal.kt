package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.yintianyan.tallybook.components.BottomSheetPopup

/**
 * 添加交易的底部弹窗组件
 * 
 * @param show 是否显示弹窗
 * @param onDismiss 关闭弹窗的回调
 * @param onTransactionAdded 交易添加成功后的回调
 */
@Composable
fun AddTransactionModal(
    show: Boolean,
    onDismiss: () -> Unit,
    onTransactionAdded: () -> Unit
) {
    BottomSheetPopup(
        show = show,
        onDismiss = onDismiss,
        disableDragDismiss = true,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        )
    ) {
        AddTransactionScreen(
            onDismiss = onDismiss,
            onTransactionAdded = onTransactionAdded
        )
    }
}