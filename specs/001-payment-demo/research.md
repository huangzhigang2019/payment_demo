# Research: 支付交易读卡 Demo（NeptuneLiteAPI + EMV）

**Feature**: 001-payment-demo  
**Purpose**: 支撑 plan 技术选型与集成方式，消除 Phase 0 未决项并记录最佳实践。

---

## 1. NeptuneLiteAPI 接入方式与读卡模式

**Decision**: 通过 `NeptuneLiteUser.getInstance().getDal(context)` 获取 `IDAL`，再使用 `getPicc()` / `getMag()` / `getIcc()` 进行非接触、磁条、接触式读卡；读卡逻辑在后台线程执行，结果通过回调或 LiveData/Handler 回主线程更新 UI。

**Rationale**:  
- 参考项目 neptuneliteapidemo 与 JemvDemo2.0 均使用 `NeptuneLiteUser.getDal(context)` 获取设备层；JemvDemo2.0 的 `PiccDetectModel`/`MagDetectModel`/`IccDetectModel` 在后台线程中调用 `getDal().getPicc()/getMag()/getIcc()` 做轮询，与规格“等待读卡超时”“取消”需求一致。  
- DAL 返回的 `IPicc`/`IMag`/`IIcc` 为设备抽象，便于在无真机时通过 Mock DAL 做单元/集成测试。

**Alternatives considered**:  
- 直接使用厂商私有 API：不利于移植与测试，不采纳。  
- 仅支持单一读卡方式：规格要求多种读卡方式与回退提示，需保留 PICC/Mag/ICC 并行或顺序检测能力。

---

## 2. EMV 流程与库使用方式

**Decision**: 采用 JemvDemo2.0 的 EMV 库结构：接触式使用 `EmvProcess`，非接触使用 `ClssProcess`（及内核实现如 PayPass/PayWave 等）；交易参数通过 `EmvTransParam`/`EmvProcessParam` 注入，流程回调使用 `IEmvTransProcessListener`（应用选择、持卡人密码等）与 `IClssStatusListener`（非接触移除卡等）；设备相关操作通过 `DeviceImplNeptune` 封装 DAL（PIN、蜂鸣、读卡槽等）。

**Rationale**:  
- 规格要求“EMV 卡处理用 EMV 库”，参考项目已实现完整流程（应用选择 → 读数据 → 风险管理 → CVM → 脚本 → 结果），复用可减少重复开发与合规风险。  
- `TransResult`/`TransResultEnum`/`IssuerRspData` 等实体可直接映射到规格中的 Processing Result 与结果页展示。

**Alternatives considered**:  
- 自研简化 EMV 流程：合规与兼容性风险高，不采纳。  
- 仅做“读卡成功即算交易成功”：不满足真实 EMV 终端行为与规格中的“交易处理”与结果状态，不采纳。

---

## 3. Android 8+ 与 Java 版本

**Decision**: minSdkVersion 26（Android 8.0），targetSdkVersion 28+；语言 Java 8，编译与参考项目保持一致。

**Rationale**:  
- 用户明确“Android 8 以上”；API 26 为 Android 8.0，覆盖范围合理。  
- JemvDemo2.0 使用 minSdk 21、compileSdk 28；将 minSdk 提高到 26 仅缩小最低设备范围，不影响 NeptuneLiteAPI/EMV 在参考项目中的用法。

**Alternatives considered**:  
- minSdk 21：可兼容更多设备，但用户指定 8+，故选 26。  
- Kotlin：规格未要求；参考项目为 Java，为降低迁移成本先采用 Java。

---

## 4. UI 资源与界面一致性

**Decision**: 核心界面（主入口、读卡引导、处理中、结果、设置/信息）使用 `UI_Resource/` 下提供的图片与动效（如 Insert／Tap Card、Processing、Success、Failed、PIN 等），通过 res/drawable 或 assets 引用，保证与设计一致。

**Rationale**:  
- 规格 FR-008 要求“使用项目提供的 UI 资源文件呈现核心页面”。  
- UI_Resource 已包含成功/失败/处理中/插卡/挥卡/输入 PIN 等素材，可直接对应交易状态与引导提示。

**Alternatives considered**:  
- 纯系统控件不引用 UI_Resource：违反规格，不采纳。  
- 全部自定义绘制：工作量大且无必要，不采纳。

---

## 5. 超时与取消

**Decision**: 读卡超时采用单次轮询超时（参考项目 60s 可配置），交易流程中“等待用户操作”的步骤（如输入 PIN、应用选择）单独设置超时；所有可中断步骤支持“取消”信号，收到后停止轮询/流程并回到“可再次发起交易”状态。

**Rationale**:  
- 规格 FR-004、FR-003 要求超时与取消可操作且可返回。  
- JemvDemo2.0 中 `PiccDetectModel` 使用 `timeout` 与 `isStop` 控制超时与停止；Presenter 层可对 EMV 回调与读卡结果做统一取消与超时处理。

**Alternatives considered**:  
- 仅读卡超时、不做交互超时：规格要求“关键交互超时”，需覆盖。  
- 取消后不释放读卡器：可能导致下次读卡异常，必须在取消时 stopPolling/close。

---

## 6. 配置与参数（EMV 参数、终端状态）

**Decision**: EMV 参数文件（AID/CAPK/终端等）放在 app assets，格式与加载方式参考 JemvDemo2.0（如 emv_param.contact、paypass_param 等）；“设置/信息”页展示的“参数是否加载”“读卡模块是否可用”通过应用启动时初始化 DAL 与参数加载结果提供。

**Rationale**:  
- 规格 FR-006、User Story 3 要求可查看配置与状态，便于排障。  
- 参考项目已有参数选择与内核版本展示（SelectParamFileActivity、KernelLibInfoActivity），可复用思路。

**Alternatives considered**:  
- 参数从网络拉取：Demo 以离线为主，本地 assets 即可。  
- 不展示参数状态：不利于“快速判断是否具备交易条件”，不采纳。

---

## 7. 内联 sdk/emv/commonlib 模块（2026-03）

**Decision**: 将 Reference_Project/JemvDemo2.0 中的 sdk、emv、commonlib 复制到项目根目录，使项目自包含；Reference_Project 仅作 AI 参考，项目完成后可移除。

**Rationale**: 用户要求“把这些实现直接在当前项目中创建，Reference_Project 只是给 AI 做参考，项目最后会移除这个目录”。内联后 settings.gradle 仅 include 本地模块，无 projectDir 指向 Reference_Project。

**实施结果**: commonlib/、sdk/、emv/ 已位于 paymentDemo 根目录；settings.gradle 已更新；构建成功，APK 内包含 lib/armeabi/libDeviceConfig.so 及 EMV so。

---

## 8. NeptuneLiteAPI 与 EMV 库集成状态验证（2026-03）

**验证结论**：

| 组件 | 状态 | 说明 |
|------|------|------|
| NeptuneLiteAPI | 已集成 | sdk 模块（项目根目录），使用 NeptuneLiteApi_V3.12.00_20200331.jar；CardReaderManager 通过 getDal().getPicc/getMag/getIcc 读卡；PaymentDemoApp 通过 Sdk.getInstance().getDal() 获取 IDAL。 |
| EMV 库 | 依赖已加入，未用于 APDU | emv、commonlib 模块（项目根目录）；emv 提供 EmvProcess、ClssProcess 及 native so；ProcessingActivity 使用 runEmvPlaceholder() 直接返回成功，未调用 EmvProcess/ClssProcess，当前无法与卡片进行 APDU 交互。 |

**根因**：tasks.md 中 T013（实现 EMV 处理封装）未完成。

**后续实施建议**（补齐 EMV 流程）：  
1. 新建 `app/src/main/java/.../emv/` 包，封装 EmvProcess/ClssProcess 调用，参考 TransProcessPresenter、DeviceImplNeptune。  
2. 改造 ProcessingActivity：根据 CardReadResult.readType（ICC/PICC/MAG）选择 EmvProcess（接触式）或 ClssProcess（非接触式）；MAG 仅做展示或简单处理。  
3. 处理 EMV 回调：应用选择、持卡人验证（PIN/签名）、在线授权、脚本处理等，参考 TransProcessActivity 的 onTransFinish、processCvm、displaySelectOnlineResultDlg。  
4. 确保 app/src/main/assets/ 中有有效 EMV 参数（emv_param.contact、paypass_param 等），参考 JemvDemo2.0 的 assets。

---

## 9. 寻卡与卡片处理问题修复（2026-03）

**问题**: 应用执行后无法正常的寻卡并进行卡片的处理。

**Code Review 结论**:
- **寻卡**: CardReaderManager 仅检测 PICC，未实现 ICC、MAG 轮询；需参考 MagDetectModel、IccDetectModel 扩展，或使用 ICardReaderHelper.polling()（若 IDAL 提供）。
- **DAL 时序**: 异步初始化可能导致进入读卡页时 getDal() 为 null；建议跳转前校验 DAL 就绪。
- **卡片处理**: ProcessingActivity 使用 runEmvPlaceholder，未调用 EmvProcess/ClssProcess；T013 未完成。

**决策**: 按 [fix-readcard-and-process.plan.md](./fix-readcard-and-process.plan.md) 实施：P1 扩展 CardReaderManager 支持 ICC/MAG 或改用 ICardReaderHelper；P2 校验 DAL 就绪、完成 T013。

---

---

## 10. 寻卡无反应根因与修复（2026-03）

**问题**: 在寻卡页面，挥卡、插卡或刷卡，应用没有任何反应。

**Decision**: 使用显式读卡模式 `PICC | ICC | MAG` 替代 `EReaderType.DEFAULT.getEReaderType()`。

**Rationale**:
- `CardReaderManager.doPoll()` 使用 `mode = EReaderType.DEFAULT.getEReaderType()` 作为轮询模式
- 若 DEFAULT 返回 0 或不包含 PICC/ICC/MAG 位标志，则 `(mode & EReaderType.XXX) == EReaderType.XXX` 恒为 false
- 轮询循环永不进入 MAG/ICC/PICC 检测分支，仅执行 sleep(200)，导致无反应
- 参考项目 JemvDemo2.0 使用 `EReaderType.ICC_PICC` 等显式类型，从未用 DEFAULT 作为轮询模式

**Alternatives considered**:
- 仅修复 PICC：用户反馈包含三种方式，需全部支持
- 动态探测设备能力：增加复杂度，显式 0x07 已满足 Demo 需求

---

## Summary

| 主题           | 决策要点                                                                 |
|----------------|--------------------------------------------------------------------------|
| NeptuneLiteAPI | getDal(context) → getPicc/getMag/getIcc；后台轮询，主线程更新 UI；已集成 |
| EMV            | EmvProcess/ClssProcess + 参考项目回调与 DeviceImplNeptune；依赖已加入，T013 已完成 |
| 平台           | Android 8+ (API 26)，Java 8                                              |
| UI             | 核心页面引用 UI_Resource 素材                                            |
| 超时/取消      | 读卡与交互均支持超时与取消，取消时释放资源                                |
| 配置           | EMV 参数放 assets，设置页展示加载状态与读卡可用性                        |
| 寻卡无反应     | 使用显式 mode (PICC\|ICC\|MAG) 替代 DEFAULT，确保轮询三种读卡方式        |

---

## 11. 拍卡后 Clss_CoreInit_Entry UnsatisfiedLinkError（2026-03）

**问题**: 挥卡后进入 ProcessingActivity 崩溃：`No implementation found for ClssEntryApi.Clss_CoreInit_Entry()`。

**Decision**: 在 `PaymentDemoApp` 启动时调用 `EmvBase.loadLibrary()`。

**Rationale**:
- `Clss_CoreInit_Entry` 为 JNI 方法，实现在 JNI_ENTRY_v103 / F_ENTRY_LIB_PayDroid 中
- 这些库需通过 `EmvBase.loadLibrary()` 显式加载
- 参考项目 EmvDemoApp 在 `initEmvModule()` 中调用 `EmvBase.loadLibrary()`
- PaymentDemoApp 未调用，导致首次使用 ClssProcess 时 UnsatisfiedLinkError

**Alternatives considered**:
- 在 EmvProcessor 首次调用时加载：可行但需同步，Application 启动时加载更清晰
