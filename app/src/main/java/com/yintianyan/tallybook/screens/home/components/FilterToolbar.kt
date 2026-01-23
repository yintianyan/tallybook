package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.PrimaryBlue
import com.yintianyan.tallybook.theme.White

/**
 * 筛选工具栏组件，显示交易记录标题和筛选按钮
 * @param filterText 当前筛选条件的显示文本
 * @param onFilterClick 筛选按钮的点击事件
 */
@Composable
fun FilterToolbar(
    filterText: String,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = "账单记录",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )

        // 筛选按钮
        Button(
            onClick = onFilterClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = White
            ),
            modifier = Modifier.height(40.dp)
        ) {
            Text(
                text = "筛选: $filterText",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
