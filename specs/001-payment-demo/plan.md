# Implementation Plan: 拍卡后 Clss_CoreInit_Entry UnsatisfiedLinkError 修复

**Branch**: `001-payment-demo` | **Date**: 2026-03-06 | **Spec**: [spec.md](./spec.md)  
**Input**: 拍卡后应用崩溃：`UnsatisfiedLinkError: No implementation found for int com.pax.jemv.entrypoint.api.ClssEntryApi.Clss_CoreInit_Entry()`

---

## Summary

**问题**: 挥卡（PICC）后进入 ProcessingActivity，调用 ClssProcess.preTransProcess() 时崩溃，报 `Clss_CoreInit_Entry` 的 native 实现未找到。  
**根因**: `PaymentDemoApp` 未调用 `EmvBase.loadLibrary()`，EMV/Clss 的 native 库（JNI_ENTRY_v103、F_ENTRY_LIB_PayDroid 等）从未被加载。参考项目 `EmvDemoApp` 在 `initEmvModule()` 中调用 `EmvBase.loadLibrary()`。  
**修复**: 在 `PaymentDemoApp` 启动时调用 `EmvBase.loadLibrary()`，确保在首次使用 ClssProcess 前完成 native 库加载。

---

## Technical Context

**Language/Version**: Java 8  
**Primary Dependencies**: NeptuneLiteAPI (sdk)、EMV 库 (emv)、commonlib  
**Target Platform**: Android 8+ (API 26)，PAX 等 32 位终端  
**Project Type**: mobile-app (Android)

---

## Phase 0: 根因分析

### 错误堆栈

```
UnsatisfiedLinkError: No implementation found for int com.pax.jemv.entrypoint.api.ClssEntryApi.Clss_CoreInit_Entry()
  at ClssEntryApi.Clss_CoreInit_Entry(Native Method)
  at ClssProcess.preTransProcess(ClssProcess.java:74)
  at EmvProcessor.doPreTrans(EmvProcessor.java:139)
  at EmvProcessor.process(...)
  at ProcessingActivity.onCreate(...)
```

### 代码审查结论

| 检查项 | 结论 |
|--------|------|
| EmvDemoApp | 在 `initEmvModule()` 中调用 `EmvBase.loadLibrary()` |
| PaymentDemoApp | **未调用** `EmvBase.loadLibrary()` |
| EmvBase.loadLibrary() | 加载 F_DEVICE_LIB、F_PUBLIC_LIB、F_EMV_*、**F_ENTRY_LIB、JNI_ENTRY_v103**、F_MC_*、F_WAVE_*、F_AE_* 等 |
| Clss_CoreInit_Entry | 由 JNI_ENTRY_v103 或 F_ENTRY_LIB 提供，需先 loadLibrary |

### 根因

`ClssEntryApi.Clss_CoreInit_Entry()` 为 JNI 方法，实现在 `JNI_ENTRY_v103` 或 `F_ENTRY_LIB_PayDroid` 中。这些库通过 `EmvBase.loadLibrary()` 加载。本应用未调用，导致首次执行 Clss 相关 native 方法时抛出 UnsatisfiedLinkError。

---

## Phase 1: 修复方案

### 修复点

**文件**: `app/src/main/java/com/payment/demo/app/PaymentDemoApp.java`

**修改**: 在 Application 启动时调用 `EmvBase.loadLibrary()`。参考 EmvDemoApp，在后台线程执行以避免阻塞主线程。

```java
// 在 onCreate 中增加
initEmvLibs();

// 新增方法
private void initEmvLibs() {
    backgroundExecutor.execute(() -> {
        try {
            com.paxsz.module.emv.process.EmvBase.loadLibrary();
            Log.d(TAG, "EMV libs loaded");
        } catch (Throwable t) {
            Log.e(TAG, "EMV loadLibrary failed", t);
        }
    });
}
```

### 验证步骤

1. 构建：`gradlew assembleDebug`
2. 安装到 PAX 真机
3. 输入金额 → 开始交易 → 挥卡
4. 应进入处理中页并完成 EMV 流程，不再崩溃

---

## Project Structure

```text
app/src/main/java/com/payment/demo/app/PaymentDemoApp.java  # 修复目标
emv/src/main/java/com/paxsz/module/emv/process/EmvBase.java # loadLibrary 定义
```
