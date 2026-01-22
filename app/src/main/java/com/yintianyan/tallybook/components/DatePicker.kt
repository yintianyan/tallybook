 package com.yintianyan.tallybook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.runtime.saveable.rememberSaveable

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
 * 日期选择器组件
 *
 * @param mode 选择模式 ('year' | 'month' | 'date')
 * @param defaultValue 默认选中日期
 * @param onChange 选择确认后回调
 * @param minYear 最小可选年份
 * @param maxYear 最大可选年份
 * @param visibleItemsCount 可见项目数量
 * @param modifier 修饰符
 */
@Composable
fun DatePicker(
    mode: DatePickerMode,
    defaultValue: LocalDate = LocalDate.now(),
    onChange: (LocalDate) -> Unit,
//    年份范围为30年前至今
    minYear: Int = LocalDate.now().minusYears(30).year,
    maxYear: Int = LocalDate.now().year,
    visibleItemsCount: Int = 4,
    modifier: Modifier = Modifier
) {
    var selectedYear by remember(defaultValue) {
        mutableStateOf(defaultValue.year)
    }
    var selectedMonth by remember(defaultValue) {
        mutableStateOf(defaultValue.monthValue)
    }
    var selectedDay by remember(defaultValue) { mutableStateOf(defaultValue.dayOfMonth) }
    
    // 初始化标志，用于防止首次渲染时触发onChange
    val isInitialized = remember { mutableStateOf(false) }
    
    // 当年份或月份变化时，调整日期范围
    LaunchedEffect(selectedYear, selectedMonth) {
        val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
        if (selectedDay > daysInMonth) {
            selectedDay = daysInMonth
        }
    }
    
    // 当任何值变化时，更新选中日期
    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
        // 跳过首次初始化时的调用
        if (!isInitialized.value) {
            isInitialized.value = true
            return@LaunchedEffect
        }
        
        val newDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
        onChange(newDate)
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 年份选择器（始终显示）
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "年",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            PickerColumn(
                items = (minYear..maxYear).toList(),
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it },
                visibleItemsCount = visibleItemsCount,
                itemFormatter = { "${it}年" },
                modifier = Modifier.weight(1f)
            )
        }
        
        // 月份选择器（模式二和三显示）
        if (mode != DatePickerMode.YEAR) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "月",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PickerColumn(
                    items = (1..12).toList(),
                    selectedItem = selectedMonth,
                    onItemSelected = { selectedMonth = it },
                    visibleItemsCount = visibleItemsCount,
                    itemFormatter = { "${it.toString().padStart(2, '0')}月" },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // 日期选择器（仅模式三显示）
        if (mode == DatePickerMode.DATE) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "日",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
                // 确保选中的日期不超过当月的天数
                val adjustedSelectedDay = kotlin.math.min(selectedDay, daysInMonth)
                PickerColumn(
                    items = (1..daysInMonth).toList(),
                    selectedItem = adjustedSelectedDay,
                    onItemSelected = { selectedDay = it },
                    visibleItemsCount = visibleItemsCount,
                    itemFormatter = { "${it.toString().padStart(2, '0')}日" },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 全新的通用选择器列组件 - 彻底解决自动滚动和定位问题
 *
 * @param items 可选项目列表
 * @param selectedItem 当前选中项目
 * @param onItemSelected 项目选择回调
 * @param visibleItemsCount 可见项目数量
 * @param itemFormatter 项目格式化函数
 * @param modifier 修饰符
 */
@Composable
private fun <T : Any> PickerColumn(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    visibleItemsCount: Int,
    itemFormatter: (T) -> String,
    modifier: Modifier = Modifier
) {
    val itemHeight = 48.dp
    val halfVisibleItems = visibleItemsCount / 2
    val coroutineScope = rememberCoroutineScope()
    
    // 获取选中项索引
    val selectedIndex = remember(selectedItem, items) {
        items.indexOf(selectedItem).let { if (it >= 0) it else 0 }
    }
    
    // 创建列表状态：直接设置为选中项的位置
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0
    )
    
    // 创建 Snap 行为
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    
    // 跟踪是否为用户主动滚动
    val isUserScrolling = remember { mutableStateOf(false) }
    val hasInitialized = remember { mutableStateOf(false) }
    
    // 初始化时确保滚动到正确位置
    LaunchedEffect(selectedItem) {
        if (!hasInitialized.value) {
            hasInitialized.value = true
            if (selectedIndex >= 0) {
                // 滚动到选中项，让它显示在中心
                // 由于有 halfVisibleItems 个顶部占位符，所以滚动到 selectedIndex + halfVisibleItems - 1
                // 为了更精确，使用 selectedIndex + halfVisibleItems - 1
                listState.scrollToItem(selectedIndex + halfVisibleItems - 1)
            }
        }
    }
    
    // 监听滚动状态变化
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            // 用户开始滚动
            isUserScrolling.value = true
        } else if (isUserScrolling.value) {
            // 滚动停止，计算中心项
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                // 计算视口中心位置
                val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                
                // 找到最接近中心的项目
                val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { itemInfo ->
                    kotlin.math.abs((itemInfo.offset + itemInfo.size / 2) - viewportCenter)
                }
                
                centerItem?.let {
                    val newSelectedItem = items.getOrNull(it.index)
                    if (newSelectedItem != null && newSelectedItem != selectedItem) {
                        onItemSelected(newSelectedItem)
                    }
                }
            }
        }
    }
    
    Box(
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .background(Color.Transparent)
    ) {
        // 中间高亮指示器
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                )
        )
        
        // 滚动列表
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            flingBehavior = snapBehavior
        ) {
            // 顶部空白占位
            items(halfVisibleItems) {
                Spacer(modifier = Modifier.height(itemHeight))
            }
            
            // 实际数据项
            items(
                count = items.size,
                key = { index -> items[index] }
            ) { index ->
                val item = items[index]
                val isSelected = item == selectedItem
                
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (item != selectedItem) {
                                isUserScrolling.value = true
                                onItemSelected(item)
                                // 滚动到点击的项目
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index + halfVisibleItems - 1)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemFormatter(item),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }
            
            // 底部空白占位
            items(halfVisibleItems) {
                Spacer(modifier = Modifier.height(itemHeight))
            }
        }
    }
}

/**
 * 便捷方法：创建年份选择器
 */
@Composable
fun YearPicker(
    defaultValue: LocalDate = LocalDate.now(),
    onChange: (LocalDate) -> Unit,
    minYear: Int = LocalDate.now().minusYears(5).year,
    maxYear: Int = LocalDate.now().plusYears(5).year,
    visibleItemsCount: Int = 4,
    modifier: Modifier = Modifier
) {
    DatePicker(
        mode = DatePickerMode.YEAR,
        defaultValue = defaultValue,
        onChange = onChange,
        minYear = minYear,
        maxYear = maxYear,
        visibleItemsCount = visibleItemsCount,
        modifier = modifier
    )
}

/**
 * 便捷方法：创建年月选择器
 */
@Composable
fun MonthPicker(
    defaultValue: LocalDate = LocalDate.now(),
    onChange: (LocalDate) -> Unit,
    minYear: Int = LocalDate.now().minusYears(5).year,
    maxYear: Int = LocalDate.now().plusYears(5).year,
    visibleItemsCount: Int = 4,
    modifier: Modifier = Modifier
) {
    DatePicker(
        mode = DatePickerMode.MONTH,
        defaultValue = defaultValue,
        onChange = onChange,
        minYear = minYear,
        maxYear = maxYear,
        visibleItemsCount = visibleItemsCount,
        modifier = modifier
    )
}

/**
 * 便捷方法：创建年月日选择器
 */
@Composable
fun DatePicker(
    defaultValue: LocalDate = LocalDate.now(),
    onChange: (LocalDate) -> Unit,
    minYear: Int = LocalDate.now().minusYears(5).year,
    maxYear: Int = LocalDate.now().plusYears(5).year,
    visibleItemsCount: Int = 4,
    modifier: Modifier = Modifier
) {
    DatePicker(
        mode = DatePickerMode.DATE,
        defaultValue = defaultValue,
        onChange = onChange,
        minYear = minYear,
        maxYear = maxYear,
        visibleItemsCount = visibleItemsCount,
        modifier = modifier
    )
}
