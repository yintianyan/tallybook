package com.yintianyan.tallybook.utils

import androidx.compose.ui.graphics.Color
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.theme.*

// 添加扩展函数，统一处理分类颜色映射
fun TransactionCategory.getCategoryColor(): Color {
    return when (this) {
        TransactionCategory.FOOD -> Color(0xFFE0F7FA)
        TransactionCategory.TRANSPORT -> Color(0xFFE8F5E9)
        TransactionCategory.SHOPPING -> Color(0xFFF3E5F5)
        TransactionCategory.ENTERTAINMENT -> Color(0xFFFCE4EC)
        TransactionCategory.EDUCATION -> Color(0xFFFFF8E1)
        TransactionCategory.CLOTHING -> Color(0xFFE3F2FD)
        TransactionCategory.SPORTS -> Color(0xFFE8EAF6)
        TransactionCategory.PET -> Color(0xFFE0E0E0)
        TransactionCategory.MEDICAL -> Color(0xFFEF9A9A)
        TransactionCategory.LIVING_BILLS -> Color(0xFFB39DDB)
        TransactionCategory.RED_PACKET -> Color(0xFFFFE0B2)
        TransactionCategory.CHILDREN -> Color(0xFFC5E1A5)
        TransactionCategory.HOTEL -> Color(0xFFA5D6A7)
        TransactionCategory.BEAUTY -> Color(0xFFFFCCBC)
        TransactionCategory.HUMAN_RELATIONS -> Color(0xFFD1C4E9)
        TransactionCategory.TRANSFER -> Color(0xFFC8E6C9)
        TransactionCategory.SEND_RED_PACKET -> Color(0xFFFFAB91)
        TransactionCategory.INSURANCE -> Color(0xFFB0BEC5)
        TransactionCategory.EXPENSE_OTHER -> Color(0xFFCFD8DC)
        TransactionCategory.SALARY -> Color(0xFFA5D6A7)
        TransactionCategory.BONUS -> Color(0xFFFFE082)
        TransactionCategory.RECEIVE_RED_PACKET -> Color(0xFFFFAB91)
        TransactionCategory.RECEIVE_TRANSFER -> Color(0xFF80CBC4)
        TransactionCategory.OTHER_HUMAN_RELATIONS -> Color(0xFFCE93D8)
        TransactionCategory.INCOME_OTHER -> Color(0xFFB0BEC5)
        TransactionCategory.ALL -> Color(0xFF757575)
    }
}

// 添加扩展函数，统一处理分类图标颜色
fun TransactionCategory.getCategoryIconColor(): Color {
    return when (this) {
        // 支出分类
        TransactionCategory.FOOD -> TagOrange
        TransactionCategory.TRANSPORT -> TagBlue
        TransactionCategory.SHOPPING -> TagPurple
        TransactionCategory.ENTERTAINMENT -> TagYellow
        TransactionCategory.EDUCATION -> TagGreen
        TransactionCategory.CLOTHING -> TagOrange
        TransactionCategory.SPORTS -> TagYellow
        TransactionCategory.PET -> TagPurple
        TransactionCategory.MEDICAL -> TagBlue
        TransactionCategory.LIVING_BILLS -> Gray
        TransactionCategory.RED_PACKET -> ExpenseText
        TransactionCategory.CHILDREN -> TagPurple
        TransactionCategory.HOTEL -> TagBlue
        TransactionCategory.BEAUTY -> TagYellow
        TransactionCategory.HUMAN_RELATIONS -> TagGreen
        TransactionCategory.TRANSFER -> TagBlue
        TransactionCategory.SEND_RED_PACKET -> ExpenseText
        TransactionCategory.INSURANCE -> TagGreen
        TransactionCategory.EXPENSE_OTHER -> Gray
        // 收入分类
        TransactionCategory.SALARY -> IncomeText
        TransactionCategory.BONUS -> TagYellow
        TransactionCategory.RECEIVE_RED_PACKET -> IncomeText
        TransactionCategory.RECEIVE_TRANSFER -> IncomeText
        TransactionCategory.OTHER_HUMAN_RELATIONS -> IncomeText
        TransactionCategory.INCOME_OTHER -> Gray
        // 默认情况
        else -> TagBlue
    }
}

