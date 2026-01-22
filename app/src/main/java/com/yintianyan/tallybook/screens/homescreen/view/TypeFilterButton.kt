package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.theme.*

/**
 * 交易类型筛选按钮组件
 * 
 * @param type 交易类型（收入、支出或全部）
 * @param isSelected 是否被选中
 * @param onClick 点击事件回调
 * @param modifier 修饰符
 */
@Composable
fun TypeFilterButton(
    type: TransactionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PrimaryBlue else White
    val textColor = if (isSelected) White else DarkGray
    val borderColor = if (isSelected) PrimaryBlue else UnselectedTagBackground
    
    val displayText = when (type) {
        TransactionType.ALL -> "全部"
        TransactionType.INCOME -> "收入"
        TransactionType.EXPENSE -> "支出"
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}
