# 蓝湖 MCP 修复总结

## 已完成的修复

### 1. 418 错误处理
- 在 `_get_designs_internal` 中捕获 HTTP 418，返回明确的中文提示
- 引导用户按 `GET-COOKIE-TUTORIAL.md` 更新 Cookie

### 2. Cookie 校验
- 新增 `_is_cookie_valid()`：检查 Cookie 非占位符且包含 `session=` 或 `user_token=`
- 启动时若 Cookie 无效，打印警告信息

### 3. 新增工具 `lanhu_validate_cookie`
- 用于验证 Cookie 是否有效
- 在 Cursor 中可让 AI 调用此工具进行自检

### 4. 文档
- `COOKIE-SETUP.md`：快速配置指南
- `test_cookie.py`：独立脚本，可脱离 Cursor 测试 Cookie

## 用户需执行的操作

**418 错误表示 Cookie 无效。请按以下步骤配置：**

1. 登录 https://lanhuapp.com
2. 按 F12 → Network → 刷新页面 → 复制任意请求的 Cookie 头
3. 编辑 `lanhu-mcp/.env`，设置：
   ```env
   LANHU_COOKIE="复制的完整Cookie内容"
   ```
4. 重启 lanhu-mcp 服务：
   ```bash
   cd lanhu-mcp
   .venv\Scripts\python.exe lanhu_mcp_server.py
   ```
5. 可选：运行 `python test_cookie.py` 验证 Cookie

## 配置完成后

在 Cursor 中让 AI 调用 `lanhu_get_designs`，传入蓝湖 URL 即可获取设计图列表。随后可调用 `lanhu_get_ai_analyze_design_result` 和 `lanhu_get_design_slices` 获取交易相关 UI 资源。
