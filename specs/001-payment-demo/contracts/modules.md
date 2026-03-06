# Module / Integration Contracts: 支付 Demo

**Feature**: 001-payment-demo  
**Purpose**: 约定应用与读卡、EMV 等模块的边界与职责，便于实现与测试（含 Mock）。

---

## 1. 读卡模块（Card Reader）

**职责**: 在指定读卡类型下轮询检测卡片，支持超时与取消，返回读卡结果。

**提供给应用**:
- **启动检测**: 输入：读卡类型（如 PICC+ICC+Mag 或 DEFAULT）；无返回值，结果通过回调/LiveData 提供。
- **停止检测**: 无参数；调用后轮询停止，并尽快返回“取消”或超时类结果。
- **结果**: 见 [data-model.md](../data-model.md) 的 **Card Read Result**（介质类型、是否成功、失败原因、卡片标识信息若可得）。

**依赖**: NeptuneLiteAPI（IDAL → getPicc/getMag/getIcc）；在后台线程执行轮询，结果回主线程。

**超时**: 单次轮询超时时间可配置（建议 60s，与参考项目一致）；超时后结果状态为“超时”。

---

## 2. EMV 处理模块（Transaction Processor）

**职责**: 根据读卡结果执行对应流程（接触式 EmvProcess / 非接触 ClssProcess / 磁条若支持），处理应用选择、CVM、脚本等，并返回处理结果。

**提供给应用**:
- **执行交易**: 输入：交易金额、介质类型、读卡结果（卡片信息）；输出：Processing Result（结果状态、结果码、展示摘要）。
- **取消**: 若流程支持，可请求取消；取消后返回“取消”类结果。

**依赖**: EMV 库（参考 JemvDemo2.0 的 EmvProcess/ClssProcess）、设备封装（PIN、蜂鸣、读卡槽等通过 NeptuneLiteAPI/DAL）。

**回调**: 应用选择、持卡人密码等由 EMV 库回调；应用负责 UI 交互（输入 PIN、选应用）并回填结果，不改变 EMV 库规定的流程顺序。

---

## 3. 配置/状态模块（Config & Status）

**职责**: 加载 EMV 参数、检测 DAL 与读卡器可用性，提供 Terminal/Config Status。

**提供给应用**:
- **初始化**: 应用启动时调用；加载 assets 中的 EMV 参数，尝试 getDal()，并记录“参数是否加载”“读卡模块是否可用”。
- **获取状态**: 返回当前 Terminal/Config Status（是否可交易、版本信息等），供设置/信息页展示。

**依赖**: NeptuneLiteAPI（getDal）、assets 参数文件、应用版本信息。

---

## 4. 与参考项目的对应

| 契约模块 | 参考实现 |
|----------|----------|
| 读卡模块 | DetectCardPresenter / NeptunePollingPresenter，PiccDetectModel / MagDetectModel / IccDetectModel |
| EMV 处理模块 | TransProcessPresenter，EmvProcess / ClssProcess，DeviceImplNeptune |
| 配置/状态 | ParamManager，SelectParamFileActivity，KernelLibInfoActivity，EmvDemoApp 初始化 |

实现时可复用或适配上述类，对外满足本契约即可；测试时可对读卡与 EMV 模块做 Mock 以不依赖真机。
