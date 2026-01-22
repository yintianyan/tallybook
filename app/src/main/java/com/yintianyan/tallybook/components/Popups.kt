package com.yintianyan.tallybook.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 弹出层类型
 */
enum class PopupType {
    /** 底部弹出层 */
    BOTTOM_SHEET,
    /** 居中对话框 */
    DIALOG
}

/**
 * 通用底部弹出层组件
 *
 * @param show 是否显示弹出层
 * @param onDismiss 关闭弹出层的回调
 * @param title 弹出层标题
 * @param confirmButton 确认按钮
 * @param dismissButton 取消按钮
 * @param shape 弹出层的形状
 * @param containerColor 容器颜色
 * @param disableDragDismiss 是否禁用拖拽关闭功能
 * @param content 弹出层内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPopup(
    show: Boolean,
    onDismiss: () -> Unit,
    title: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: androidx.compose.ui.graphics.Color = com.yintianyan.tallybook.theme.White,
    disableDragDismiss: Boolean = false,
    content: @Composable () -> Unit
) {
    if (show) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = shape,
            containerColor = containerColor,
            // 禁用拖拽手柄，从而禁用拖拽关闭功能
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 标题栏
                if (title != null || dismissButton != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        title?.invoke()
                        dismissButton?.invoke()
                    }
                }
                
                // 内容
                content()
                
                // 确认按钮
                if (confirmButton != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        confirmButton()
                    }
                }
            }
        }
    }
}

/**
 * 通用对话框组件
 *
 * @param show 是否显示对话框
 * @param onDismiss 关闭对话框的回调
 * @param title 对话框标题
 * @param text 对话框文本内容
 * @param confirmButton 确认按钮
 * @param dismissButton 取消按钮
 * @param shape 对话框的形状
 * @param containerColor 容器颜色
 * @param content 自定义内容（如果提供，将忽略 title 和 text）
 */
@Composable
fun DialogPopup(
    show: Boolean,
    onDismiss: () -> Unit,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: androidx.compose.ui.graphics.Color = com.yintianyan.tallybook.theme.White,
    content: (@Composable () -> Unit)? = null
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = title,
            text = content ?: text,
            confirmButton = confirmButton ?: {},
            dismissButton = dismissButton ?: {},
            shape = shape,
            containerColor = containerColor
        )
    }
}

/**
 * 通用弹出层组件，根据类型显示不同的弹出方式
 *
 * @param type 弹出层类型
 * @param show 是否显示弹出层
 * @param onDismiss 关闭弹出层的回调
 * @param title 对话框标题（仅用于 DIALOG 类型）
 * @param text 对话框文本内容（仅用于 DIALOG 类型）
 * @param confirmButton 确认按钮（仅用于 DIALOG 类型）
 * @param dismissButton 取消按钮（仅用于 DIALOG 类型）
 * @param shape 弹出层的形状
 * @param containerColor 容器颜色
 * @param content 弹出层内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericPopup(
    type: PopupType,
    show: Boolean,
    onDismiss: () -> Unit,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = if (type == PopupType.BOTTOM_SHEET) {
        MaterialTheme.shapes.large
    } else {
        MaterialTheme.shapes.medium
    },
    containerColor: androidx.compose.ui.graphics.Color = com.yintianyan.tallybook.theme.White,
    content: @Composable () -> Unit
) {
    when (type) {
        PopupType.BOTTOM_SHEET -> {
            BottomSheetPopup(
                show = show,
                onDismiss = onDismiss,
                title = title,
                confirmButton = confirmButton,
                dismissButton = dismissButton,
                shape = shape,
                containerColor = containerColor,
                content = content
            )
        }
        PopupType.DIALOG -> {
            DialogPopup(
                show = show,
                onDismiss = onDismiss,
                title = title,
                text = text,
                confirmButton = confirmButton,
                dismissButton = dismissButton,
                shape = shape,
                containerColor = containerColor,
                content = content
            )
        }
    }
}
