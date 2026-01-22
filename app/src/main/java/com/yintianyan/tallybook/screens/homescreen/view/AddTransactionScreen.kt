package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import com.yintianyan.tallybook.model.entity.TransactionEntity
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.constants.CategoryConstants
import com.yintianyan.tallybook.components.NumberKeyboard
import com.yintianyan.tallybook.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// 添加扩展函数，统一处理分类颜色映射
private fun TransactionCategory.getCategoryColor(): Color {
    return when (this) {
        TransactionCategory.FOOD -> Color(0xFFE0F7FA)
        TransactionCategory.TRANSPORT -> Color(0xFFE8F5E9)
        TransactionCategory.SHOPPING -> Color(0xFFF3E5F5)
        TransactionCategory.ENTERTAINMENT -> Color(0xFFFCE4EC)
        TransactionCategory.EDUCATION -> Color(0xFFFFF8E1)
        TransactionCategory.CLOTHING -> Color(0xFFE3F2FD)
        TransactionCategory.SPORTS -> Color(0xFFE8EAF6)
        TransactionCategory.PET -> Color(0xFFE0E0E0)
        TransactionCategory.MEDICAL -> Color(0xFFEF9A9A)
        TransactionCategory.LIVING_BILLS -> Color(0xFFB39DDB)
        TransactionCategory.RED_PACKET -> Color(0xFFFFE0B2)
        TransactionCategory.CHILDREN -> Color(0xFFC5E1A5)
        TransactionCategory.HOTEL -> Color(0xFFA5D6A7)
        TransactionCategory.BEAUTY -> Color(0xFFFFCCBC)
        TransactionCategory.HUMAN_RELATIONS -> Color(0xFFD1C4E9)
        TransactionCategory.TRANSFER -> Color(0xFFC8E6C9)
        TransactionCategory.SEND_RED_PACKET -> Color(0xFFFFAB91)
        TransactionCategory.INSURANCE -> Color(0xFFB0BEC5)
        TransactionCategory.EXPENSE_OTHER -> Color(0xFFCFD8DC)
        TransactionCategory.SALARY -> Color(0xFFA5D6A7)
        TransactionCategory.BONUS -> Color(0xFFFFE082)
        TransactionCategory.RECEIVE_RED_PACKET -> Color(0xFFFFAB91)
        TransactionCategory.RECEIVE_TRANSFER -> Color(0xFF80CBC4)
        TransactionCategory.OTHER_HUMAN_RELATIONS -> Color(0xFFCE93D8)
        TransactionCategory.INCOME_OTHER -> Color(0xFFB0BEC5)
        TransactionCategory.ALL -> Color(0xFF757575)
    }
}

// 添加扩展函数，统一处理分类图标颜色
private fun TransactionCategory.getCategoryIconColor(): Color {
    return when (this) {
        TransactionCategory.FOOD -> Color(0xFF00BCD4)
        TransactionCategory.TRANSPORT -> Color(0xFF4CAF50)
        TransactionCategory.SHOPPING -> Color(0xFF9C27B0)
        TransactionCategory.ENTERTAINMENT -> Color(0xFFE91E63)
        TransactionCategory.EDUCATION -> Color(0xFFFBC02D)
        TransactionCategory.CLOTHING -> Color(0xFF2196F3)
        TransactionCategory.SPORTS -> Color(0xFF3F51B5)
        TransactionCategory.PET -> Color(0xFF795548)
        TransactionCategory.MEDICAL -> Color(0xFFF44336)
        TransactionCategory.LIVING_BILLS -> Color(0xFFFF5722)
        TransactionCategory.RED_PACKET -> Color(0xFFFF9800)
        TransactionCategory.CHILDREN -> Color(0xFF8BC34A)
        TransactionCategory.HOTEL -> Color(0xFF673AB7)
        TransactionCategory.BEAUTY -> Color(0xFFE91E63)
        TransactionCategory.HUMAN_RELATIONS -> Color(0xFF9C27B0)
        TransactionCategory.TRANSFER -> Color(0xFF3F51B5)
        TransactionCategory.SEND_RED_PACKET -> Color(0xFFFFC107)
        TransactionCategory.INSURANCE -> Color(0xFF00BCD4)
        TransactionCategory.EXPENSE_OTHER -> Color(0xFF757575)
        TransactionCategory.SALARY -> Color(0xFF4CAF50)
        TransactionCategory.BONUS -> Color(0xFFFFC107)
        TransactionCategory.RECEIVE_RED_PACKET -> Color(0xFFE91E63)
        TransactionCategory.RECEIVE_TRANSFER -> Color(0xFF2196F3)
        TransactionCategory.OTHER_HUMAN_RELATIONS -> Color(0xFF9C27B0)
        TransactionCategory.INCOME_OTHER -> Color(0xFF757575)
        TransactionCategory.ALL -> Color(0xFF757575)
    }
}

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
    val transactionDao = remember { database.transactionDao() }
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
                val transactionEntity = TransactionEntity(
                    id = 0, // 自动生成
                    date = currentDate,
                    time = currentTime,
                    category = selectedCategory.name, // 将枚举转换为String
                    type = selectedType.name, // 将枚举转换为String
                    amount = transactionAmount,
                    description = remark
                )
                transactionDao.insertTransaction(transactionEntity)
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
    
    // 使用ModalBottomSheet实现底部弹出层
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 创建遮罩层效果
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x02000000))
                .clickable { onDismiss() }
        )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = White,
//            contentColor = DarkGray
        ) {
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
                    Row(
                    ) {
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
                   Box() {
                       Text(
                           text = currentDate,
                           color = Gray,
                           fontSize = 14.sp,
                           modifier = Modifier.clickable {
                               showDatePicker = true
                           }
                       )
                   }
                    Box() {
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
    }
}
    
    // 日期选择器弹窗
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerModal(
        show: Boolean,
        initialDate: String,
        onDismiss: () -> Unit,
        onDateSelected: (String) -> Unit
    ) {
        if (show) {
            // 解析初始日期
            val dateParts = initialDate.split("-")
            val initialYear = dateParts[0].toInt()
            val initialMonth = dateParts[1].toInt()
            val initialDay = dateParts[2].toInt()
            
            // 日期选择器状态
            var selectedYear by remember { mutableStateOf(initialYear) }
            var selectedMonth by remember { mutableStateOf(initialMonth) }
            var selectedDay by remember { mutableStateOf(initialDay) }
            
            // 获取月份天数
            fun getDaysInMonth(year: Int, month: Int): Int {
                return when (month) {
                    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
                    4, 6, 9, 11 -> 30
                    else -> 31
                }
            }
            
            val daysInMonth = getDaysInMonth(selectedYear, selectedMonth)
            
            ModalBottomSheet(
                onDismissRequest = onDismiss,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 标题
                    Text(
                        text = "选择日期",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
                    )
                    
                    // 年份和月份选择
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 年份选择
                        IconButton(onClick = { selectedYear-- }) {
                            Icon(MaterialTheme.icons.arrowLeftFilled, contentDescription = "上一年")
                        }
                        
                        Text(
                            text = "${selectedYear}年",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkGray,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        
                        IconButton(onClick = { selectedYear++ }) {
                            Icon(MaterialTheme.icons.arrowLeftFilled, contentDescription = "下一年")
                        }
                        
                        Spacer(modifier = Modifier.width(32.dp))
                        
                        // 月份选择
                        IconButton(onClick = { 
                            selectedMonth-- 
                            if (selectedMonth < 1) {
                                selectedMonth = 12
                                selectedYear--
                            }
                        }) {
                            Icon(MaterialTheme.icons.arrowLeftFilled, contentDescription = "上一月")
                        }
                        
                        Text(
                            text = "${selectedMonth}月",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkGray,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        
                        IconButton(onClick = { 
                            selectedMonth++ 
                            if (selectedMonth > 12) {
                                selectedMonth = 1
                                selectedYear++
                            }
                        }) {
                            Icon(MaterialTheme.icons.arrowRightFilled, contentDescription = "下一月")
                        }
                    }
                    
                    // 星期标题
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val weekdays = listOf("日", "一", "二", "三", "四", "五", "六")
                        weekdays.forEach {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    // 日期网格
                    Column(modifier = Modifier.padding(vertical = 16.dp)) {
                        // 计算第一天是星期几
                        val calendar = Calendar.getInstance()
                        calendar.set(selectedYear, selectedMonth - 1, 1)
                        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0-6, 0是周日
                        
                        // 创建日期列表
                        val dates = mutableListOf<Int?>()
                        // 添加空白日期
                        repeat(firstDayOfWeek) {
                            dates.add(null)
                        }
                        // 添加实际日期
                        repeat(daysInMonth) {
                            dates.add(it + 1)
                        }
                        
                        // 显示日期网格
                        for (i in 0 until (dates.size + 6) / 7) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                for (j in 0..6) {
                                    val index = i * 7 + j
                                    val date = if (index < dates.size) dates[index] else null
                                    
                                    val isSelected = date == selectedDay
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) PrimaryBlue else 
                                                if (date != null) LightGray else Color.Transparent
                                            )
                                            .clickable {
                                                if (date != null) {
                                                    selectedDay = date
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (date != null) {
                                            Text(
                                                text = "$date",
                                                fontSize = 16.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) White else DarkGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // 确认按钮
                    Button(
                        onClick = {
                            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
                            onDateSelected(formattedDate)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "确定", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
    
    // 时间选择器弹窗
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TimePickerModal(
        show: Boolean,
        initialTime: String,
        onDismiss: () -> Unit,
        onTimeSelected: (String) -> Unit
    ) {
        if (show) {
            // 解析初始时间
            val timeParts = initialTime.split(":")
            val initialHour = timeParts[0].toInt()
            val initialMinute = timeParts[1].toInt()
            val initialSecond = timeParts[2].toInt()
            
            // 时间选择器状态
            var selectedHour by remember { mutableStateOf(initialHour) }
            var selectedMinute by remember { mutableStateOf(initialMinute) }
            var selectedSecond by remember { mutableStateOf(initialSecond) }
            
            ModalBottomSheet(
                onDismissRequest = onDismiss,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 标题
                    Text(
                        text = "选择时间",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
                    )
                    
                    // 时间选择区域
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 小时选择
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { selectedHour = (selectedHour + 23) % 24 }) {
                                Icon(MaterialTheme.icons.arrowUpFilled, contentDescription = "增加小时")
                            }
                            Text(
                                text = String.format("%02d", selectedHour),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            IconButton(onClick = { selectedHour = (selectedHour + 1) % 24 }) {
                                Icon(MaterialTheme.icons.arrowDownFilled, contentDescription = "减少小时")
                            }
                        }
                        
                        Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                        
                        // 分钟选择
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { selectedMinute = (selectedMinute + 59) % 60 }) {
                                Icon(MaterialTheme.icons.arrowUpFilled, contentDescription = "增加分钟")
                            }
                            Text(
                                text = String.format("%02d", selectedMinute),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            IconButton(onClick = { selectedMinute = (selectedMinute + 1) % 60 }) {
                                Icon(MaterialTheme.icons.arrowDownFilled, contentDescription = "减少分钟")
                            }
                        }
                        
                        Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                        
                        // 秒钟选择
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { selectedSecond = (selectedSecond + 59) % 60 }) {
                                Icon(MaterialTheme.icons.arrowUpFilled, contentDescription = "增加秒钟")
                            }
                            Text(
                                text = String.format("%02d", selectedSecond),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            IconButton(onClick = { selectedSecond = (selectedSecond + 1) % 60 }) {
                                Icon(MaterialTheme.icons.arrowDownFilled, contentDescription = "减少秒钟")
                            }
                        }
                    }
                    
                    // 确认按钮
                    Button(
                        onClick = {
                            val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, selectedSecond)
                            onTimeSelected(formattedTime)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(top = 24.dp)
                    ) {
                        Text(text = "确定", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
    
    // 调用日期选择器弹窗
    DatePickerModal(
        show = showDatePicker,
        initialDate = currentDate,
        onDismiss = { showDatePicker = false },
        onDateSelected = { date -> currentDate = date }
    )
    
    // 调用时间选择器弹窗
    TimePickerModal(
        show = showTimePicker,
        initialTime = currentTime,
        onDismiss = { showTimePicker = false },
        onTimeSelected = { time -> currentTime = time }
    )
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



