package com.yintianyan.tallybook.screens.homescreen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.screens.homescreen.viewmodel.HomeViewModel
import com.yintianyan.tallybook.screens.homescreen.viewmodel.formatAmount
import com.yintianyan.tallybook.theme.*

/**
 * 余额概览卡片组件，显示本月收入和支出信息
 * @param viewModel HomeViewModel实例，用于获取统计数据
 */
@Composable
fun BalanceOverviewCard(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 收入卡片
        Card(
            modifier = Modifier.weight(0.48f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = IncomeBackground),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "本月收入",
                        fontSize = 14.sp,
                        color = Gray
                    )
                    Text(
                        text = "↑",
                        fontSize = 14.sp,
                        color = IncomeText,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = viewModel.monthlyIncome.formatAmount(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = IncomeText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = "${viewModel.incomeCount}笔收入",
                    fontSize = 12.sp,
                    color = Gray
                )
            }
        }

        // 添加间距
        Spacer(modifier = Modifier.width(16.dp))

        // 支出卡片
        Card(
            modifier = Modifier.weight(0.48f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ExpenseBackground),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
           Column(
               modifier = Modifier.padding(16.dp)
           ) {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
               ) {
                   Text(
                       text = "本月支出",
                       fontSize = 14.sp,
                       color = Gray
                   )
                   Text(
                       text = "↓",
                       fontSize = 14.sp,
                       color = ExpenseText,
                       modifier = Modifier.padding(start = 4.dp)
                   )
               }
               Text(
                   text = viewModel.monthlyExpense.formatAmount(),
                   fontSize = 20.sp,
                   fontWeight = FontWeight.Bold,
                   color = ExpenseText,
                   maxLines = 1,
                   overflow = TextOverflow.Ellipsis,
                   modifier = Modifier
                       .padding(vertical = 8.dp)
               )
               Text(
                   text = "${viewModel.expenseCount}笔支出",
                   fontSize = 12.sp,
                   color = Gray,
               )
           }
        }
    }
}
