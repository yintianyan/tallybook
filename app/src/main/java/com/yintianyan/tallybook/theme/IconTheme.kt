package com.yintianyan.tallybook.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.yintianyan.tallybook.routes.TransactionCategory

/**
 * 图标主题扩展，集中管理应用中所有使用的图标
 */
val MaterialTheme.icons: AppIcons
    @Composable
    get() = AppIcons

/**
 * 应用中使用的所有图标集合，按功能模块分组
 */
object AppIcons {
    // 底部导航栏图标
    object Navigation {
        val home = Icons.AutoMirrored.Outlined.List
        val statistics = Icons.AutoMirrored.Outlined.List
        val profile = Icons.Outlined.AccountCircle
    }
    
    // 通用图标
    object Common {
        val close = Icons.Default.Close
        val edit = Icons.Default.Edit
        val add = Icons.Outlined.Add
        val moreVert = Icons.Outlined.MoreVert
        val shoppingCart = Icons.Default.Add
    }
    
    // 月份选择器图标
    object MonthPicker {
        val arrowLeft = Icons.AutoMirrored.Filled.KeyboardArrowLeft
        val arrowRight = Icons.AutoMirrored.Filled.KeyboardArrowRight
        val arrowLeftFilled = Icons.AutoMirrored.Filled.KeyboardArrowLeft
        val arrowRightFilled = Icons.AutoMirrored.Filled.KeyboardArrowRight
        val arrowUpFilled = Icons.Default.KeyboardArrowUp
        val arrowDownFilled = Icons.Default.KeyboardArrowDown
    }
    
    // 分类图标
    object Categories {
        // 支出分类
        object Expense {
            val food = Icons.Default.Restaurant
            val transport = Icons.Default.DirectionsCar
            val shopping = Icons.Default.ShoppingCart
            val entertainment = Icons.Default.Movie
            val education = Icons.Default.School
            val clothing = Icons.Default.Checkroom
            val sports = Icons.Default.SportsBasketball
            val pet = Icons.Default.Pets
            val medical = Icons.Default.LocalHospital
            val livingBills = Icons.Default.Receipt
            val redPacket = Icons.Default.CardGiftcard
            val children = Icons.Default.ChildCare
            val hotel = Icons.Default.Hotel
            val beauty = Icons.Default.Face
            val humanRelations = Icons.Default.People
            val transfer = Icons.AutoMirrored.Filled.Sort
            val sendRedPacket = Icons.AutoMirrored.Filled.Send
            val insurance = Icons.Default.Security
            val other = Icons.AutoMirrored.Filled.List
        }
        
        // 收入分类
        object Income {
            val salary = Icons.Default.AccountBalanceWallet
            val bonus = Icons.Default.AttachMoney
            val receiveRedPacket = Icons.Default.CardGiftcard
            val receiveTransfer = Icons.AutoMirrored.Filled.Sort
            val otherHumanRelations = Icons.Default.People
            val other = Icons.Default.AddCircleOutline
        }
    }
    
    // 其他常用图标
    object Misc {
        val person = Icons.Outlined.Person
        val star = Icons.Outlined.Star
        val homeFilled = Icons.Default.Home
        val starFilled = Icons.Default.Star
        val search = Icons.Default.Search
        val share = Icons.Default.Share
        val email = Icons.Default.Email
        val phone = Icons.Default.Phone
        val favorite = Icons.Default.Favorite
        val check = Icons.Default.Check
        val menu = Icons.Default.Menu
        val settings = Icons.Default.Settings
        val info = Icons.Outlined.Info
    }
    
    // 快捷方式 - 保持与旧路径的兼容性
    val home = Navigation.home
    val statistics = Navigation.statistics
    val profile = Navigation.profile
    
    val close = Common.close
    val edit = Common.edit
    val add = Common.add
    val moreVert = Common.moreVert
    val shoppingCart = Common.shoppingCart
    
    val arrowLeft = MonthPicker.arrowLeft
    val arrowRight = MonthPicker.arrowRight
    val arrowLeftFilled = MonthPicker.arrowLeftFilled
    val arrowRightFilled = MonthPicker.arrowRightFilled
    val arrowUpFilled = MonthPicker.arrowUpFilled
    val arrowDownFilled = MonthPicker.arrowDownFilled
    
    // 分类图标快捷方式
    val food = Categories.Expense.food
    val transport = Categories.Expense.transport
    val shopping = Categories.Expense.shopping
    val entertainment = Categories.Expense.entertainment
    val education = Categories.Expense.education
    val clothing = Categories.Expense.clothing
    val sports = Categories.Expense.sports
    val pet = Categories.Expense.pet
    val medical = Categories.Expense.medical
    val livingBills = Categories.Expense.livingBills
    val redPacket = Categories.Expense.redPacket
    val children = Categories.Expense.children
    val hotel = Categories.Expense.hotel
    val beauty = Categories.Expense.beauty
    val humanRelations = Categories.Expense.humanRelations
    val transfer = Categories.Expense.transfer
    val sendRedPacket = Categories.Expense.sendRedPacket
    val insurance = Categories.Expense.insurance
    val expenseOther = Categories.Expense.other
    
    val salary = Categories.Income.salary
    val bonus = Categories.Income.bonus
    val receiveRedPacket = Categories.Income.receiveRedPacket
    val receiveTransfer = Categories.Income.receiveTransfer
    val otherHumanRelations = Categories.Income.otherHumanRelations
    val incomeOther = Categories.Income.other
    
    val person = Misc.person
    val star = Misc.star
    val homeFilled = Misc.homeFilled
    val starFilled = Misc.starFilled
    val search = Misc.search
    val share = Misc.share
    val email = Misc.email
    val phone = Misc.phone
    val favorite = Misc.favorite
    val check = Misc.check
    val menu = Misc.menu
    val settings = Misc.settings
    val info = Misc.info
}

/**
 * TransactionCategory的扩展函数，用于获取对应的图标
 */
val TransactionCategory.icon: ImageVector
    get() = AppIcons.categoryIconMap[this] ?: AppIcons.Misc.info

/**
 * 扩展AppIcons，添加分类图标映射表
 */
val AppIcons.categoryIconMap: Map<TransactionCategory, ImageVector>
    get() = mapOf(
        // 支出分类
        TransactionCategory.FOOD to AppIcons.Categories.Expense.food,
        TransactionCategory.TRANSPORT to AppIcons.Categories.Expense.transport,
        TransactionCategory.SHOPPING to AppIcons.Categories.Expense.shopping,
        TransactionCategory.ENTERTAINMENT to AppIcons.Categories.Expense.entertainment,
        TransactionCategory.EDUCATION to AppIcons.Categories.Expense.education,
        TransactionCategory.CLOTHING to AppIcons.Categories.Expense.clothing,
        TransactionCategory.SPORTS to AppIcons.Categories.Expense.sports,
        TransactionCategory.PET to AppIcons.Categories.Expense.pet,
        TransactionCategory.MEDICAL to AppIcons.Categories.Expense.medical,
        TransactionCategory.LIVING_BILLS to AppIcons.Categories.Expense.livingBills,
        TransactionCategory.RED_PACKET to AppIcons.Categories.Expense.redPacket,
        TransactionCategory.CHILDREN to AppIcons.Categories.Expense.children,
        TransactionCategory.HOTEL to AppIcons.Categories.Expense.hotel,
        TransactionCategory.BEAUTY to AppIcons.Categories.Expense.beauty,
        TransactionCategory.HUMAN_RELATIONS to AppIcons.Categories.Expense.humanRelations,
        TransactionCategory.TRANSFER to AppIcons.Categories.Expense.transfer,
        TransactionCategory.SEND_RED_PACKET to AppIcons.Categories.Expense.sendRedPacket,
        TransactionCategory.INSURANCE to AppIcons.Categories.Expense.insurance,
        TransactionCategory.EXPENSE_OTHER to AppIcons.Categories.Expense.other,
        
        // 收入分类
        TransactionCategory.SALARY to AppIcons.Categories.Income.salary,
        TransactionCategory.BONUS to AppIcons.Categories.Income.bonus,
        TransactionCategory.RECEIVE_RED_PACKET to AppIcons.Categories.Income.receiveRedPacket,
        TransactionCategory.RECEIVE_TRANSFER to AppIcons.Categories.Income.receiveTransfer,
        TransactionCategory.OTHER_HUMAN_RELATIONS to AppIcons.Categories.Income.otherHumanRelations,
        TransactionCategory.INCOME_OTHER to AppIcons.Categories.Income.other,
        
        // 特殊分类
        TransactionCategory.ALL to AppIcons.Misc.info
    )
