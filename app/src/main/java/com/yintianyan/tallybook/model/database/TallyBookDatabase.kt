package com.yintianyan.tallybook.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yintianyan.tallybook.model.dao.TransactionDao
import com.yintianyan.tallybook.model.entity.TransactionEntity

/**
 * 记账本数据库
 */
@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TallyBookDatabase : RoomDatabase() {
    
    // 提供TransactionDao的访问方法
    abstract fun transactionDao(): TransactionDao
    
    companion object {
        // 使用volatile关键字确保INSTANCE变量在多线程环境下的可见性
        @Volatile
        private var INSTANCE: TallyBookDatabase? = null
        
        /**
         * 获取数据库实例（单例模式）
         */
        fun getDatabase(context: Context): TallyBookDatabase {
            // 如果INSTANCE不为null，则返回它
            // 否则，创建一个新的数据库实例
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TallyBookDatabase::class.java,
                    "tally_book_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * 添加示例交易数据
         */
        suspend fun insertSampleData(database: TallyBookDatabase) {
            val dao = database.transactionDao()
            
            // 检查是否已有数据
            if (dao.getAllTransactions().isNotEmpty()) {
                return // 如果已有数据，不重复添加
            }
            
            // 获取当前年月
            val calendar = java.util.Calendar.getInstance()
            val currentYear = calendar.get(java.util.Calendar.YEAR)
            val currentMonth = calendar.get(java.util.Calendar.MONTH) + 1 // 月份从0开始
            
            // 创建示例数据
            val sampleTransactions = listOf(
                // 收入数据
                TransactionEntity(
                    date = String.format("%d-%02d-15", currentYear, currentMonth),
                    time = "10:00",
                    category = "SALARY",
                    type = "INCOME",
                    amount = 15800.00,
                    description = "工资收入"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-20", currentYear, currentMonth),
                    time = "15:30",
                    category = "BONUS",
                    type = "INCOME",
                    amount = 2000.00,
                    description = "奖金收入"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-05", currentYear, currentMonth),
                    time = "08:45",
                    category = "RECEIVE_RED_PACKET",
                    type = "INCOME",
                    amount = 500.00,
                    description = "收到红包"
                ),
                // 支出数据
                TransactionEntity(
                    date = String.format("%d-%02d-16", currentYear, currentMonth),
                    time = "12:30",
                    category = "FOOD",
                    type = "EXPENSE",
                    amount = 85.50,
                    description = "午餐"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-16", currentYear, currentMonth),
                    time = "18:45",
                    category = "FOOD",
                    type = "EXPENSE",
                    amount = 120.00,
                    description = "晚餐"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-17", currentYear, currentMonth),
                    time = "09:15",
                    category = "TRANSPORT",
                    type = "EXPENSE",
                    amount = 25.00,
                    description = "地铁充值"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-18", currentYear, currentMonth),
                    time = "14:20",
                    category = "SHOPPING",
                    type = "EXPENSE",
                    amount = 350.00,
                    description = "购买衣物"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-19", currentYear, currentMonth),
                    time = "19:00",
                    category = "ENTERTAINMENT",
                    type = "EXPENSE",
                    amount = 150.00,
                    description = "电影票"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-20", currentYear, currentMonth),
                    time = "11:00",
                    category = "MEDICAL",
                    type = "EXPENSE",
                    amount = 230.00,
                    description = "药品"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-21", currentYear, currentMonth),
                    time = "16:45",
                    category = "LIVING_BILLS",
                    type = "EXPENSE",
                    amount = 180.00,
                    description = "电费"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-22", currentYear, currentMonth),
                    time = "10:30",
                    category = "SHOPPING",
                    type = "EXPENSE",
                    amount = 520.00,
                    description = "购买日用品"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-23", currentYear, currentMonth),
                    time = "08:20",
                    category = "TRANSPORT",
                    type = "EXPENSE",
                    amount = 30.00,
                    description = "打车"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-24", currentYear, currentMonth),
                    time = "12:15",
                    category = "FOOD",
                    type = "EXPENSE",
                    amount = 95.00,
                    description = "午餐"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-25", currentYear, currentMonth),
                    time = "15:30",
                    category = "EDUCATION",
                    type = "EXPENSE",
                    amount = 800.00,
                    description = "书籍"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-26", currentYear, currentMonth),
                    time = "19:45",
                    category = "FOOD",
                    type = "EXPENSE",
                    amount = 200.00,
                    description = "朋友聚餐"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-27", currentYear, currentMonth),
                    time = "13:20",
                    category = "SPORTS",
                    type = "EXPENSE",
                    amount = 150.00,
                    description = "健身卡"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-28", currentYear, currentMonth),
                    time = "11:50",
                    category = "SHOPPING",
                    type = "EXPENSE",
                    amount = 480.00,
                    description = "购买电子产品"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-29", currentYear, currentMonth),
                    time = "09:30",
                    category = "LIVING_BILLS",
                    type = "EXPENSE",
                    amount = 220.00,
                    description = "水费"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-30", currentYear, currentMonth),
                    time = "17:15",
                    category = "PET",
                    type = "EXPENSE",
                    amount = 120.00,
                    description = "宠物用品"
                ),
                TransactionEntity(
                    date = String.format("%d-%02d-31", currentYear, currentMonth),
                    time = "14:45",
                    category = "BEAUTY",
                    type = "EXPENSE",
                    amount = 380.00,
                    description = "美容护理"
                )
            )
            
            // 插入示例数据
            dao.insertTransactions(sampleTransactions)
        }
    }
}
