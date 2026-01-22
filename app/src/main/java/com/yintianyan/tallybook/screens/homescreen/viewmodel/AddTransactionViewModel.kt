package com.yintianyan.tallybook.screens.homescreen.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import com.yintianyan.tallybook.model.entity.TransactionEntity
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * AddTransactionViewModel 管理添加交易屏幕的状态和业务逻辑
 */
class AddTransactionViewModel(private val context: Context) {

    // 交易类型和分类状态
    var selectedType by mutableStateOf(TransactionType.EXPENSE)
    var selectedCategory by mutableStateOf(TransactionCategory.FOOD)

    // 金额状态
    var amount by mutableStateOf("0.00")

    // 备注状态
    var remark by mutableStateOf("")

    // 日期和时间状态
    var currentDate by mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    var currentTime by mutableStateOf(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))
    var showDatePicker by mutableStateOf(false)
    var showTimePicker by mutableStateOf(false)
    var showRemarkDialog by mutableStateOf(false)

    // 数据库
    private val database = TallyBookDatabase.getDatabase(context)
    private val transactionDao = database.transactionDao()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // 初始化分类
    init {
        updateCategoryForType(selectedType)
    }

    // 当交易类型改变时，更新分类
    fun updateCategoryForType(type: TransactionType) {
        selectedCategory = when (type) {
            TransactionType.INCOME -> TransactionCategory.SALARY
            TransactionType.EXPENSE -> TransactionCategory.FOOD
            TransactionType.ALL -> TransactionCategory.FOOD
        }
    }

    // 获取分类列表
    fun getCategoriesForType(type: TransactionType): List<TransactionCategory> {
        return when (type) {
            TransactionType.INCOME -> listOf(
                TransactionCategory.SALARY,
                TransactionCategory.BONUS,
                TransactionCategory.RECEIVE_RED_PACKET,
                TransactionCategory.RECEIVE_TRANSFER,
                TransactionCategory.OTHER_HUMAN_RELATIONS
            )
            TransactionType.EXPENSE -> listOf(
                TransactionCategory.FOOD,
                TransactionCategory.TRANSPORT,
                TransactionCategory.SHOPPING,
                TransactionCategory.ENTERTAINMENT,
                TransactionCategory.EDUCATION,
                TransactionCategory.CLOTHING,
                TransactionCategory.SPORTS
            )
            TransactionType.ALL -> emptyList()
        }
    }

    // 处理数字键盘输入
    fun handleNumberInput(input: String) {
        when (input) {
            "<", "X" -> {
                if (amount.length > 1) {
                    amount = amount.dropLast(1)
                } else {
                    amount = "0.00"
                }
            }
            "." -> {
                if (!amount.contains(".")) {
                    if (amount == "0.00") {
                        amount = "0."
                    } else {
                        amount += "."
                    }
                }
            }
            else -> {
                if (amount == "0.00") {
                    amount = input
                } else {
                    // 检查小数点后位数
                    val parts = amount.split(".")
                    if (parts.size == 2 && parts[1].length >= 2) {
                        // 已经有两位小数，不添加
                        return
                    }
                    amount += input
                }
            }
        }
    }

    // 格式化金额显示
    fun formatAmount(amount: String): String {
        if (amount.isEmpty()) return "0.00"

        val parts = amount.split(".")
        val integerPart = parts[0]
        val decimalPart = if (parts.size > 1) parts[1] else ""

        return if (decimalPart.length <= 2) {
            String.format("%s.%s", integerPart, decimalPart.padEnd(2, '0'))
        } else {
            String.format("%s.%s", integerPart, decimalPart.substring(0, 2))
        }
    }

    // 保存交易记录
    fun saveTransaction(onSuccess: () -> Unit, onError: (Exception) -> Unit = {}) {
        // 验证金额是否大于0
        val transactionAmount = amount.toDoubleOrNull() ?: 0.0
        if (transactionAmount <= 0.0) {
            onError(IllegalArgumentException("金额必须大于0"))
            return
        }

        // 验证时间是否在当前时间之前
        val selectedDateTimeString = "$currentDate $currentTime"
        val selectedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(selectedDateTimeString)
        val currentDateTime = Date()

        if (selectedDateTime != null && selectedDateTime.after(currentDateTime)) {
            onError(IllegalArgumentException("不能选择未来的时间"))
            return
        }

        coroutineScope.launch {
            try {
                val transactionEntity = TransactionEntity(
                    id = 0, // 自动生成
                    date = currentDate,
                    time = currentTime,
                    category = selectedCategory.name,
                    type = selectedType.name,
                    amount = transactionAmount,
                    description = remark
                )
                transactionDao.insertTransaction(transactionEntity)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    // 重置状态
    fun reset() {
        selectedType = TransactionType.EXPENSE
        selectedCategory = TransactionCategory.FOOD
        amount = "0.00"
        remark = ""
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        showDatePicker = false
        showTimePicker = false
        showRemarkDialog = false
    }
}