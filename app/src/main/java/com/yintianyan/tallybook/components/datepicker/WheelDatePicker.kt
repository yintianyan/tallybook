package com.yintianyan.tallybook.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.Gray
import com.yintianyan.tallybook.theme.PrimaryBlue
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

/**
 * 日期选择器模式
 */
enum class DatePickerMode {
    /** 仅年份选择 */
    YEAR,
    /** 年月选择 */
    MONTH,
    /** 年月日选择 */
    DATE
}

/**
 * 滚轮日期选择器组件
 *
 * @param mode 选择模式 ('year' | 'month' | 'date')
 * @param defaultValue 默认选中日期
 * @param onChange 选择确认后回调
 * @param minYear 最小可选年份
 * @param maxYear 最大可选年份
 * @param visibleItemsCount 可见项目数量 (建议使用奇数，如 3, 5)
 * @param modifier 修饰符
 */
@Composable
fun WheelDatePicker(
    mode: DatePickerMode,
    defaultValue: LocalDate = LocalDate.now(),
    onChange: (LocalDate) -> Unit,
    minYear: Int = LocalDate.now().minusYears(30).year,
    maxYear: Int = LocalDate.now().year,
    visibleItemsCount: Int = 5,
    modifier: Modifier = Modifier
) {
    val currentOnChange by rememberUpdatedState(onChange)

    var selectedYear by remember { mutableStateOf(defaultValue.year) }
    var selectedMonth by remember { mutableStateOf(defaultValue.monthValue) }
    var selectedDay by remember { mutableStateOf(defaultValue.dayOfMonth) }

    // 当外部传入的 defaultValue 变化时，更新内部状态
    LaunchedEffect(defaultValue) {
        selectedYear = defaultValue.year
        selectedMonth = defaultValue.monthValue
        selectedDay = defaultValue.dayOfMonth
    }

    // 监听内部状态变化并回调
    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
        val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
        val actualDay = selectedDay.coerceAtMost(daysInMonth)

        // 如果日期需要被修正（例如从31号切到小月），则先更新内部状态
        // 修正后的状态会在下一次 recomposition 后通过这个 LaunchedEffect 触发回调
        if (selectedDay != actualDay) {
            selectedDay = actualDay
        } else {
            val newDate = LocalDate.of(selectedYear, selectedMonth, actualDay)
            currentOnChange(newDate)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 年份选择器
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderLabel("年")
            PickerColumn(
                items = (minYear..maxYear).toList(),
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it },
                visibleItemsCount = visibleItemsCount,
                itemFormatter = { "${it}年" },
                position = if (mode == DatePickerMode.YEAR) PickerColumnPosition.SINGLE else PickerColumnPosition.START
            )
        }

        // 月份选择器
        if (mode != DatePickerMode.YEAR) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderLabel("月")
                PickerColumn(
                    items = (1..12).toList(),
                    selectedItem = selectedMonth,
                    onItemSelected = { selectedMonth = it },
                    visibleItemsCount = visibleItemsCount,
                    itemFormatter = { "${it.toString().padStart(2, '0')}月" },
                    position = if (mode == DatePickerMode.MONTH) PickerColumnPosition.END else PickerColumnPosition.MIDDLE
                )
            }
        }

        // 日期选择器
        if (mode == DatePickerMode.DATE) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderLabel("日")
                val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
                PickerColumn(
                    items = (1..daysInMonth).toList(),
                    selectedItem = selectedDay.coerceAtMost(daysInMonth),
                    onItemSelected = { selectedDay = it },
                    visibleItemsCount = visibleItemsCount,
                    itemFormatter = { "${it.toString().padStart(2, '0')}日" },
                    position = PickerColumnPosition.END
                )
            }
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

enum class PickerColumnPosition {
    START, MIDDLE, END, SINGLE
}

/**
 * 通用滚轮选择器列组件
 */
@Composable
fun <T : Any> PickerColumn(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    visibleItemsCount: Int,
    itemFormatter: (T) -> String,
    modifier: Modifier = Modifier,
    itemHeightDp: Dp = 48.dp,
    position: PickerColumnPosition = PickerColumnPosition.MIDDLE
) {
    val coroutineScope = rememberCoroutineScope()
    
    // 计算圆角逻辑
    val cornerRadius = 8.dp
    val shape = remember(position) {
        when (position) {
            PickerColumnPosition.START -> RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius)
            PickerColumnPosition.END -> RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
            PickerColumnPosition.SINGLE -> RoundedCornerShape(cornerRadius)
            PickerColumnPosition.MIDDLE -> RoundedCornerShape(0.dp)
        }
    }
    
    // 计算初始索引
    val initialIndex = remember { 
        val index = items.indexOf(selectedItem)
        if (index >= 0) index else 0
    }
    
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // 计算上下填充，确保中间项居中
    val centralPadding = remember(visibleItemsCount, itemHeightDp) {
        (itemHeightDp * (visibleItemsCount - 1)) / 2
    }
    
    // 实时计算当前中间项的索引
    // 使用 derivedStateOf 避免频繁重组
    val centeredItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isNotEmpty()) {
                val viewportCenter = layoutInfo.viewportSize.height / 2 + layoutInfo.viewportStartOffset
                val closestItem = visibleItemsInfo.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - viewportCenter)
                }
                closestItem?.index ?: -1
            } else {
                -1
            }
        }
    }

    // 核心逻辑：滚动停止后，确认选择
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it } // 仅在滚动停止时触发
            .collect {
                if (centeredItemIndex != -1) {
                    val centerItem = items.getOrNull(centeredItemIndex)
                    if (centerItem != null && centerItem != selectedItem) {
                        onItemSelected(centerItem)
                    }
                }
            }
    }

    // 当 selectedItem 从外部被改变时（例如点击），滚动到指定位置
    LaunchedEffect(selectedItem) {
        val index = items.indexOf(selectedItem)
        if (index >= 0) {
            // 如果目标位置不在当前视口中心，或者没有正在滚动，则触发滚动
            if (index != centeredItemIndex && !listState.isScrollInProgress) {
                listState.animateScrollToItem(index)
            }
        }
    }

    // 计算圆角形状
    // 只有当组件位于最左侧或最右侧时才显示圆角
    // 之前定义的 shape 变量已经在上面计算好了

    Box(
        modifier = modifier
            .height(itemHeightDp * visibleItemsCount)
            .fillMaxWidth()
    ) {
        // 中间高亮条 (背景)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeightDp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = shape // 使用计算好的圆角形状
                )
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = centralPadding),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item ->
                // 使用实时计算的 centeredItemIndex 来判断高亮，而不是 selectedItem
                // 这样滚动过程中中间项会实时高亮
                val isCentered = index == centeredItemIndex

                Box(
                    modifier = Modifier
                        .height(itemHeightDp)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            // 点击时，触发滚动到该项
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemFormatter(item),
                        fontSize = if (isCentered) 20.sp else 16.sp, // 选中项稍微放大
                        textAlign = TextAlign.Center,
                        fontWeight = if (isCentered) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCentered) {
                            PrimaryBlue
                        } else {
                            DarkGray.copy(alpha = 0.6f)
                        }
                    )
                }
            }
        }
    }
}
