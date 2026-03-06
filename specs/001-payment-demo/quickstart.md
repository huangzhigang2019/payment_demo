# Quickstart: 支付交易读卡 Demo

**Feature**: 001-payment-demo  
**Audience**: 开发与测试人员，用于本地构建、安装与基本验证。

---

## 1. 环境要求

- **JDK**: Java 8 或以上（与 Android Gradle 插件兼容）。
- **Android SDK**: 建议 compileSdk 28+，minSdk 26（Android 8.0）。
- **Android Studio / 命令行**: 能执行 `./gradlew` 或 `gradlew.bat` 的构建环境。
- **设备**: 支持 NeptuneLiteAPI 与 EMV 库的真机或厂商模拟环境；纯模拟器可能无法完成读卡与 EMV 流程。

---

## 2. 获取代码与依赖

- 克隆或打开本仓库，切换到分支 `001-payment-demo`。
- 确认以下内容存在：
  - `sdk/`、`emv/`、`commonlib/`：NeptuneLiteAPI 与 EMV 库（已内联至项目根目录，app 依赖 `implementation project(':sdk')` 等）。
  - `UI_Resource/`：界面素材，需在 app 中引用。
- `Reference_Project/` 为可选参考，项目不依赖该目录参与编译，完成后可移除。

---

## 3. 构建与安装

- **命令行**（在仓库根目录）:
  - Windows: `gradlew.bat assembleDebug`
  - Linux/macOS: `./gradlew assembleDebug`
- **安装到设备**: `gradlew installDebug` 或通过 Android Studio 的 Run。
- **输出**: Debug APK 位于 `app/build/outputs/apk/debug/`。
- **PAX 终端 32 位 ABI**：本工程面向 PAX 等 32 位终端，已在 `app/build.gradle` 中配置 `ndk.abiFilters 'armeabi-v7a','armeabi'` 与 `jniLibs.useLegacyPackaging = true`，并在 `AndroidManifest.xml` 中设置 `android:extractNativeLibs="true"`，确保 native so（如 libDeviceConfig.so、EMV so）可被正确加载。APK 内 so 位于 `lib/armeabi/`。

---

## 4. 首次运行与基本验证

1. 启动应用，进入主入口界面。
2. 输入合法金额（如 1.00），点击“开始交易”。
3. 应进入读卡引导界面（插卡/挥卡/刷磁条提示）；可点击“取消”返回主入口，验证 FR-003。
4. 若设备与卡片就绪，完成读卡与 EMV 流程后应进入结果页，展示金额、时间、结果状态与原因摘要（FR-005）。
5. 进入“设置/信息”页，确认版本信息与“参数是否加载”“读卡是否可用”等状态（FR-006）。

---

## 5. 配置与参数

- EMV 参数文件（AID、CAPK 等）放在 `app/src/main/assets/`，格式参考 `emv/src/main/assets/`；若需选择参数文件，可在设置或首次启动时完成。
- 读卡超时时间、是否支持某读卡方式等，可在配置层或 BuildConfig 中定义，便于调试。

---

## 6. 故障排查

- **无法读卡**: 检查 DAL 是否可用（getDal() 非 null）、设备是否支持对应读卡方式；查看设置页“读卡模块是否可用”。若进入寻卡即失败，可点“重试”或“返回”；读卡模块已做生命周期加固，避免关闭时 native 崩溃。
- **EMV 失败**: 确认参数已加载、测试卡与终端配置匹配；查看结果页原因摘要与日志。
- **构建失败**: 确认 NeptuneLiteAPI 与 EMV 库依赖路径正确、minSdk/targetSdk 与库要求一致。
- **libDeviceConfig.so / Native 崩溃**: 已通过 32 位 ABI 打包与 `extractNativeLibs`、读卡关闭流程安全化（safeCloseReaders）修复；若仍崩溃，请确认设备为 32 位并检查 APK 内 `lib/armeabi/` 是否含所需 so。

---

## 7. PAX 真机回归验证（推荐）

在 PAX 等 32 位终端上建议执行以下回归以确认读卡与关闭流程稳定：

- **场景 A**：输入金额进入寻卡页，保持至少 10s，不应立刻失败。
- **场景 B**：进入寻卡页后点击返回或按返回键，应正常回到主入口，不触发 native 崩溃。
- **场景 C**：无卡等待至超时，应提示超时并可选择重试/返回；有卡时可继续进入处理中与结果页。

**验证状态**：构建通过 `gradlew assembleDebug`；T003 已引入 UI_Resource 素材（读卡/处理中/结果页）；T019–T021 已实现读卡能力探测、换方式/重试提示、移卡提示。PAX 真机 A/B/C 场景需在设备上手动验证。

---

## 8. Reference_Project 移除（可选）

`Reference_Project/` 为参考代码，项目构建不依赖该目录（`settings.gradle` 未包含）。确认构建与运行正常后，可手动删除 `Reference_Project/` 以精简仓库。

更详细的实现与任务拆分见 [plan.md](./plan.md) 与 [tasks.md](./tasks.md)。
