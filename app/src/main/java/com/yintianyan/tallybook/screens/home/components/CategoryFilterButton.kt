package com.yintianyan.tallybook.screens.home.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.constants.CategoryConstants
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.theme.*

/**
 * 交易分类筛选按钮组件
 * 
 * @param category 交易分类
 * @param isSelected 是否被选中
 * @param onClick 点击事件回调
 */
@Composable
fun CategoryFilterButton(
    category: TransactionCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PrimaryBlue else White
    val textColor = if (isSelected) White else DarkGray
    val borderColor = if (isSelected) PrimaryBlue else UnselectedTagBackground
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = CategoryConstants.CATEGORY_DISPLAY_NAME_MAP[category] ?: "其他",
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
