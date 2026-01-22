# TallyBook - 记账本应用

## 项目结构

```
app/src/main/java/com/yintianyan/tallybook/
├── MainAc          tivity.kt              # 应用主入口
├── constants/                   # 常量包
│   └── CategoryConstants.kt     # 交易分类常量定义
└── ui/                          # UI相关包
    ├── components/              # UI组件
    │   └── BottomNavigation.kt  # 底部导航组件
    ├── screens/                 # 屏幕页面
    │   ├── HomeScreen.kt        # 首页屏幕
    │   ├── ProfileScreen.kt     # 个人中心屏幕
    │   └── StatisticsScreen.kt  # 统计屏幕
    ├── theme/                   # 主题相关
    │   ├── Color.kt             # 颜色主题定义
    │   ├── Theme.kt             # 应用主题
    │   └── Type.kt              # 字体样式定义
    └── NavRoutes.kt             # 导航路由和数据模型
```

## 文件说明

### MainActivity.kt
应用的主入口文件，负责设置应用的主题和导航结构。
- 初始化Jetpack Compose环境
- 设置应用主题
- 配置导航宿主(NavHost)

### constants/CategoryConstants.kt
集中管理交易分类的常量定义，将分类文本与业务逻辑解耦。
- 定义支出分类常量（餐饮、交通、购物、娱乐等）
- 定义收入分类常量（工资、奖金、收红包等）
- 提供分类枚举与显示名称的映射关系

### ui/components/BottomNavigation.kt
底部导航栏组件，用于在不同屏幕间切换。
- 实现底部导航栏UI
- 提供三个导航选项：明细、统计、我的
- 与导航控制器(NavController)集成

### ui/screens/HomeScreen.kt
应用的首页屏幕，展示交易记录和分类筛选功能。
- 展示本月支出和收入卡片
- 提供分类筛选标签（支持横向滚动）
- 按日期分组显示交易记录
- 浮动添加按钮

### ui/screens/ProfileScreen.kt
个人中心屏幕，展示用户信息和设置选项。

### ui/screens/StatisticsScreen.kt
统计分析屏幕，展示收支统计数据。

### ui/theme/Color.kt
定义应用的颜色主题，集中管理所有颜色值。
- 主色调定义
- 辅助色定义
- 背景色和文本色定义

### ui/theme/Theme.kt
应用的主题配置文件，整合颜色、字体等主题元素。
- 设置Material3主题
- 配置颜色方案
- 配置排版方案

### ui/theme/Type.kt
定义应用的字体样式，集中管理所有文本样式。
- 标题样式
- 正文样式
- 按钮文本样式

### ui/NavRoutes.kt
定义应用的导航路由和数据模型。
- 导航路由枚举
- 交易数据模型
- 交易分类枚举

## 技术栈

- Jetpack Compose - 声明式UI框架
- Material3 - UI组件库
- Compose Navigation - 导航组件

## 开发说明

### 分类管理
所有交易分类定义在`CategoryConstants.kt`中，支持在设置中进行管理。

### 导航结构
应用包含三个主要页面：
- 首页(HomeScreen) - 查看交易记录
- 统计(StatisticsScreen) - 查看收支统计
- 我的(ProfileScreen) - 个人设置

### UI组件
- 底部导航栏(BottomNavigation)
- 分类筛选标签
- 交易记录卡片
- 浮动添加按钮(FloatingActionButton)
