package com.yintianyan.tallybook.routes

/**
 * 导航路由常量
 */
object NavRoutes {
    const val HOME = "home"
    const val STATISTICS = "statistics"
    const val PROFILE = "profile"
}

/**
 * 交易类型枚举
 */
enum class TransactionType {
    ALL, INCOME, EXPENSE
}

/**
 * 交易分类枚举
 */
enum class TransactionCategory {
    // 特殊分类
    ALL,
    
    // 支出分类
    FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, EDUCATION, CLOTHING,
    SPORTS, PET, MEDICAL, LIVING_BILLS, RED_PACKET, CHILDREN,
    HOTEL, BEAUTY, HUMAN_RELATIONS, TRANSFER, SEND_RED_PACKET,
    INSURANCE, EXPENSE_OTHER,
    
    // 收入分类
    SALARY, BONUS, RECEIVE_RED_PACKET, RECEIVE_TRANSFER,
    OTHER_HUMAN_RELATIONS, INCOME_OTHER
}

/**
 * 交易数据类
 */
data class Transaction(
    val id: Int,
    val date: String,
    val time: String,
    val category: TransactionCategory,
    val type: TransactionType,
    val amount: Double,
    val description: String
)
