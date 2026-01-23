package com.yintianyan.tallybook.screens.home.components

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
import com.yintianyan.tallybook.utils.formatAmount
import com.yintianyan.tallybook.utils.formatAmountWithSign
import com.yintianyan.tallybook.theme.*
import com.yintianyan.tallybook.screens.home.components.TransactionGroupHeader
import com.yintianyan.tallybook.screens.home.components.TransactionItem

/**
 * 交易列表组件，显示按日期分组的交易记录
 * 合并了TransactionList和TransactionCard的功能
 * @param transactionsByDate 按日期分组的交易记录
 * @param modifier 修饰符
 * @param onDelete 删除交易记录的回调
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionList(
    transactionsByDate: Map<String, List<Transaction>>,
    modifier: Modifier = Modifier,
    onDelete: (Transaction) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().background(Color(0xFFF2F3F5)), // 加深背景色，增强对比度
        contentPadding = PaddingValues(bottom = 80.dp) // 为底部导航栏和FAB留出空间
    ) {
        // 交易列表
        if (transactionsByDate.isNotEmpty()) {
            // 按日期降序排序
            val sortedTransactions = transactionsByDate.toList().sortedByDescending { it.first }
            
            sortedTransactions.forEachIndexed { index, (date, dateTransactions) ->
                // 在每个区块之前添加间隔
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 吸顶日期头部
                stickyHeader {
                    // 计算该日期的收入和支出
                    val dailyIncome = dateTransactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val dailyExpense = dateTransactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    TransactionGroupHeader(
                        date = date,
                        dailyIncome = dailyIncome,
                        dailyExpense = dailyExpense
                    )
                }
                
                // 交易卡片
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        // 下半部分圆角，上半部分直角，与 Header 拼接
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp) // 扁平化
                    ) {
                        Column {
                            dateTransactions.forEachIndexed { transactionIndex, transaction ->
                                TransactionItem(
                                    transaction = transaction,
                                    isLast = transactionIndex == dateTransactions.size - 1,
                                    onDelete = onDelete
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // ... (空数据UI)
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