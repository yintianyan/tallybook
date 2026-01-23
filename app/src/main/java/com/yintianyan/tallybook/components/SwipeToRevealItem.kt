package com.yintianyan.tallybook.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yintianyan.tallybook.theme.White
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 左滑显示删除按钮组件 (使用标准 draggable 实现)
 *
 * @param onDelete 点击删除按钮的回调
 * @param content 前景内容
 */
@Composable
fun SwipeToRevealItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val actionWidth = 80.dp
    val actionWidthPx = with(density) { actionWidth.toPx() }
    
    // 偏移量状态 (0f 到 -actionWidthPx)
    val offsetX = remember { Animatable(0f) }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // 确保高度匹配内容
    ) {
        // 删除按钮背景（右侧）
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(actionWidth)
                .fillMaxHeight()
                .background(Color.Red)
                .clickable { 
                    // 点击删除
                    onDelete()
                    // 点击后自动收起
                    scope.launch { offsetX.animateTo(0f) }
                },
            contentAlignment = Alignment.Center
        ) {
             Icon(
                 imageVector = Icons.Default.Delete,
                 contentDescription = "删除",
                 tint = White
             )
        }

        // 前景内容
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        // 应用 delta 并限制范围在 [-actionWidthPx, 0f]
                        // 这样自然实现了"不能向右滑动"(上限为0)和"最多滑动距离"(下限为-width)
                        scope.launch {
                            val newValue = (offsetX.value + delta).coerceIn(-actionWidthPx, 0f)
                            offsetX.snapTo(newValue)
                        }
                    },
                    onDragStopped = { velocity ->
                        // 简单的吸附逻辑
                        // 如果滑动超过一半，或者快速向左滑动，则展开；否则收起
                        val targetValue = if (offsetX.value < -actionWidthPx / 2 || velocity < -500f) {
                            -actionWidthPx
                        } else {
                            0f
                        }
                        
                        scope.launch {
                            offsetX.animateTo(
                                targetValue = targetValue,
                                animationSpec = tween()
                            )
                        }
                    }
                )
        ) {
            content()
        }
    }
}
