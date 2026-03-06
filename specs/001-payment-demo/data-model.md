# Data Model: 支付交易读卡 Demo

**Feature**: 001-payment-demo  
**Source**: 从 [spec.md](./spec.md) 的 Key Entities 与 Functional Requirements 提取，用于实现与契约设计。

---

## 1. Transaction（交易）

一笔交易的抽象，对应规格中的“从发起交易到结果页”的完整过程。

| 属性 | 类型 | 说明 | 校验/约束 |
|------|------|------|------------|
| 金额 | 数值（最小单位，如分） | 交易金额 | 必填；> 0；上限由业务/配置约定（FR-001 金额校验） |
| 开始时间 | 时间戳 | 用户点击“开始交易”或进入读卡引导时 | 必填 |
| 结束时间 | 时间戳 | 展示结果页时 | 成功/失败/取消/超时均有 |
| 介质类型 | 枚举 | 磁条 / 接触式 IC / 非接触 | 读卡成功后确定 |
| 结果状态 | 枚举 | 成功 / 失败 / 取消 / 超时 | 必填（FR-005） |
| 失败原因摘要 | 文本 | 可展示给测试人员的错误信息 | 失败时必填；取消/超时可有简短说明 |
| 参考号/流水号 | 文本 | 若 EMV/主机返回则记录 | 可选 |

**状态**: 从“已创建”到“已结束”；取消或超时也视为已结束。不要求持久化到数据库，单次会话内有效即可。

---

## 2. Card Read Result（读卡结果）

读卡阶段的结果，用于决定后续走磁条/接触/非接触哪条 EMV 流程。

| 属性 | 类型 | 说明 | 校验/约束 |
|------|------|------|------------|
| 介质类型 | 枚举 | PICC / ICC / Mag | 与 EReaderType 或等价枚举一致 |
| 是否成功 | 布尔 | 是否检测到有效卡 | 必填 |
| 失败原因 | 枚举/码 | 超时、取消、初始化失败等 | 未成功时必填 |
| 卡片标识信息 | 视介质 | 如 PAN、AID 等（若已读） | 可选，用于结果页或日志 |

**来源**: NeptuneLiteAPI 的 IPicc/IIcc/IMag 轮询结果；参考 DetectCardResult 的 retCode、readType。

---

## 3. Processing Result（交易处理结果）

EMV 流程（或磁条简化流程）执行完毕后的结果，用于驱动结果页展示。

| 属性 | 类型 | 说明 | 校验/约束 |
|------|------|------|------------|
| 结果状态 | 枚举 | 成功 / 失败 / 取消 / 超时 | 与 Transaction 结果状态一致 |
| 结果码/原因码 | 整型或枚举 | EMV/内核返回码或内部定义码 | 失败时用于展示与排障 |
| 展示摘要 | 文本 | 结果页上的简短说明（如“交易成功”“密码错误”“已取消”） | 必填，用户可读 |

**来源**: EMV 库的 TransResult/TransResultEnum、IssuerRspData 等；需映射到规格要求的“交易金额、时间、结果状态、原因摘要”（FR-005）。

---

## 4. Terminal/Config Status（终端与配置状态）

用于“设置/信息”页，帮助判断是否具备交易条件（FR-006）。

| 属性 | 类型 | 说明 | 校验/约束 |
|------|------|------|------------|
| 是否可交易 | 布尔 | DAL 可用且参数已加载 | 必填 |
| 关键参数是否加载 | 布尔 | EMV 参数（AID/CAPK 等）是否成功加载 | 必填 |
| 读卡模块是否可用 | 布尔/多值 | 各读卡方式（PICC/ICC/Mag）是否可用 | 可选，便于细分排障 |
| 版本信息 | 文本 | 应用版本、EMV 内核版本等 | 必填 |

**来源**: 应用启动时初始化 NeptuneLiteAPI（getDal）、加载 assets 参数；若 getDal 为 null 或异常则“不可交易”。

---

## 5. 关系与流转

- **Transaction** 创建后，依赖一次 **Card Read Result**（成功后才进入处理）。
- **Transaction** 的最终结果由 **Processing Result** 或“用户取消/读卡超时”决定。
- **Terminal/Config Status** 在应用生命周期内可多次读取，不绑定单笔交易。
- 金额输入校验：在创建 Transaction 前完成（禁止空、0、超限、格式错误），对应规格 Edge Cases。

---

## 6. 与参考项目实体的对应

| 本模型 | JemvDemo2.0 / 参考 |
|--------|---------------------|
| Card Read Result | DetectCardResult（retCode, readType, 卡片信息） |
| Processing Result | TransResult, TransResultEnum, CvmResultEnum, IssuerRspData |
| Transaction | 由 MainActivity 传入的 transAmount、transType 与 TransProcessActivity 结果组合而成 |
| Terminal/Config Status | 参数加载状态、KernelLibInfo、SelectParamFile 相关状态 |

实现时可直接复用或适配参考项目中的实体类，保持字段语义与上表一致即可满足规格与契约。
