package com.yintianyan.tallybook.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.datepicker.DatePickerModal
import com.yintianyan.tallybook.components.datepicker.DatePickerMode
import com.yintianyan.tallybook.screens.statistics.viewmodel.StatisticsViewModel
import com.yintianyan.tallybook.theme.*
import com.yintianyan.tallybook.utils.formatAmount
import com.yintianyan.tallybook.data.repository.TransactionRepositoryImpl
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    val database = remember { TallyBookDatabase.getDatabase(context) }
    val repository = remember { TransactionRepositoryImpl(database.transactionDao()) }
    val viewModel = remember { StatisticsViewModel(repository) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "统计", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                modifier = Modifier.background(White),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        containerColor = White
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(it),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 月份选择
            item {
                MonthSelector(
                    selectedMonth = viewModel.selectedMonth,
                    onMonthClick = { viewModel.showMonthPicker = true }
                )
            }
            
            // 统计概览卡片
            item {
                StatisticsOverview(
                    monthlyIncome = viewModel.monthlyIncome,
                    monthlyExpense = viewModel.monthlyExpense,
                    balance = viewModel.balance,
                    incomeCount = viewModel.incomeCount,
                    expenseCount = viewModel.expenseCount
                )
            }
            
            // 分类统计卡片
            item {
                CategoryStatistics(
                    incomeCategories = viewModel.incomeCategories,
                    expenseCategories = viewModel.expenseCategories,
                    getCategoryDisplayName = { viewModel.getCategoryDisplayName(it) }
                )
            }
            
            // 月度趋势卡片
            item {
                MonthlyTrendCard()
            }
        }
        
        // 月份选择器弹窗
        DatePickerModal(
            show = viewModel.showMonthPicker,
            mode = DatePickerMode.MONTH,
            selectedDate = LocalDate.of(viewModel.selectedYear, viewModel.selectedMonthValue, 1),
            onDateSelected = { year, month, _ ->
                viewModel.setSelectedMonth(year, month)
            },
            onDismiss = { viewModel.showMonthPicker = false }
        )
    }
}

/**
 * 月份选择组件
 */
@Composable
fun MonthSelector(selectedMonth: String, onMonthClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onMonthClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedMonth,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "选择月份",
            tint = DarkGray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * 统计概览组件
 */
@Composable
fun StatisticsOverview(
    monthlyIncome: Double,
    monthlyExpense: Double,
    balance: Double,
    incomeCount: Int,
    expenseCount: Int
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // 收支概览
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatisticsCard(
                title = "本月总收入",
                value = monthlyIncome.formatAmount(),
                secondaryValue = "共${incomeCount}笔",
                color = IncomeBackground,
                textColor = IncomeText,
                modifier = Modifier.weight(0.48f)
            )
            StatisticsCard(
                title = "本月总支出",
                value = monthlyExpense.formatAmount(),
                secondaryValue = "共${expenseCount}笔",
                color = ExpenseBackground,
                textColor = ExpenseText,
                modifier = Modifier.weight(0.48f)
            )
        }
        
        // 余额概览
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightGray),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "本月结余",
                    fontSize = 14.sp,
                    color = DarkGray
                )
                Text(
                    text = balance.formatAmount(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * 统计卡片组件
 */
@Composable
fun StatisticsCard(
    title: String,
    value: String,
    secondaryValue: String,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = DarkGray
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = secondaryValue,
                fontSize = 12.sp,
                color = Gray
            )
        }
    }
}

/**
 * 分类统计组件
 */
@Composable
fun CategoryStatistics(
    incomeCategories: List<com.yintianyan.tallybook.screens.statistics.viewmodel.CategoryStatistic>,
    expenseCategories: List<com.yintianyan.tallybook.screens.statistics.viewmodel.CategoryStatistic>,
    getCategoryDisplayName: (com.yintianyan.tallybook.routes.TransactionCategory) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "分类统计",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 收入分类
            if (incomeCategories.isNotEmpty()) {
                Text(
                    text = "收入",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IncomeText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                incomeCategories.forEachIndexed { index, categoryStat ->
                    CategoryStatisticsItem(
                        category = getCategoryDisplayName(categoryStat.category),
                        amount = categoryStat.amount.formatAmount(),
                        percentage = "100%", // 简化实现，实际应计算百分比
                        color = IncomeText,
                        isLast = index == incomeCategories.size - 1
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 支出分类
            if (expenseCategories.isNotEmpty()) {
                Text(
                    text = "支出",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ExpenseText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                expenseCategories.forEachIndexed { index, categoryStat ->
                    CategoryStatisticsItem(
                        category = getCategoryDisplayName(categoryStat.category),
                        amount = categoryStat.amount.formatAmount(),
                        percentage = "100%", // 简化实现，实际应计算百分比
                        color = ExpenseText,
                        isLast = index == expenseCategories.size - 1
                    )
                }
            }
            
            // 无数据状态
            if (incomeCategories.isEmpty() && expenseCategories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(LightGray)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无交易数据",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }
        }
    }
}

/**
 * 分类统计项组件
 */
@Composable
fun CategoryStatisticsItem(
    category: String,
    amount: String,
    percentage: String,
    color: Color,
    isLast: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category,
                    fontSize = 14.sp,
                    color = DarkGray
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = percentage,
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        }
        
        if (!isLast) {
            Divider(
                color = LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

/**
 * 月度趋势卡片组件
 */
@Composable
fun MonthlyTrendCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "月度趋势",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 图表区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(LightGray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "图表区域",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        }
    }
}

