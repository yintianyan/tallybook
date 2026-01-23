package com.yintianyan.tallybook.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.theme.*
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = TallyBookDatabase.getDatabase(context)
    val transactionDao = database.transactionDao()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 顶部标题
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "我的",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            }
        }
        
        // 用户信息卡片
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                            .padding(16.dp)
                    ) {
                        Icon(
                            MaterialTheme.icons.profile,
                            contentDescription = "用户头像",
                            tint = White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "用户名",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "user@example.com",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }
        }
        
        // 设置选项卡片
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column {
                    SettingItem(
                        icon = MaterialTheme.icons.settings,
                        title = "设置"
                    )
                    SettingItem(
                        icon = MaterialTheme.icons.info,
                        title = "帮助与反馈"
                    )
                    // 临时删除数据按钮
                    SettingItem(
                        icon = Icons.Default.Delete,
                        title = "清除所有数据（开发用）",
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                transactionDao.deleteAllTransactions()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "所有数据已清除", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
        
        // 应用信息
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "记账本 v1.0.0",
                    fontSize = 12.sp,
                    color = Gray
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = DarkGray
        )
    }
}
