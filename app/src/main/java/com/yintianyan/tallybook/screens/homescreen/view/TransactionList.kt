package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.routes.Transaction
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.screens.homescreen.viewmodel.formatAmount
import com.yintianyan.tallybook.screens.homescreen.viewmodel.formatAmountWithSign
import com.yintianyan.tallybook.theme.*


/**
 * 交易列表组件，显示按日期分组的交易记录
 * 合并了TransactionList和TransactionCard的功能
 * @param transactionsByDate 按日期分组的交易记录
 * @param modifier 修饰符
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(
    transactionsByDate: Map<String, List<Transaction>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 80.dp) // 为底部导航栏和FAB留出空间
    ) {
        // 交易列表
        if (transactionsByDate.isNotEmpty()) {
            // 按日期降序排序
            val sortedTransactions = transactionsByDate.toList().sortedByDescending { it.first }
            
            sortedTransactions.forEach { (date, dateTransactions) ->
                // 吸顶日期头部
                stickyHeader {
                    // 计算该日期的收入和支出
                    val dailyIncome = dateTransactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val dailyExpense = dateTransactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFFF9FAFB))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp
                                ))
                                .padding(16.dp, 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = date,
                                fontSize = 14.sp,
                                color = DarkGray,
                                fontWeight = FontWeight.Normal
                            )
                            Row {
                                Text(
                                    text = "收入 ${dailyIncome.formatAmount()}",
                                    fontSize = 12.sp,
                                    color = IncomeText,
                                    fontWeight = FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "支出 ${dailyExpense.formatAmount()}",
                                    fontSize = 12.sp,
                                    color = ExpenseText,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                
                // 交易卡片，直接合并到TransactionList中
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            dateTransactions.forEachIndexed { transactionIndex, transaction ->
                                // 单个交易记录项
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // 分类图标
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(LightGray)
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            // 使用category.icon直接获取图标
                                            val tintColor = when (transaction.category) {
                                                // 支出分类
                                                TransactionCategory.FOOD -> TagOrange
                                                TransactionCategory.TRANSPORT -> TagBlue
                                                TransactionCategory.SHOPPING -> TagPurple
                                                TransactionCategory.ENTERTAINMENT -> TagYellow
                                                TransactionCategory.EDUCATION -> TagGreen
                                                TransactionCategory.CLOTHING -> TagOrange
                                                TransactionCategory.SPORTS -> TagYellow
                                                TransactionCategory.PET -> TagPurple
                                                TransactionCategory.MEDICAL -> TagBlue
                                                TransactionCategory.LIVING_BILLS -> Gray
                                                TransactionCategory.RED_PACKET -> ExpenseText
                                                TransactionCategory.CHILDREN -> TagPurple
                                                TransactionCategory.HOTEL -> TagBlue
                                                TransactionCategory.BEAUTY -> TagYellow
                                                TransactionCategory.HUMAN_RELATIONS -> TagGreen
                                                TransactionCategory.TRANSFER -> TagBlue
                                                TransactionCategory.SEND_RED_PACKET -> ExpenseText
                                                TransactionCategory.INSURANCE -> TagGreen
                                                TransactionCategory.EXPENSE_OTHER -> Gray
                                                // 收入分类
                                                TransactionCategory.SALARY -> IncomeText
                                                TransactionCategory.BONUS -> TagYellow
                                                TransactionCategory.RECEIVE_RED_PACKET -> IncomeText
                                                TransactionCategory.RECEIVE_TRANSFER -> IncomeText
                                                TransactionCategory.OTHER_HUMAN_RELATIONS -> IncomeText
                                                TransactionCategory.INCOME_OTHER -> Gray
                                                // 默认情况
                                                else -> TagBlue
                                            }
                                            
                                            Icon(
                                                transaction.category.icon,
                                                contentDescription = transaction.description,
                                                tint = tintColor
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = transaction.description,
                                                fontSize = 16.sp,
                                                color = DarkGray
                                            )
                                            Text(
                                                text = transaction.time,
                                                fontSize = 12.sp,
                                                color = Gray
                                            )
                                        }
                                    }

                                    // 金额
                                    Text(
                                        text = transaction.amount.formatAmountWithSign(transaction.type),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (transaction.type == TransactionType.INCOME) IncomeText else ExpenseText
                                    )
                                }
                                
                                // 在最后一个交易项后不添加分割线
                                if (transactionIndex < dateTransactions.size - 1) {
                                    HorizontalDivider(
                                        color = Color(0xFFF3F4F6),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // 空数据UI
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = MaterialTheme.icons.shoppingCart,
                        contentDescription = "暂无交易记录",
                        tint = Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无交易记录",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角的 + 添加第一笔交易",
                        fontSize = 14.sp,
                        color = LightGray
                    )
                }
            }
        }
    }
}