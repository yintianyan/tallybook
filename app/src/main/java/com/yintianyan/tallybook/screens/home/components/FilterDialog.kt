package com.yintianyan.tallybook.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import com.yintianyan.tallybook.screens.home.HomeViewModel
import com.yintianyan.tallybook.theme.*

import com.yintianyan.tallybook.components.DialogPopup

/**
 * 筛选对话框组件，用于筛选交易记录
 * @param show 是否显示对话框
 * @param selectedType 当前选中的交易类型
 * @param selectedCategory 当前选中的交易分类
 * @param filteredCategories 过滤后的分类列表
 * @param onDismiss 关闭对话框的回调
 * @param onTypeChange 类型变化的回调
 * @param onCategoryChange 分类变化的回调
 * @param onReset 重置筛选的回调
 */
@Composable
fun FilterDialog(
    show: Boolean,
    selectedType: TransactionType,
    selectedCategory: TransactionCategory,
    filteredCategories: List<TransactionCategory>,
    onDismiss: () -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onCategoryChange: (TransactionCategory) -> Unit,
    onReset: () -> Unit
) {
    DialogPopup(
        show = show,
        onDismiss = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = "筛选",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )

                // 重置按钮
                TextButton(onClick = onReset) {
                    Text(
                        text = "重置",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }
        },
        content = {
            Column(modifier = Modifier.padding(4.dp)) {
                // 收支类型筛选
                Text(
                    text = "收支类型",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TypeFilterButton(
                        type = TransactionType.ALL,
                        isSelected = selectedType == TransactionType.ALL,
                        onClick = { onTypeChange(TransactionType.ALL) },
                        modifier = Modifier.weight(1f)
                    )
                    TypeFilterButton(
                        type = TransactionType.INCOME,
                        isSelected = selectedType == TransactionType.INCOME,
                        onClick = { onTypeChange(TransactionType.INCOME) },
                        modifier = Modifier.weight(1f)
                    )
                    TypeFilterButton(
                        type = TransactionType.EXPENSE,
                        isSelected = selectedType == TransactionType.EXPENSE,
                        onClick = { onTypeChange(TransactionType.EXPENSE) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 分类筛选
                Text(
                    text = "分类",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 由于对话框空间有限，我们只显示常用分类
                // 并使用横向滚动来处理
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredCategories.forEach { category ->
                        CategoryFilterButton(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { onCategoryChange(category) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                )
            ) {
                Text(
                    text = "确认",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "取消",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = White
    )
}
