package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import com.yintianyan.tallybook.data.repository.TransactionRepositoryImpl
import com.yintianyan.tallybook.routes.Transaction
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.constants.CategoryConstants
import com.yintianyan.tallybook.components.NumberKeyboard
import com.yintianyan.tallybook.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.yintianyan.tallybook.components.datepicker.DatePickerModal
import com.yintianyan.tallybook.components.datepicker.DatePickerMode
import com.yintianyan.tallybook.components.datepicker.TimePickerModal
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.yintianyan.tallybook.utils.getCategoryColor
import com.yintianyan.tallybook.utils.getCategoryIconColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onDismiss: () -> Unit,
    onTransactionAdded: () -> Unit
) {
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember {
        mutableStateOf(
            when (selectedType) {
                TransactionType.INCOME -> TransactionCategory.SALARY
                TransactionType.EXPENSE -> TransactionCategory.FOOD
                TransactionType.ALL -> TransactionCategory.FOOD
            }
        )
    }
    var amount by remember { mutableStateOf("0.00") }
    var remark by remember { mutableStateOf("") }
    
    // 日期和时间选择相关状态
    var currentDate by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var currentTime by remember { mutableStateOf(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    // 获取数据库实例
    val context = LocalContext.current
    val database = remember { TallyBookDatabase.getDatabase(context) }
    val repository = remember { TransactionRepositoryImpl(database.transactionDao()) }
    val coroutineScope = rememberCoroutineScope()
    
    // 当交易类型改变时，自动更新分类为对应类型的默认分类
    LaunchedEffect(selectedType) {
        selectedCategory = when (selectedType) {
            TransactionType.INCOME -> TransactionCategory.SALARY
            TransactionType.EXPENSE -> TransactionCategory.FOOD
            TransactionType.ALL -> TransactionCategory.FOOD
        }
    }
    
    // 保存交易记录到数据库
    fun saveTransaction() {
        // 验证金额是否大于0
        val transactionAmount = amount.toDoubleOrNull() ?: 0.0
        if (transactionAmount <= 0.0) return
        
        // 验证时间是否在当前时间之前
        val selectedDateTimeString = "$currentDate $currentTime"
        val selectedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(selectedDateTimeString)
        val currentDateTime = Date()
        
        if (selectedDateTime != null && selectedDateTime.after(currentDateTime)) {
            // 未来时间记录不保存
            return
        }
        
        coroutineScope.launch {
            try {
                val transaction = Transaction(
                    id = 0, // 自动生成
                    date = currentDate,
                    time = currentTime,
                    category = selectedCategory,
                    type = selectedType,
                    amount = transactionAmount,
                    description = remark
                )
                repository.insertTransaction(transaction)
                onTransactionAdded() // 通知父组件数据已更新
            } catch (e: Exception) {
                // 捕获数据库操作和数据转换异常，避免应用闪退
                e.printStackTrace()
            }
        }
    }
    
    // 根据选中类型获取分类列表
    val categories = remember(selectedType) {
        when (selectedType) {
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
                TransactionCategory.SPORTS,
                TransactionCategory.PET,
                TransactionCategory.MEDICAL,
                TransactionCategory.LIVING_BILLS,
                TransactionCategory.RED_PACKET,
                TransactionCategory.CHILDREN,
                TransactionCategory.HOTEL,
                TransactionCategory.BEAUTY,
                TransactionCategory.HUMAN_RELATIONS,
                TransactionCategory.TRANSFER,
                TransactionCategory.SEND_RED_PACKET,
                TransactionCategory.INSURANCE
            )
            TransactionType.ALL -> emptyList()
        }
    }
    
    // 处理数字键盘输入
    fun handleNumberInput(input: String) {
        when (input) {
            "<", "X" -> {
                if (amount.isNotEmpty()) {
                    amount = amount.dropLast(1)
                }
                if (amount.isEmpty()) {
                    amount = "0.00"
                }
            }
            "OK" -> {
                saveTransaction()
                onDismiss()
            }
            "." -> {
                // 检查是否已经有小数点
                if (!amount.contains(".")) {
                    // 如果小数点前没有数字，默认为"0."
                    if (amount.isEmpty() || amount == "0.00") {
                        amount = "0."
                    } else {
                        amount += input
                    }
                }
            }
            else -> {
                // 检查输入是否为数字
                if (input.matches(Regex("\\d"))) {
                    if (amount == "0.00") {
                        // 如果当前是默认值，直接替换为输入的数字
                        amount = input
                    } else if (amount == "0") {
                        // 防止多个前导零
                        amount = input
                    } else {
                        // 检查是否在小数部分，限制小数部分最多两位
                        val hasDecimalPoint = amount.contains(".")
                        if (hasDecimalPoint) {
                            val parts = amount.split(".")
                            val decimalPart = parts[1]
                            // 只有当小数部分小于2位时才允许输入
                            if (decimalPart.length < 2) {
                                amount += input
                            }
                        } else {
                            // 整数部分没有限制（除了总长度限制）
                            // 限制总长度，避免输入过长
                            if (amount.length < 10) {
                                amount += input
                            }
                        }
                    }
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
    
    // 移除自定义的ModalBottomSheet实现，因为已经由AddTransactionModal中的BottomSheetPopup接管了
    // 只需要保留内容部分
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()) // 添加底部安全距离
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card() {
                Row {
                    Box(
                        modifier = Modifier
                            .clickable { selectedType = TransactionType.EXPENSE }
                            .background(
                                if (selectedType == TransactionType.EXPENSE) PrimaryBlue else LightGray)
                            .padding(horizontal = 16.dp, vertical = 8.dp)

                    ) {
                        Text(
                            text = "支出",
                            color = if (selectedType == TransactionType.EXPENSE) White else DarkGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clickable { selectedType = TransactionType.INCOME }
                            .background(
                                if (selectedType == TransactionType.INCOME) PrimaryBlue else LightGray)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "收入",
                            color = if (selectedType == TransactionType.INCOME) White else DarkGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Text(
                        text = currentDate,
                        color = Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            showDatePicker = true
                        }
                    )
                }
                Box {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = MaterialTheme.icons.close,
                            contentDescription = "关闭",
                            tint = Gray,
                            modifier = Modifier.size(16.dp) 
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedCategory = category
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedCategory == category) PrimaryBlue.copy(alpha = 0.2f) else 
                                    category.getCategoryColor()
                            )   
                            .border(
                                2.dp,
                                if (selectedCategory == category) PrimaryBlue else Color.Transparent,
                                CircleShape
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            category.icon, 
                            contentDescription = CategoryConstants.CATEGORY_DISPLAY_NAME_MAP[category], 
                            tint = category.getCategoryIconColor()
                        )
                    }
                    Text(
                        text = CategoryConstants.CATEGORY_DISPLAY_NAME_MAP[category] ?: "其他",
                        fontSize = 12.sp,
                        color = DarkGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        
        // 备注输入
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .clickable { /* 打开备注输入框 */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = MaterialTheme.icons.edit,
                contentDescription = "添加备注",
                tint = Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (remark.isEmpty()) "添加备注" else remark,
                color = if (remark.isEmpty()) Gray else DarkGray,
                fontSize = 16.sp
            )
        }

        // 金额显示
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, bottom = 32.dp),

        ) {
            Text(
                text = "¥ ${formatAmount(amount)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
        }
        
        // 数字键盘
        NumberKeyboard(onKeyPress = ::handleNumberInput)
    }

    // 调用日期选择器弹窗
    if (showDatePicker) {
        val dateParts = currentDate.split("-")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val day = dateParts[2].toInt()
        val selectedDate = LocalDate.of(year, month, day)
        
        DatePickerModal(
            show = true,
            mode = DatePickerMode.DATE,
            selectedDate = selectedDate,
            onDismiss = { showDatePicker = false },
            onDateSelected = { y, m, d -> 
                currentDate = String.format("%04d-%02d-%02d", y, m, d)
            }
        )
    }
    
    // 调用时间选择器弹窗
    if (showTimePicker) {
        val timeParts = currentTime.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val second = timeParts[2].toInt()
        val initialTime = LocalTime.of(hour, minute, second)

        TimePickerModal(
            show = true,
            initialTime = initialTime,
            onDismiss = { showTimePicker = false },
            onTimeSelected = { time -> 
                currentTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            }
        )
    }
}

/**
 * 预览函数 - 用于在Android Studio中预览AddTransactionScreen的实现效果
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = false
)
@Composable
fun PreviewAddTransactionScreen() {
    TallyBookTheme {
        AddTransactionScreen(
            onDismiss = { /* 预览模式下不需要实际关闭 */ },
            onTransactionAdded = { /* 预览模式下不需要实际处理添加事件 */ }
        )
    }
}
