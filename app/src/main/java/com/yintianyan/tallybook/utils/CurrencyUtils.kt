package com.yintianyan.tallybook.utils

import com.yintianyan.tallybook.routes.TransactionType

/**
 * 金额格式化函数
 */
fun Double.formatAmount(): String {
    return String.format("¥%,.2f", this)
}

/**
 * 带符号的金额格式化函数
 */
fun Double.formatAmountWithSign(type: TransactionType): String {
    val formatted = String.format("%,.2f", this)
    return if (type == TransactionType.INCOME) "+¥$formatted" else "-¥$formatted"
}
