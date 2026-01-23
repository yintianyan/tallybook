package com.yintianyan.tallybook.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yintianyan.tallybook.model.entity.TransactionEntity

/**
 * 账单数据访问对象
 */
@Dao
interface TransactionDao {
    
    /**
     * 插入一条交易记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    
    /**
     * 插入多条交易记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)
    
    /**
     * 查询所有交易记录
     */
    @Query("SELECT * FROM transactions ORDER BY date DESC, time DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>
    
    /**
     * 根据类型查询交易记录
     */
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC, time DESC")
    suspend fun getTransactionsByType(type: String): List<TransactionEntity>
    
    /**
     * 根据日期查询交易记录
     */
    @Query("SELECT * FROM transactions WHERE date = :date ORDER BY time DESC")
    suspend fun getTransactionsByDate(date: String): List<TransactionEntity>
    
    /**
     * 根据分类查询交易记录
     */
    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC, time DESC")
    suspend fun getTransactionsByCategory(category: String): List<TransactionEntity>
    
    /**
     * 根据ID查询交易记录
     */
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?
    
    /**
     * 删除交易记录
     */
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    /**
     * 更新交易记录
     */
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    /**
     * 获取指定日期范围内的交易记录
     */
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, time DESC")
    suspend fun getTransactionsByDateRange(startDate: String, endDate: String): List<TransactionEntity>
    
    /**
     * 根据类型和分类查询交易记录
     */
    @Query("SELECT * FROM transactions WHERE type = :type AND category = :category ORDER BY date DESC, time DESC")
    suspend fun getTransactionsByTypeAndCategory(type: String, category: String): List<TransactionEntity>
    
    /**
     * 获取本月所有交易记录
     */
    @Query("SELECT * FROM transactions WHERE date LIKE :currentMonth || '%' ORDER BY date DESC, time DESC")
    suspend fun getCurrentMonthTransactions(currentMonth: String): List<TransactionEntity>
    
    /**
     * 删除所有交易记录
     */
    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
