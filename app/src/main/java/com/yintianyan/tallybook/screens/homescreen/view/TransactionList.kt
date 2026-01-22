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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete


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
                    // ... (保持不变)
                    // 计算该日期的收入和支出
                    val dailyIncome = dateTransactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val dailyExpense = dateTransactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    // 外层 Box 用于遮挡下方滚动内容，背景色需与列表背景一致
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF2F3F5)) // 必须不透明且与列表背景一致
                    ) {
                        // 使用 Surface 实现一体化卡片头部的视觉效果
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp), // 与卡片对齐
                            color = White,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                            // 添加底部边框线，增强分隔感
                            border = androidx.compose.foundation.BorderStroke(
                                width = 0.5.dp,
                                color = Color(0xFFF3F4F6)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = date,
                                    fontSize = 14.sp,
                                    color = DarkGray,
                                    fontWeight = FontWeight.Medium
                                )
                                Row {
                                    if (dailyIncome > 0) {
                                        Text(
                                            text = "收 ${dailyIncome.formatAmount()}",
                                            fontSize = 12.sp,
                                            color = Gray,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }
                                    if (dailyExpense > 0) {
                                        Text(
                                            text = "支 ${dailyExpense.formatAmount()}",
                                            fontSize = 12.sp,
                                            color = Gray,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                                // SwipeToDismiss State
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = {
                                        if (it == SwipeToDismissBoxValue.EndToStart) {
                                            onDelete(transaction)
                                            // 返回 false 以防止自动消失，等待确认弹窗结果
                                            // 或者如果要在确认前重置状态，可以在 onDelete 内部处理，或者由外部控制列表刷新
                                            // 为了视觉效果，我们暂时返回 false，由上层处理删除逻辑后刷新列表
                                            // 如果这里返回 true，列表项会立即消失，如果弹窗取消则需要恢复
                                            // 鉴于需要弹窗确认，我们返回 false，让上层去处理弹窗，
                                            // 并在弹窗确认后删除数据。
                                            // 但是返回 false 会导致滑动立即回弹，体验不好。
                                            // 通常做法：返回 false 并显示 Dialog。
                                            // 或者：允许 dismiss，但数据不删，Dialog Cancel 时恢复。
                                            // 考虑到简单性，我们返回 false，并在 onDelete 中显示 Dialog。
                                            // 但是这样用户体验是：滑到头 -> 松手 -> 马上弹回 -> 弹窗显示。
                                            // 这有点怪。
                                            // 更好的做法：使用 SwipeToDismissBoxValue.EndToStart 触发，
                                            // 如果返回 true，则保持 Dismissed 状态？
                                            // Compose 的 SwipeToDismissBox 如果 confirmValueChange 返回 true，
                                            // 它会 snap 到 anchor。
                                            // 我们试试返回 true，如果用户取消删除，我们需要重置 state。
                                            // 但在 LazyColumn 中重置 state 比较复杂。
                                            // 让我们先简单处理：返回 false，触发 onDelete。
                                            false 
                                        } else {
                                            false
                                        }
                                    }
                                )

                                SwipeToDismissBox(
                                    state = dismissState,
                                    backgroundContent = {
                                        val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                            Color.Red
                                        } else {
                                            Color.Transparent
                                        }
                                        
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "删除",
                                                tint = White
                                            )
                                        }
                                    },
                                    content = {
                                        // 单个交易记录项
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(White) // 必须设置背景色，否则滑动时透明
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // ... (Row content)
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
                                    }
                                )
                                
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