package com.yintianyan.tallybook.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

/**
 * DatePicker组件单元测试
 */
class DatePickerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * 测试年份选择模式
     */
    @Test
    fun testYearPickerMode() {
        val testDate = LocalDate.of(2023, 1, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.YEAR,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证默认年份显示
        composeTestRule.onNodeWithText("2023年").assertExists()
    }

    /**
     * 测试年月选择模式
     */
    @Test
    fun testMonthPickerMode() {
        val testDate = LocalDate.of(2023, 1, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.MONTH,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证默认年月显示
        composeTestRule.onNodeWithText("2023年").assertExists()
        composeTestRule.onNodeWithText("01月").assertExists()
    }

    /**
     * 测试年月日选择模式
     */
    @Test
    fun testDatePickerMode() {
        val testDate = LocalDate.of(2023, 1, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.DATE,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证默认年月日显示
        composeTestRule.onNodeWithText("2023年").assertExists()
        composeTestRule.onNodeWithText("01月").assertExists()
        composeTestRule.onNodeWithText("01日").assertExists()
    }

    /**
     * 测试闰年处理
     */
    @Test
    fun testLeapYearHandling() {
        val testDate = LocalDate.of(2024, 2, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.DATE,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证UI渲染正常
        composeTestRule.onNodeWithText("2024年").assertExists()
        composeTestRule.onNodeWithText("02月").assertExists()
    }

    /**
     * 测试平年处理
     */
    @Test
    fun testNonLeapYearHandling() {
        val testDate = LocalDate.of(2023, 2, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.DATE,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证UI渲染正常
        composeTestRule.onNodeWithText("2023年").assertExists()
        composeTestRule.onNodeWithText("02月").assertExists()
    }

    /**
     * 测试月份天数变化
     */
    @Test
    fun testMonthDaysChange() {
        val testDate = LocalDate.of(2023, 4, 1)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.DATE,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证UI渲染正常
        composeTestRule.onNodeWithText("2023年").assertExists()
        composeTestRule.onNodeWithText("04月").assertExists()
    }

    /**
     * 测试defaultValue属性
     */
    @Test
    fun testDefaultValue() {
        val testDate = LocalDate.of(2025, 6, 15)

        composeTestRule.setContent {
            DatePicker(
                mode = DatePickerMode.DATE,
                defaultValue = testDate,
                onChange = {}
            )
        }

        // 验证UI渲染正常
        composeTestRule.onNodeWithText("2025年").assertExists()
        composeTestRule.onNodeWithText("06月").assertExists()
    }
}
