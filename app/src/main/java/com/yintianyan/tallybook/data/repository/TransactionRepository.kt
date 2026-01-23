package com.yintianyan.tallybook.data.repository

import com.yintianyan.tallybook.model.dao.TransactionDao
import com.yintianyan.tallybook.model.entity.TransactionEntity
import com.yintianyan.tallybook.routes.Transaction
import com.yintianyan.tallybook.routes.TransactionCategory
import com.yintianyan.tallybook.routes.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 交易数据仓库接口
 * 定义了所有与交易相关的数据操作
 */
interface TransactionRepository {
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun getTransactionsByDate(date: String): List<Transaction>
    suspend fun getCurrentMonthTransactions(currentMonth: String): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
}

/**
 * 交易数据仓库实现
 */
class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionDao.getAllTransactions().map { it.toTransaction() }
    }

    override suspend fun getTransactionsByDate(date: String): List<Transaction> {
        return transactionDao.getTransactionsByDate(date).map { it.toTransaction() }
    }

    override suspend fun getCurrentMonthTransactions(currentMonth: String): List<Transaction> {
        return transactionDao.getCurrentMonthTransactions(currentMonth).map { it.toTransaction() }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        // 删除时需要 Entity，通常只需 ID
        transactionDao.deleteTransaction(transaction.toEntity())
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }
    
    // 扩展函数：Entity -> Domain Model
    private fun TransactionEntity.toTransaction(): Transaction {
        return Transaction(
            id = id,
            date = date,
            time = time,
            category = TransactionCategory.valueOf(category),
            type = TransactionType.valueOf(type),
            amount = amount,
            description = description
        )
    }

    // 扩展函数：Domain Model -> Entity
    private fun Transaction.toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            date = date,
            time = time,
            category = category.name,
            type = type.name,
            amount = amount,
            description = description
        )
    }
}
