// HomeViewModel.kt
// 首页视图模型，负责管理首页的状态、数据加载和业务逻辑

package com.yintianyan.tallybook.screens.homescreen.viewmodel

import android.content.Context
import android.util.Log
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
 * HomeViewModel 管理首页的所有状态和业务逻辑
 * @param context 应用上下文，用于数据库访问
 */
class HomeViewModel(private val context: Context) {
    // 筛选状态
    var selectedCategory by mutableStateOf(TransactionCategory.ALL)
    var selectedType by mutableStateOf(TransactionType.ALL)
    
    // 月份选择状态
    var selectedMonth by mutableStateOf("")
    var showMonthPicker by mutableStateOf(false)
    var selectedYear by mutableIntStateOf(0)
    var selectedMonthValue by mutableIntStateOf(0) // 1-12
    
    // 筛选面板状态
    var showFilterDialog by mutableStateOf(false)
    
    // 添加交易页面状态
    var showAddTransactionScreen by mutableStateOf(false)
    
    // 加载状态
    var isLoading by mutableStateOf(false)
    
    // 数据状态 - 源数据（本月所有交易）
    var currentMonthTransactions by mutableStateOf<List<Transaction>>(emptyList())
    
    // 刷新触发器
    var refreshTrigger by mutableIntStateOf(0)
    
    // 数据库访问
    private val database = TallyBookDatabase.getDatabase(context)
    private val transactionDao: TransactionDao = database.transactionDao()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    // 当前月份字符串（格式：YYYY-MM）
    val currentMonth by derivedStateOf { 
        String.format("%d-%02d", selectedYear, selectedMonthValue)
    }
    
    // 过滤后的交易数据 - 基于源数据和筛选条件实时计算
    val transactions by derivedStateOf { 
        currentMonthTransactions.filter { transaction ->
            val typeMatch = selectedType == TransactionType.ALL || transaction.type == selectedType
            val categoryMatch = selectedCategory == TransactionCategory.ALL || transaction.category == selectedCategory
            typeMatch && categoryMatch
        }
    }
    
    // 按日期分组的交易数据
    val transactionsByDate by derivedStateOf { 
        transactions.groupBy { it.date }
    }
    
    // 本月收入和支出统计 - 始终基于本月所有数据统计，不受筛选影响
    val monthlyIncome by derivedStateOf { 
        currentMonthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }
    
    val incomeCount by derivedStateOf { 
        currentMonthTransactions
            .count { it.type == TransactionType.INCOME }
    }
    
    val monthlyExpense by derivedStateOf { 
        currentMonthTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }
    
    val expenseCount by derivedStateOf { 
        currentMonthTransactions
            .count { it.type == TransactionType.EXPENSE }
    }
    
    // 根据选中类型过滤的分类列表
    val filteredCategories by derivedStateOf { 
        when (selectedType) {
            TransactionType.ALL -> listOf(
                TransactionCategory.ALL,
                // 支出分类
                TransactionCategory.FOOD, TransactionCategory.TRANSPORT, TransactionCategory.SHOPPING, 
                TransactionCategory.ENTERTAINMENT, TransactionCategory.EDUCATION, TransactionCategory.CLOTHING,
                TransactionCategory.SPORTS, TransactionCategory.MEDICAL
            )
            TransactionType.INCOME -> listOf(
                TransactionCategory.ALL,
                // 收入分类
                TransactionCategory.SALARY, TransactionCategory.BONUS, TransactionCategory.RECEIVE_RED_PACKET,
                TransactionCategory.RECEIVE_TRANSFER, TransactionCategory.OTHER_HUMAN_RELATIONS
            )
            TransactionType.EXPENSE -> listOf(
                TransactionCategory.ALL,
                // 支出分类
                TransactionCategory.FOOD, TransactionCategory.TRANSPORT, TransactionCategory.SHOPPING,
                TransactionCategory.ENTERTAINMENT, TransactionCategory.EDUCATION, TransactionCategory.CLOTHING,
                TransactionCategory.SPORTS, TransactionCategory.PET, TransactionCategory.MEDICAL,
                TransactionCategory.LIVING_BILLS, TransactionCategory.RED_PACKET, TransactionCategory.CHILDREN
            )
        }
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
     * 加载交易数据
     */
    private fun loadData() {
        // 直接构建当前月份字符串，避免依赖derivedStateOf的计算时机
        val monthString = String.format("%d-%02d", selectedYear, selectedMonthValue)
        
        isLoading = true
        
        // 调试日志
        Log.d("HomeViewModel", "loadData called for month: $monthString")
        
        // 加载本月所有交易数据
        coroutineScope.launch {
            try {
                // 查询指定月份的所有交易记录
                val entities = transactionDao.getCurrentMonthTransactions(monthString)
                
                // 转换为Transaction类型
                val transactionsList = entities.map { it.toTransaction() }
                Log.d("HomeViewModel", "  Loaded ${transactionsList.size} transactions")
                
                // 在主线程更新状态
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
        refreshData()
    }
    
    /**
     * 刷新数据
     */
    fun refreshData() {
        refreshTrigger += 1
        loadData()
    }
    
    /**
     * 删除交易记录
     */
    fun deleteTransaction(transaction: Transaction) {
        coroutineScope.launch {
            try {
                // 转换回 Entity 进行删除
                val entity = TransactionEntity(
                    id = transaction.id,
                    date = transaction.date,
                    time = transaction.time,
                    category = transaction.category.name,
                    type = transaction.type.name,
                    amount = transaction.amount,
                    description = transaction.description
                )
                transactionDao.deleteTransaction(entity)
                
                // 刷新数据
                refreshData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 重置筛选条件
     */
    fun resetFilters() {
        selectedType = TransactionType.ALL
        selectedCategory = TransactionCategory.ALL
    }
    
    /**
     * 获取当前筛选条件的显示文本
     */
    fun getFilterText(): String {
        return buildString {
            append(when (selectedType) {
                TransactionType.ALL -> "全部"
                TransactionType.INCOME -> "收入"
                TransactionType.EXPENSE -> "支出"
            })
            
            if (selectedCategory != TransactionCategory.ALL) {
                append(" · ")
                append(CategoryConstants.CATEGORY_DISPLAY_NAME_MAP[selectedCategory] ?: "其他")
            }
        }
    }
}

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
