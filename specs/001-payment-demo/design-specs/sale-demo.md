# SALE DEMO 设计规范

**来源**: 蓝湖项目 `pid=d6141275-bdd7-4aaa-a947-d2c52028f1d3`  
**获取日期**: 2026-03-09

## 切图与界面映射

| 切图资源 | 用途 | 对应界面 |
|----------|------|----------|
| sale_demo.png | 主界面参考/背景 | MainActivity |
| sale_demo_insert_tap_card.png | 插卡/挥卡综合提示 | ReadCardActivity（多模式） |
| sale_demo_icon_tap_card.png | 挥卡图标 | ReadCardActivity（仅 PICC） |
| sale_demo_icon_insert_card.png | 插卡图标 | ReadCardActivity（仅 ICC） |
| sale_demo_icon_swipe_card.png | 刷磁条图标 | ReadCardActivity（仅 MAG） |
| sale_demo_processing.png | 处理中 | ProcessingActivity |
| sale_demo_please_enter_pin.png | 输入 PIN | ProcessingActivity（PIN 状态） |
| sale_demo_successful_transaction.png | 交易成功 | ResultActivity（成功） |
| sale_demo_deal_failed.png | 交易失败 | ResultActivity（失败） |

## 布局建议

- 主入口：参考 sale_demo 布局，金额输入居中，按钮醒目
- 读卡引导：按读卡模式切换图标（PICC→icon_tap_card, ICC→icon_insert_card, MAG→icon_swipe_card）
- 处理中：默认 processing，PIN 时切换 please_enter_pin
- 结果页：成功用 successful_transaction，失败用 deal_failed
