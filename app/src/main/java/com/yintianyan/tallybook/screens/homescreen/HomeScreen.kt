package com.yintianyan.tallybook.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.datepicker.DatePickerModal
import com.yintianyan.tallybook.components.datepicker.DatePickerMode
import com.yintianyan.tallybook.routes.*
import com.yintianyan.tallybook.screens.homescreen.view.*
import com.yintianyan.tallybook.screens.homescreen.viewmodel.HomeViewModel
import com.yintianyan.tallybook.screens.homescreen.viewmodel.formatAmount
import com.yintianyan.tallybook.theme.*

import java.time.LocalDate
/**
 * 首页主组件
 * 
 * 整合了所有首页相关的子组件，包括：
 * - HomeHeader: 顶部标题栏和月份选择器
 * - BalanceOverviewCard: 收入支出概览卡片
 * - FilterToolbar: 筛选工具栏
 * - TransactionList: 交易列表
 * - FilterDialog: 筛选对话框
 * - AddTransactionModal: 添加交易的底部弹窗
 * - DatePickerModal: 日期选择的底部弹窗
 * 
 * 使用HomeViewModel管理所有状态和业务逻辑
 */
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel = remember { HomeViewModel(context) }

    Scaffold(
        floatingActionButton = { 
            FloatingActionButton(
                onClick = { viewModel.showAddTransactionScreen = true },
                containerColor = PrimaryBlue,
                shape = CircleShape,
                modifier = Modifier.offset(x = (-24).dp, y = (-24).dp)
            ) {
                Icon(MaterialTheme.icons.add, contentDescription = "添加", tint = White)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(it)
            ) {
                // 顶部标题栏
                HomeHeader(
                    selectedMonth = viewModel.selectedMonth,
                    onMonthClick = { viewModel.showMonthPicker = true }
                )

                // 收入支出概览卡片
                BalanceOverviewCard(viewModel = viewModel)

                // 筛选工具栏
                FilterToolbar(
                    filterText = viewModel.getFilterText(),
                    onFilterClick = { viewModel.showFilterDialog = true }
                )

                // 交易列表 - 使用简化的stickyHeader实现
                TransactionList(
                    transactionsByDate = viewModel.transactionsByDate,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )

    // 筛选对话框
    FilterDialog(
        show = viewModel.showFilterDialog,
        selectedType = viewModel.selectedType,
        selectedCategory = viewModel.selectedCategory,
        filteredCategories = viewModel.filteredCategories,
        onDismiss = { viewModel.showFilterDialog = false },
        onTypeChange = { type -> 
            viewModel.selectedType = type
            viewModel.selectedCategory = TransactionCategory.ALL
            viewModel.refreshData()
        },
        onCategoryChange = { category -> 
            viewModel.selectedCategory = category
            viewModel.refreshData()
        },
        onReset = { 
            viewModel.resetFilters()
            viewModel.refreshData()
        }
    )

    // 添加交易页面弹出动画
    AddTransactionModal(
        show = viewModel.showAddTransactionScreen,
        onDismiss = { viewModel.showAddTransactionScreen = false },
        onTransactionAdded = { viewModel.refreshData() } // 触发数据刷新
    )

    // 月份选择器弹出动画
    DatePickerModal(
        show = viewModel.showMonthPicker,
        mode = DatePickerMode.DATE,
        selectedDate = LocalDate.of(viewModel.selectedYear, viewModel.selectedMonthValue, 1),
        onDateSelected = { year, month, _ ->
            viewModel.setSelectedMonth(year, month)
        },
        onDismiss = { viewModel.showMonthPicker = false }
    )
}

/**
 * 首页预览组件
 * 
 * 使用模拟数据展示首页界面，方便开发过程中查看效果
 * 避免依赖真实的HomeViewModel
 */
@Preview(showBackground = true, name = "首页预览", widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenPreview() {
    // 模拟交易数据
    val mockTransactions = listOf(
        Transaction(
            id = 1,
            date = "2025-12-30",
            time = "12:30",
            category = TransactionCategory.FOOD,
            type = TransactionType.EXPENSE,
            amount = 45.50,
            description = "午餐"
        ),
        Transaction(
            id = 2,
            date = "2025-12-30",
            time = "18:45",
            category = TransactionCategory.TRANSPORT,
            type = TransactionType.EXPENSE,
            amount = 12.00,
            description = "打车"
        ),
        Transaction(
            id = 3,
            date = "2025-12-29",
            time = "14:20",
            category = TransactionCategory.SHOPPING,
            type = TransactionType.EXPENSE,
            amount = 128.00,
            description = "购买日用品"
        ),
        Transaction(
            id = 4,
            date = "2025-12-29",
            time = "09:00",
            category = TransactionCategory.ENTERTAINMENT,
            type = TransactionType.EXPENSE,
            amount = 80.00,
            description = "电影票"
        ),
        Transaction(
            id = 5,
            date = "2025-12-28",
            time = "09:00",
            category = TransactionCategory.SALARY,
            type = TransactionType.INCOME,
            amount = 8500.00,
            description = "12月工资"
        )
    )
    
    // 按日期分组
    val mockTransactionsByDate = mockTransactions.groupBy { it.date }
    
    Scaffold(
        floatingActionButton = { 
            FloatingActionButton(
                onClick = {},
                containerColor = PrimaryBlue,
                shape = CircleShape,
                modifier = Modifier.offset(x = (-24).dp, y = (-24).dp)
            ) {
                Icon(MaterialTheme.icons.add, contentDescription = "添加", tint = White)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(padding)
            ) {
                // 顶部标题栏
                HomeHeader(
                    selectedMonth = "2025年12月",
                    onMonthClick = {}
                )

                // 收入支出概览卡片（简化版）
                BalanceOverviewCard(
                    totalIncome = 8500.00,
                    totalExpense = 265.50
                )

                // 筛选工具栏（简化版）
                FilterToolbar(
                    filterText = "全部",
                    onFilterClick = {}
                )

                // 交易列表 - 使用简化的stickyHeader实现
                TransactionList(
                    transactionsByDate = mockTransactionsByDate,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}

/**
 * 简化版的收入支出概览卡片，用于预览
 */
@Composable
private fun BalanceOverviewCard(totalIncome: Double, totalExpense: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "收入支出概览",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "收入",
                        fontSize = 14.sp,
                        color = White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "¥${totalIncome.formatAmount()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                Column {
                    Text(
                        text = "支出",
                        fontSize = 14.sp,
                        color = White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "¥${totalExpense.formatAmount()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    }
}


