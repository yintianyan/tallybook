package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.utils.formatAmount
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.ExpenseText
import com.yintianyan.tallybook.theme.Gray
import com.yintianyan.tallybook.theme.IncomeText
import com.yintianyan.tallybook.theme.White

@Composable
fun TransactionGroupHeader(
    date: String,
    dailyIncome: Double,
    dailyExpense: Double
) {
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
            border = BorderStroke(
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
