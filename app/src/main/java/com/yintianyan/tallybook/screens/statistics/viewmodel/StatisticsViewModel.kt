package com.yintianyan.tallybook.screens.statistics.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import com.yintianyan.tallybook.model.dao.TransactionDao
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import com.yintianyan.tallybook.model.entity.TransactionEntity
import com.yintianyan.tallybook.routes.Transaction
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.constants.CategoryConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * 统计页面视图模型，负责管理统计页面的状态、数据加载和业务逻辑
 */
class StatisticsViewModel(private val context: Context) {
    // 月份选择状态
    var selectedMonth by mutableStateOf("2025年12月")
    var selectedYear by mutableIntStateOf(2025)
    var selectedMonthValue by mutableIntStateOf(12) // 1-12
    var showMonthPicker by mutableStateOf(false)
    
    // 筛选状态
    var selectedType by mutableStateOf(TransactionType.ALL)
    
    // 数据状态
    var currentMonthTransactions by mutableStateOf<List<Transaction>>(emptyList())
    var isLoading by mutableStateOf(true)
    
    // 数据库访问
    private val database = TallyBookDatabase.getDatabase(context)
    private val transactionDao: TransactionDao = database.transactionDao()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    // 当前月份字符串（格式：YYYY-MM）
    val currentMonth by derivedStateOf { 
        String.format("%d-%02d", selectedYear, selectedMonthValue)
    }
    
    // 本月收入和支出统计
    val monthlyIncome by derivedStateOf { 
        currentMonthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }
    
    val monthlyExpense by derivedStateOf { 
        currentMonthTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }
    
    val balance by derivedStateOf { 
        monthlyIncome - monthlyExpense
    }
    
    val incomeCount by derivedStateOf { 
        currentMonthTransactions
            .count { it.type == TransactionType.INCOME }
    }
    
    val expenseCount by derivedStateOf { 
        currentMonthTransactions
            .count { it.type == TransactionType.EXPENSE }
    }
    
    // 分类统计数据
    val categoryStatistics by derivedStateOf { 
        currentMonthTransactions
            .groupBy { it.category }
            .mapValues { (category, transactions) ->
                val total = transactions.sumOf { it.amount }
                val type = transactions.firstOrNull()?.type ?: TransactionType.EXPENSE
                CategoryStatistic(category, total, type)
            }
            .values
            .sortedByDescending { it.amount }
    }
    
    // 按类型分组的分类统计
    val incomeCategories by derivedStateOf { 
        categoryStatistics.filter { it.type == TransactionType.INCOME }
    }
    
    val expenseCategories by derivedStateOf { 
        categoryStatistics.filter { it.type == TransactionType.EXPENSE }
    }
    
    init {
        // 初始化当前日期
        val calendar = Calendar.getInstance()
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonthValue = calendar.get(Calendar.MONTH) + 1 // 月份从0开始
        selectedMonth = "${selectedYear}年${selectedMonthValue}月"
        
        // 加载数据
        loadData()
    }
    
    /**
     * 加载统计数据
     */
    fun loadData() {
        isLoading = true
        
        // 直接构建当前月份字符串，避免依赖derivedStateOf的计算时机
        val monthString = String.format("%d-%02d", selectedYear, selectedMonthValue)
        
        coroutineScope.launch {
            try {
                // 加载指定月份的所有交易数据
                val entities = transactionDao.getCurrentMonthTransactions(monthString)
                val transactionsList = entities.map { it.toTransaction() }
                
                withContext(Dispatchers.Main) {
                    currentMonthTransactions = transactionsList
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    currentMonthTransactions = emptyList()
                    isLoading = false
                }
            }
        }
    }
    
    /**
     * 设置选中的月份
     */
    fun setSelectedMonth(year: Int, month: Int) {
        selectedYear = year
        selectedMonthValue = month
        selectedMonth = "${year}年${month}月"
        loadData()
    }
    
    /**
     * 获取分类的显示名称
     */
    fun getCategoryDisplayName(category: TransactionCategory): String {
        return CategoryConstants.CATEGORY_DISPLAY_NAME_MAP[category] ?: "其他"
    }
}

/**
 * 分类统计数据类
 */
data class CategoryStatistic(
    val category: TransactionCategory,
    val amount: Double,
    val type: TransactionType
)

/**
 * 金额格式化函数
 */
fun Double.formatAmount(): String {
    return String.format("¥%,.2f", this)
}

/**
 * 带符号的金额格式化函数
 */
fun Double.formatAmountWithSign(type: TransactionType): String {
    val formatted = String.format("%,.2f", this)
    return if (type == TransactionType.INCOME) "+¥$formatted" else "-¥$formatted"
}

/**
 * 转换函数：将TransactionEntity转换为Transaction
 */
fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        date = date,
        time = time,
        category = TransactionCategory.valueOf(category),
        type = TransactionType.valueOf(type),
        amount = amount,
        description = description
    )
}
