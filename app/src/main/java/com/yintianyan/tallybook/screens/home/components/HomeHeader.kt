// HomeHeader.kt
// 首页顶部标题栏组件

package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.LightGray
import com.yintianyan.tallybook.theme.White
import com.yintianyan.tallybook.theme.*

/**
 * 首页顶部标题栏组件
 * @param selectedMonth 当前选中的月份显示文本（例如："2025年12月"）
 * @param onMonthClick 点击月份选择器的回调
 */
@Composable
fun HomeHeader(
    selectedMonth: String,
    onMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "明细",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(LightGray)
                .clickable { onMonthClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedMonth,
                    fontSize = 16.sp,
                    color = DarkGray,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    MaterialTheme.icons.arrowDownFilled,
                    contentDescription = "选择月份",
                    tint = DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
