package com.yintianyan.tallybook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme


import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yintianyan.tallybook.routes.NavRoutes
import com.yintianyan.tallybook.theme.*

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(LightGray)
            .padding(horizontal = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            icon = MaterialTheme.icons.home,
            label = "明细",
            route = NavRoutes.HOME,
            isSelected = currentRoute == NavRoutes.HOME,
            onClick = onNavigate
        )
        NavigationItem(
            icon = MaterialTheme.icons.statistics,
            label = "统计",
            route = NavRoutes.STATISTICS,
            isSelected = currentRoute == NavRoutes.STATISTICS,
            onClick = onNavigate
        )
        NavigationItem(
            icon = MaterialTheme.icons.profile,
            label = "我的",
            route = NavRoutes.PROFILE,
            isSelected = currentRoute == NavRoutes.PROFILE,
            onClick = onNavigate
        )
    }
}

@Composable
fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    route: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    val color = if (isSelected) PrimaryBlue else Gray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(56.dp)
            .padding(8.dp)
            .clickable(onClick = { onClick(route) })
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = color,
                fontWeight = fontWeight
            )
        }
    }
}
