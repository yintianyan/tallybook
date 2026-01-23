# TallyBook - 记账本应用

## 项目概述
TallyBook 是一款基于 Android 平台的个人记账应用，旨在帮助用户轻松管理个人财务。通过简洁直观的界面，用户可以快速记录日常收支，查看分类统计，并掌握个人财务状况。应用采用现代化的 UI 设计和交互方式，提供流畅的用户体验。

### 核心功能
- **快速记账**：支持“记一笔”悬浮按钮，具备智能吸附功能，随手记录收支。
- **收支明细**：按日期分组展示交易记录，清晰明了。
- **统计分析**：提供收支概览、分类统计和月度趋势图表，辅助财务决策。
- **分类管理**：内置丰富的收支分类，支持自定义管理。
- **个人中心**：用户配置和数据管理。

## 技术栈

本项目采用现代 Android 开发技术栈：

- **语言**：[Kotlin](https://kotlinlang.org/) (v2.0.21)
- **UI 框架**：[Jetpack Compose](https://developer.android.com/jetpack/compose) (BOM 2024.10.00)
- **设计系统**：Material Design 3
- **架构模式**：MVVM (Model-View-ViewModel) + Repository Pattern
- **导航**：[Navigation Compose](https://developer.android.com/guide/navigation) (v2.7.7)
- **数据库**：[Room](https://developer.android.com/training/data-storage/room) (v2.6.0)
- **异步处理**：Kotlin Coroutines & Flow
- **依赖注入**：手动依赖注入 (Manual DI)
- **构建工具**：Gradle (AGP v8.13.1)

## 快速开始

### 环境要求
- Android Studio Ladybug | 2024.2.1 或更高版本
- JDK 11 或更高版本
- Android SDK API 34 (UpsideDownCake)

### 运行步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/yintianyan/tallybook.git
   cd tallybook
   ```

2. **打开项目**
   - 启动 Android Studio。
   - 选择 "Open" 并指向项目根目录。

3. **同步依赖**
   - 等待 Gradle Sync 完成，Android Studio 会自动下载所需依赖。

4. **运行应用**
   - 连接 Android 设备或启动模拟器（推荐 API 26+）。
   - 点击工具栏上的 "Run" 按钮 (绿色三角形) 或使用快捷键 `Shift + F10`。

### 构建命令
```bash
# 编译并打包 Debug 版本
./gradlew assembleDebug

# 运行单元测试
./gradlew test

# 运行 UI 测试
./gradlew connectedAndroidTest
```

## 代码规范

本项目遵循 [Android Kotlin 编码规范](https://developer.android.com/kotlin/style-guide) 和 [Jetpack Compose 开发规范](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md)。

### 目录结构
```
app/src/main/java/com/yintianyan/tallybook/
├── components/          # 可复用 UI 组件 (如 DraggableFloatingActionButton)
├── constants/           # 全局常量 (如 CategoryConstants)
├── data/                # 数据层
│   └── repository/      # 数据仓库 (Repository)
├── model/               # 数据模型
│   ├── dao/             # Room DAO
│   ├── database/        # Room Database
│   └── entity/          # 数据库实体
├── routes/              # 导航路由定义
├── screens/             # 业务屏幕 (Feature-based structure)
│   ├── home/            # 首页模块
│   ├── statistics/      # 统计模块
│   └── profile/         # 个人中心模块
├── theme/               # Material3 主题配置 (Color, Type, Theme)
└── utils/               # 工具类 (Extensions, Formatters)
```

### 提交规范
推荐使用 Conventional Commits 格式：
- `feat`: 新功能
- `fix`: 修复 Bug
- `docs`: 文档变更
- `style`: 代码格式调整（不影响逻辑）
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具变动

## 注意事项

- **KSP 增量编译**：为解决缓存冲突，项目在 `build.gradle.kts` 中禁用了 KSP 的增量编译 (`ksp { arg("incremental", "false") }`)。
- **图标资源**：项目使用 `androidx.compose.material.icons:material-icons-extended` 库，部分图标采用了 `AutoMirrored` 变体以支持 RTL 布局。
- **数据库迁移**：修改 `TransactionEntity` 后，需更新数据库版本并提供迁移策略，或者在开发阶段清除应用数据。

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库。
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)。
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)。
4. 推送到分支 (`git push origin feature/AmazingFeature`)。
5. 提交 Pull Request。

请确保在提交前运行 `./gradlew assembleDebug` 验证构建通过，并尽量保持代码风格一致。

## 许可证

本项目采用 [MIT License](LICENSE) 许可证。
