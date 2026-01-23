package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.components.SwipeToRevealItem
import com.yintianyan.tallybook.routes.Transaction
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.utils.formatAmountWithSign
import com.yintianyan.tallybook.utils.getCategoryIconColor
import com.yintianyan.tallybook.theme.*

@Composable
fun TransactionItem(
    transaction: Transaction,
    isLast: Boolean,
    onDelete: (Transaction) -> Unit
) {
    SwipeToRevealItem(
        onDelete = { onDelete(transaction) },
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
                        val tintColor = transaction.category.getCategoryIconColor()
                        
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
    if (!isLast) {
        HorizontalDivider(
            color = Color(0xFFF3F4F6),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
