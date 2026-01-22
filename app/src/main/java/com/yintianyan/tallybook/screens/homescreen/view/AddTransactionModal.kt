package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * 添加交易的底部弹窗组件
 * 
 * @param show 是否显示弹窗
 * @param onDismiss 关闭弹窗的回调
 * @param onTransactionAdded 交易添加成功后的回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionModal(
    show: Boolean,
    onDismiss: () -> Unit,
    onTransactionAdded: () -> Unit
) {
    if (show) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            // 禁用拖拽手柄，从而禁用拖拽关闭功能
            dragHandle = null
        ) {
            AddTransactionScreen(
                onDismiss = onDismiss,
                onTransactionAdded = onTransactionAdded
            )
        }
    }
}