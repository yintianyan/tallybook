package com.yintianyan.tallybook.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yintianyan.tallybook.theme.PrimaryBlue
import com.yintianyan.tallybook.theme.White
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableFloatingActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    
    // 按钮的偏移量状态
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    
    // 父容器尺寸（用于计算边界）
    var parentSize by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }
    // 按钮尺寸
    var buttonSize by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }
    
    // 初始位置标记
    var isInitialized by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                if (coordinates.parentCoordinates != null) {
                    parentSize = coordinates.parentCoordinates!!.size
                    buttonSize = coordinates.size
                    
                    // 初始化位置到右下角
                    if (!isInitialized && parentSize.width > 0 && parentSize.height > 0) {
                        val initialX = parentSize.width - buttonSize.width - with(density) { 24.dp.toPx() }
                        val initialY = parentSize.height - buttonSize.height - with(density) { 24.dp.toPx() }
                        
                        coroutineScope.launch {
                            offsetX.snapTo(initialX)
                            offsetY.snapTo(initialY)
                            isInitialized = true
                        }
                    }
                }
            }
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = icon,
            text = text,
            containerColor = PrimaryBlue,
            contentColor = White,
            modifier = modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // 吸附逻辑：吸附到最近的左右边缘
                            val parentWidth = parentSize.width.toFloat()
                            val buttonWidth = buttonSize.width.toFloat()
                            val currentX = offsetX.value
                            
                            // 左右边界 padding
                            val padding = with(density) { 16.dp.toPx() }
                            
                            // 目标 X 坐标
                            val targetX = if (currentX + buttonWidth / 2 < parentWidth / 2) {
                                padding // 吸附到左边
                            } else {
                                parentWidth - buttonWidth - padding // 吸附到右边
                            }
                            
                            // 限制 Y 轴范围
                            val parentHeight = parentSize.height.toFloat()
                            val buttonHeight = buttonSize.height.toFloat()
                            val targetY = offsetY.value.coerceIn(padding, parentHeight - buttonHeight - padding)

                            coroutineScope.launch {
                                offsetX.animateTo(
                                    targetValue = targetX,
                                    animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow)
                                )
                                offsetY.animateTo(
                                    targetValue = targetY,
                                    animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow)
                                )
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                }
        )
    }
}
