# Payment Flow Requirements Quality Checklist: 支付 Demo 应用

**Purpose**: 校验交易流程、读卡、EMV 处理、异常与回退等需求在规格中的完整性、清晰度与可测性（需求质量，非实现验证）  
**Created**: 2026-03-06  
**Feature**: [spec.md](../spec.md) | [plan.md](../plan.md) | [contracts/](../contracts/)

**Note**: 本检查清单由 `/speckit.checklist` 基于 feature 上下文生成，用于在实现前验证需求是否写全、写清、可测。

---

## Requirement Completeness（需求完整性）

- [ ] CHK001 - 是否对“发起交易”入口的触发条件与前置校验（如 DAL 就绪）有明确需求？[Completeness, Spec §FR-001, §FR-002]
- [ ] CHK002 - 读卡引导页的“等待读卡”超时时间是否在规格中量化（如 60 秒）？[Gap, Spec §FR-004]
- [ ] CHK003 - 是否对“关键交互超时”（PIN 输入、应用选择）有独立需求定义？[Gap, Spec §Edge Cases, tasks T025]
- [ ] CHK004 - 结果页“原因摘要”的粒度是否在规格中定义（如：面向测试人员可读、可定位问题）？[Clarity, Spec §FR-005]
- [ ] CHK005 - 多种读卡方式（PICC/ICC/Mag）的“不支持或失败”分支是否有可操作提示与回退路径的明确需求？[Completeness, Spec §FR-007, §US2]

---

## Requirement Clarity（需求清晰度）

- [ ] CHK006 - “合法金额”是否在规格中量化（空、0、格式错误、上限）？[Clarity, Spec §FR-001, §Edge Cases]
- [ ] CHK007 - “可再次发起交易的状态”是否明确定义（如：释放资源、回到主入口、3 步内可达）？[Clarity, Spec §FR-003, §SC-004]
- [ ] CHK008 - “交易成功”及“关键摘要信息”的必显字段是否与 FR-005 一致且无歧义？[Consistency, Spec §US1-AS2, §FR-005]
- [ ] CHK009 - “可操作的提示”（重试/换方式/取消）是否在规格中枚举或分类？[Clarity, Spec §FR-007, §US2-AS2]

---

## Requirement Consistency（需求一致性）

- [ ] CHK010 - 读卡引导与处理中页的“取消”行为是否与 FR-003 一致？[Consistency, Spec §FR-003, contracts §2 §3]
- [ ] CHK011 - 超时处理需求在 US1、FR-004、Edge Cases 之间是否一致？[Consistency, Spec §FR-004, §Edge Cases]
- [ ] CHK012 - 结果状态枚举（成功/失败/取消/超时）在 Transaction、Processing Result、结果页契约中是否统一？[Consistency, data-model, contracts §4]

---

## Acceptance Criteria Quality（验收标准质量）

- [ ] CHK013 - SC-001（60 秒内完成）是否排除“用户人为停顿”的判定方式有说明？[Measurability, Spec §SC-001]
- [ ] CHK014 - SC-002（Crash-free）的“20 次连续演示”环境与卡/设备条件是否在规格中约定？[Measurability, Spec §SC-002]
- [ ] CHK015 - SC-003（首轮完成率 90%）的“标准测试卡与设备环境”是否在规格或依赖中定义？[Measurability, Spec §SC-003]
- [ ] CHK016 - “3 步以内回到可再次发起交易”的“步”是否明确定义（如：点击次数、界面跳转）？[Clarity, Spec §SC-004]

---

## Scenario Coverage（场景覆盖）

- [ ] CHK017 - 主流程（输入金额 → 读卡 → 处理 → 结果）的验收场景是否覆盖所有结果状态？[Coverage, Spec §US1]
- [ ] CHK018 - 多种读卡方式的 Alternate 流程（挥卡/插卡/刷卡）是否在 US2 中有对应验收场景？[Coverage, Spec §US2]
- [ ] CHK019 - 异常流程（读卡失败、EMV 不可恢复错误、应用崩溃防护）是否在 Edge Cases 或 FR 中有明确需求？[Coverage, Spec §Edge Cases]
- [ ] CHK020 - 恢复流程（取消/超时后重试、卡片移开后重试）是否在规格中定义用户路径与系统行为？[Coverage, Spec §FR-004, §Edge Cases]

---

## Edge Case Coverage（边界条件覆盖）

- [ ] CHK021 - 读卡过程中卡片移开/拔出的检测时机与提示行为是否在规格中定义？[Gap, Spec §Edge Cases]
- [ ] CHK022 - 交易处理中“异常/不可恢复错误”的结束方式与展示要求是否明确？[Clarity, Spec §Edge Cases]
- [ ] CHK023 - 金额输入非法（空、0、超限、格式错误）的校验规则与提示是否在 FR-001 或 Edge Cases 中完整列出？[Completeness, Spec §FR-001, §Edge Cases]
- [ ] CHK024 - 设备不支持某种读卡方式时，是否在规格中定义“清晰提示”与“换方式/重试”的边界？[Gap, Spec §FR-007]

---

## Non-Functional Requirements（非功能需求）

- [ ] CHK025 - 性能需求（如 60 秒内完成）是否覆盖读卡、EMV、网络（若适用）各阶段？[Coverage, Spec §SC-001]
- [ ] CHK026 - 稳定性需求（Crash-free）是否明确“应用不崩溃”的边界（如：native 库加载失败、DAL 不可用）？[Clarity, Spec §SC-002, §Edge Cases]
- [ ] CHK027 - UI 资源一致性（FR-008）的“核心页面”范围是否在规格中枚举？[Completeness, Spec §FR-008]

---

## Dependencies & Assumptions（依赖与假设）

- [ ] CHK028 - 读卡与 EMV 依赖（NeptuneLiteAPI、EMV 库）的“能力边界”是否在 Dependencies 中说明？[Assumption, Spec §Dependencies]
- [ ] CHK029 - “设备读卡与交易处理能力由公司内部提供”的假设是否包含 native 库加载顺序等前置条件？[Assumption, Spec §Assumptions, plan.md]
- [ ] CHK030 - UI_Resource 的“核心页面”与资源缺失时的回退行为是否在规格或契约中定义？[Gap, Spec §Dependencies]

---

## Ambiguities & Conflicts（歧义与冲突）

- [ ] CHK031 - “可定位问题的原因摘要”与“面向测试人员可读”是否在规格中给出示例或格式约定？[Ambiguity, Spec §FR-005, §Assumptions]
- [ ] CHK032 - FR-007（SHOULD 支持多种读卡方式）与设备能力不支持时的降级行为是否一致？[Consistency, Spec §FR-007]

---

## Notes

- 勾选完成：`[x]`
- 可在项后添加注释或发现
- 引用规格章节时使用 `[Spec §FR-001]` 等格式
- 本清单校验**需求质量**，不校验实现行为
