package com.yintianyan.tallybook.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 账单数据实体类
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val time: String,
    val category: String,
    val type: String,
    val amount: Double,
    val description: String
)
