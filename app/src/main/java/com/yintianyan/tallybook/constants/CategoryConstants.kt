package com.yintianyan.tallybook.constants

import com.yintianyan.tallybook.routes.TransactionCategory

/**
 * 分类常量定义
 */
object CategoryConstants {
    
    // 支出分类
    const val CATEGORY_EXPENSE_FOOD = "餐饮"
    const val CATEGORY_EXPENSE_TRANSPORT = "交通"
    const val CATEGORY_EXPENSE_SHOPPING = "购物"
    const val CATEGORY_EXPENSE_ENTERTAINMENT = "娱乐"
    const val CATEGORY_EXPENSE_EDUCATION = "教育"
    const val CATEGORY_EXPENSE_CLOTHING = "服饰"
    const val CATEGORY_EXPENSE_SPORTS = "运动"
    const val CATEGORY_EXPENSE_PET = "宠物"
    const val CATEGORY_EXPENSE_MEDICAL = "医疗"
    const val CATEGORY_EXPENSE_LIVING_BILLS = "生活缴费"
    const val CATEGORY_EXPENSE_RED_PACKET = "红包"
    const val CATEGORY_EXPENSE_CHILDREN = "亲子"
    const val CATEGORY_EXPENSE_HOTEL = "酒店"
    const val CATEGORY_EXPENSE_BEAUTY = "服饰美容"
    const val CATEGORY_EXPENSE_HUMAN_RELATIONS = "其他人情"
    const val CATEGORY_EXPENSE_TRANSFER = "转账"
    const val CATEGORY_EXPENSE_SEND_RED_PACKET = "发红包"
    const val CATEGORY_EXPENSE_INSURANCE = "保险"
    const val CATEGORY_EXPENSE_OTHER = "其他"
    
    // 收入分类
    const val CATEGORY_INCOME_SALARY = "工资"
    const val CATEGORY_INCOME_BONUS = "奖金"
    const val CATEGORY_INCOME_RECEIVE_RED_PACKET = "收红包"
    const val CATEGORY_INCOME_RECEIVE_TRANSFER = "收转账"
    const val CATEGORY_INCOME_OTHER_HUMAN_RELATIONS = "其他人情"
    const val CATEGORY_INCOME_OTHER = "其他"
    
    // 所有分类映射
    val CATEGORY_DISPLAY_NAME_MAP = mapOf(
        // 支出分类
        TransactionCategory.FOOD to CATEGORY_EXPENSE_FOOD,
        TransactionCategory.TRANSPORT to CATEGORY_EXPENSE_TRANSPORT,
        TransactionCategory.SHOPPING to CATEGORY_EXPENSE_SHOPPING,
        TransactionCategory.ENTERTAINMENT to CATEGORY_EXPENSE_ENTERTAINMENT,
        TransactionCategory.EDUCATION to CATEGORY_EXPENSE_EDUCATION,
        TransactionCategory.CLOTHING to CATEGORY_EXPENSE_CLOTHING,
        TransactionCategory.SPORTS to CATEGORY_EXPENSE_SPORTS,
        TransactionCategory.PET to CATEGORY_EXPENSE_PET,
        TransactionCategory.MEDICAL to CATEGORY_EXPENSE_MEDICAL,
        TransactionCategory.LIVING_BILLS to CATEGORY_EXPENSE_LIVING_BILLS,
        TransactionCategory.RED_PACKET to CATEGORY_EXPENSE_RED_PACKET,
        TransactionCategory.CHILDREN to CATEGORY_EXPENSE_CHILDREN,
        TransactionCategory.HOTEL to CATEGORY_EXPENSE_HOTEL,
        TransactionCategory.BEAUTY to CATEGORY_EXPENSE_BEAUTY,
        TransactionCategory.HUMAN_RELATIONS to CATEGORY_EXPENSE_HUMAN_RELATIONS,
        TransactionCategory.TRANSFER to CATEGORY_EXPENSE_TRANSFER,
        TransactionCategory.SEND_RED_PACKET to CATEGORY_EXPENSE_SEND_RED_PACKET,
        TransactionCategory.INSURANCE to CATEGORY_EXPENSE_INSURANCE,
        TransactionCategory.EXPENSE_OTHER to CATEGORY_EXPENSE_OTHER,
        
        // 收入分类
        TransactionCategory.SALARY to CATEGORY_INCOME_SALARY,
        TransactionCategory.BONUS to CATEGORY_INCOME_BONUS,
        TransactionCategory.RECEIVE_RED_PACKET to CATEGORY_INCOME_RECEIVE_RED_PACKET,
        TransactionCategory.RECEIVE_TRANSFER to CATEGORY_INCOME_RECEIVE_TRANSFER,
        TransactionCategory.OTHER_HUMAN_RELATIONS to CATEGORY_INCOME_OTHER_HUMAN_RELATIONS,
        TransactionCategory.INCOME_OTHER to CATEGORY_INCOME_OTHER,
        
        // 特殊分类
        TransactionCategory.ALL to "全部"
    )
}
