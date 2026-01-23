package com.yintianyan.tallybook.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.yintianyan.tallybook.theme.PrimaryBlue
import com.yintianyan.tallybook.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableFloatingActionButton(
    onClick: () -> Unit,
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
    
    // 状态管理
    var isInitialized by remember { mutableStateOf(false) }
    var isDocked by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    // 记录停靠在哪一侧 (true: Left, false: Right)
    var isDockedLeft by remember { mutableStateOf(false) }

    // 自动吸附逻辑
    LaunchedEffect(isDragging, isDocked) {
        if (!isDragging && !isDocked) {
            delay(3000) // 3秒无操作自动吸附
            isDocked = true
        }
    }

    // 监听停靠状态变化，执行动画
    LaunchedEffect(isDocked, parentSize, buttonSize) {
        if (isInitialized && !isDragging) {
            val parentWidth = parentSize.width.toFloat()
            val buttonWidth = buttonSize.width.toFloat()
            val padding = with(density) { 16.dp.toPx() }
            val visibleWidth = with(density) { 12.dp.toPx() } // 吸附时露出的宽度

            val targetX = if (isDockedLeft) {
                if (isDocked) -buttonWidth + visibleWidth else padding
            } else {
                if (isDocked) parentWidth - visibleWidth else parentWidth - buttonWidth - padding
            }

            offsetX.animateTo(
                targetValue = targetX,
                animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow)
            )
        }
    }

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
            onClick = {
                if (isDocked) {
                    isDocked = false
                } else {
                    onClick()
                }
            },
            text = text,
            containerColor = PrimaryBlue,
            contentColor = White,
            modifier = modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            isDocked = false
                        },
                        onDragEnd = {
                            isDragging = false
                            // 计算吸附方向
                            val parentWidth = parentSize.width.toFloat()
                            val buttonWidth = buttonSize.width.toFloat()
                            val currentX = offsetX.value
                            
                            isDockedLeft = currentX + buttonWidth / 2 < parentWidth / 2
                            
                            // 拖拽结束时，先吸附到展开位置
                            val padding = with(density) { 16.dp.toPx() }
                            val targetX = if (isDockedLeft) {
                                padding
                            } else {
                                parentWidth - buttonWidth - padding
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
