package com.yintianyan.tallybook.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.theme.PrimaryBlue
import com.yintianyan.tallybook.theme.DarkGray
import com.yintianyan.tallybook.theme.Gray
import com.yintianyan.tallybook.theme.TagOrange
import com.yintianyan.tallybook.theme.White

/**
 * 自定义数字键盘组件
 * @param onKeyPress 按键回调函数，参数为按键的字符串值
 */
@Composable
fun NumberKeyboard(
    onKeyPress: (String) -> Unit
) {
    Row(modifier = Modifier.height(224.dp)) {

        // 左侧数字区域
        Column(
            modifier = Modifier
                .weight(3f),
            verticalArrangement = Arrangement.Center
        ) {
            // 第一行：1, 2, 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                NumberKey("1", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("2", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("3", onKeyPress, modifier = Modifier.weight(1f))
            }

            // 第二行：4, 5, 6
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                NumberKey("4", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("5", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("6", onKeyPress, modifier = Modifier.weight(1f))
            }

            // 第三行：7, 8, 9
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                NumberKey("7", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("8", onKeyPress, modifier = Modifier.weight(1f))
                NumberKey("9", onKeyPress, modifier = Modifier.weight(1f))
            }

            // 第四行：0, .
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                // 0 (占据2个位置)
                NumberKey("0", onKeyPress, modifier = Modifier.weight(2f))
                // .
                NumberKey(".", onKeyPress, modifier = Modifier.weight(1f))
            }
        }

        // 右侧操作区域：删除键和确认键
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // 删除键
            NumberKey("X", onKeyPress, isDelete = true, modifier = Modifier.weight(1f).fillMaxWidth())

            // 确认键 (weight为3f)
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .background(PrimaryBlue)
                    .border(0.5.dp, Color(0xFFEEEEEE))
                    .clickable { onKeyPress("OK") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "确定",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

/**
 * 数字键盘按键组件
 */
@Composable
private fun NumberKey(
    text: String,
    onPress: (String) -> Unit,
    isDelete: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isDelete) Color(0xFFF3F4F6) else Color.Transparent
    val textColor = if (isDelete) White else DarkGray

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor)
            .border(0.5.dp, Color(0xFFEEEEEE))
            .clickable { onPress(text) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}