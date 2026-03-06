# Tasks: 支付交易读卡 Demo（001-payment-demo）

**Input**: `specs/001-payment-demo/spec.md`, `plan.md`, `research.md`, `data-model.md`, `contracts/*`, `quickstart.md`  
**Prerequisites**: plan.md（required）, spec.md（required for user stories）

**Tests**: 规格未强制要求 TDD；未生成独立测试任务。验证见 quickstart.md 与各 Phase Independent Test。

**Organization**: 按用户故事分组，便于独立实现与验证。

---

## Format（严格）

每个任务必须严格遵循：

- `- [ ] T### [P?] [USn?] <描述>（含文件路径）`

约定：

- **[P]**：可并行（不同文件、无依赖）
- **[USn]**：仅用户故事相关任务需要（US1/US2/US3）
- **文件路径**：每条任务描述必须包含至少一个精确路径

---

## Phase 1: Setup（共享基础 / 项目自包含）

**Purpose**: Android 8+（minSdk 26）可构建运行；工程不依赖 `Reference_Project/` 参与编译；native so 可正确打包/加载。

- [x] T001 将 `sdk/`, `emv/`, `commonlib/` 内联到项目根目录并移除对 `Reference_Project/` 的构建依赖（`settings.gradle`）
- [x] T002 配置应用依赖与 ABI 打包策略（`app/build.gradle`, `app/src/main/AndroidManifest.xml`）
- [x] T003 [P] 引入核心 UI_Resource 素材到资源目录并替换关键页面资源引用（`UI_Resource/`, `app/src/main/res/`）
- [x] T004 准备 EMV 参数文件（复制/生成）并确保打包进 assets（`app/src/main/assets/`, `emv/src/main/assets/`）
- [x] T005 构建后校验 APK 内包含 `libDeviceConfig.so` 与 EMV so（`app/build/outputs/apk/debug/app-debug.apk`）

---

## Phase 2: Foundational（阻塞所有用户故事）

**Purpose**: DAL 初始化、参数加载、状态查询、基础实体与错误处理就绪；Application 内需完成 EMV native 库加载（plan.md），确保 ClssProcess 使用前已 loadLibrary。

**⚠️ CRITICAL**: 未完成本阶段前，不得开始任何用户故事实现。

- [x] T006 初始化 Application 与 DAL 获取（容错不崩溃，提供线程切换能力）；含 EMV 库加载 EmvBase.loadLibrary()（`app/src/main/java/com/payment/demo/app/PaymentDemoApp.java`）
- [x] T007 配置/状态模块：参数加载与 Terminal/Config Status 提供（`app/src/main/java/com/payment/demo/config/ConfigStatusProvider.java`）
- [x] T008 [P] 实体模型：Transaction / CardReadResult / ProcessingResult（`app/src/main/java/com/payment/demo/trans/Transaction.java`, `.../CardReadResult.java`, `.../ProcessingResult.java`）
- [x] T009 统一错误处理与日志：异常必须转为可读原因，不允许崩溃（`app/src/main/java/com/payment/demo/` 下相关模块）
- [x] T010 基础导航骨架与 5 屏占位跳转（`app/src/main/java/com/payment/demo/ui/*`, `app/src/main/res/layout/*`）

**Checkpoint**: Foundation ready（可开始 US1/US2/US3）。

---

## Phase 3: User Story 1 - 完成一笔“消费/支付”交易（Priority: P1）🎯 MVP

**Goal**: 输入金额 → 寻卡 → EMV 处理 → 结果页；支持取消与读卡超时。

**Independent Test**: 输入合法金额 → 开始交易 → 等待读卡（或取消/超时）→ 进入结果页展示金额/时间/状态/原因摘要。

### Implementation（US1）

- [x] T011 [US1] 金额输入校验与开始交易入口（阻止非法金额）（`app/src/main/java/com/payment/demo/ui/MainActivity.java`）
- [x] T012 [US1] DAL 就绪校验：跳转读卡页前确认 `getDal()!=null` 或提供“设备未就绪”提示（`app/src/main/java/com/payment/demo/ui/MainActivity.java`, `app/src/main/res/values/strings.xml`）
- [x] T013 [US1] 读卡模块：使用显式 mode (PICC|ICC|MAG) 替代 EReaderType.DEFAULT，支持 PICC/ICC/MAG 轮询（MAG: isSwiped/read；ICC: detect/init；PICC: detect），并保留 stop/close 兜底（`app/src/main/java/com/payment/demo/card/CardReaderManager.java`）
- [x] T014 [US1] 读卡引导页：成功跳转处理中；超时/取消/初始化失败可读提示 + 重试/返回（`app/src/main/java/com/payment/demo/ui/ReadCardActivity.java`, `app/src/main/res/layout/activity_read_card.xml`, `app/src/main/res/values/strings.xml`）
- [x] T015 [US1] EMV 处理封装：按介质类型调用 EmvProcess/ClssProcess，处理应用选择/PIN/在线授权回调并输出 ProcessingResult（`app/src/main/java/com/payment/demo/emv/*`）
- [x] T016 [US1] 处理中页：调用 EMV 处理模块，异常/不可恢复错误转失败并展示原因，不崩溃（`app/src/main/java/com/payment/demo/ui/ProcessingActivity.java`）
- [x] T017 [US1] 结果页：展示金额/时间/状态/原因摘要；提供返回或再试一笔（`app/src/main/java/com/payment/demo/ui/ResultActivity.java`, `app/src/main/res/layout/activity_result.xml`）
- [x] T018 [US1] 流程编排：取消/超时后回到可再次发起状态并释放资源（`app/src/main/java/com/payment/demo/trans/*`, `app/src/main/java/com/payment/demo/ui/*`）

**Checkpoint**: US1 可端到端演示（含失败可读原因），且 crash-free。

---

## Phase 4: User Story 2 - 支持多种读卡方式与回退提示（Priority: P2）

**Goal**: 多介质读卡与“换方式/重试/取消”回退提示覆盖更多演示场景。

**Independent Test**: 用不同介质卡验证；某方式失败/不支持时提示可操作，能回到可继续状态。

- [x] T019 [US2] 读卡能力探测与屏蔽不支持项（PICC/ICC/MAG），并在 UI 上显示正确提示语（`app/src/main/java/com/payment/demo/config/ConfigStatusProvider.java`, `app/src/main/java/com/payment/demo/ui/ReadCardActivity.java`）
- [x] T020 [US2] 读卡失败分支 UX：初始化失败/不支持/轮询失败时提供换方式/重试/返回（`app/src/main/java/com/payment/demo/ui/ReadCardActivity.java`, `app/src/main/res/values/strings.xml`）
- [x] T021 [US2] 卡片移开/拔出提示：读卡或 EMV 处理中检测到移除时提示并允许重试/取消（`app/src/main/java/com/payment/demo/card/CardReaderManager.java`, `app/src/main/java/com/payment/demo/emv/*`, `app/src/main/java/com/payment/demo/ui/*`）

---

## Phase 5: User Story 3 - 可查看基础信息与必要配置状态（Priority: P3）

**Goal**: 设置/信息页可查看版本与关键状态，便于排障。

**Independent Test**: 不插卡即可进入设置页看到“参数是否加载/读卡是否可用/版本信息”。

- [x] T022 [US3] 设置/信息页展示：版本、参数加载状态、读卡模块可用性（`app/src/main/java/com/payment/demo/ui/SettingsActivity.java`, `app/src/main/java/com/payment/demo/config/ConfigStatusProvider.java`, `app/src/main/res/layout/activity_settings.xml`）
- [x] T023 [US3] 主入口增加设置入口并可返回（`app/src/main/java/com/payment/demo/ui/MainActivity.java`, `app/src/main/res/layout/activity_main.xml`）

---

## Phase 6: Polish & Cross-Cutting

**Purpose**: 边界情况、体验一致性、交付验证与文档。

- [x] T024 金额校验 Edge Cases 全覆盖（空、0、格式错误、超限）（`app/src/main/java/com/payment/demo/ui/MainActivity.java`, `app/src/main/res/values/strings.xml`）
- [x] T025 关键交互超时（PIN/应用选择）处理：超时进入结果页并可重试（`app/src/main/java/com/payment/demo/emv/*`, `app/src/main/java/com/payment/demo/ui/*`）
- [x] T026 [P] 全屏使用 UI_Resource 视觉素材一致性核对（`UI_Resource/`, `app/src/main/res/drawable/`, `app/src/main/res/layout/`）
- [x] T027 运行 quickstart 验证：构建、安装、跑通 A/B/C 场景并更新说明（`specs/001-payment-demo/quickstart.md`, `app/build/outputs/apk/debug/app-debug.apk`）
- [x] T028 移除 `Reference_Project/`（确认构建不依赖后再删）并复跑构建验证（`Reference_Project/`, `settings.gradle`）

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 → Phase 2 → Phase 3/4/5 → Phase 6

### User Story Dependencies

- **US1（P1）**: 依赖 Phase 2（Foundational）；无其他故事依赖
- **US2（P2）**: 依赖 Phase 2；建议在 US1 读卡稳定后增强，可独立验收
- **US3（P3）**: 依赖 Phase 2（尤其 ConfigStatusProvider）；可独立验收

### Within Each User Story

- 实体/契约已落在 Phase 2 或 Setup；各故事内任务按实现顺序（入口 → 读卡/处理 → 结果/设置）
- 每 Phase 完成可在 Checkpoint 做独立验证

### Parallel Opportunities

- Phase 1：T003 与 T004 可并行
- Phase 2：T008 可与其他任务并行（独立实体文件）
- Phase 3：T011 与 T013、T015 可并行（不同文件）
- Phase 6：T026 与其它收尾任务可并行

---

## Parallel Example: US1

```text
Task: "T011 MainActivity 金额校验（app/src/main/java/com/payment/demo/ui/MainActivity.java）"
Task: "T013 CardReaderManager 显式 mode + PICC/ICC/MAG（app/src/main/java/com/payment/demo/card/CardReaderManager.java）"
Task: "T015 EMV 处理封装（app/src/main/java/com/payment/demo/emv/*）"
```

---

## Implementation Strategy

### MVP First（仅 User Story 1）

1. 完成 Phase 1: Setup
2. 完成 Phase 2: Foundational
3. 完成 Phase 3: User Story 1
4. **STOP 并验证**：PAX 真机挥卡/插卡/刷卡能检测并跳转，无 UnsatisfiedLinkError
5. 可部署/演示

### Incremental Delivery

1. Setup + Foundational → 基础就绪
2. 加入 US1 → 独立验证 → 部署（MVP）
3. 加入 US2 → 独立验证 → 部署
4. 加入 US3 → 独立验证 → 部署
5. 每故事增量交付，不破坏已有功能

### 与 plan.md 的对应

- **T006**：Application 初始化须包含 `EmvBase.loadLibrary()`（后台线程），避免拍卡后 `Clss_CoreInit_Entry` UnsatisfiedLinkError。
- **T013**：读卡须使用显式 mode `(PICC|ICC|MAG)` 替代 `EReaderType.DEFAULT`，否则轮询不进入检测分支，寻卡无反应。

---

## Notes

- [P] 任务：不同文件、无未完成依赖时可并行
- [USn] 标签用于与 spec 用户故事对应
- 每 Phase 可在 Checkpoint 单独验收
- 路径以 repo 根为基准；Android 模块为 `app/`、`emv/`、`sdk/`、`commonlib/`
