# IM 简易即时聊天系统(as-chat)

---

## 阶段一： Version 1 - 基于短连接轮询的简易聊天系统
[Release-1.0](https://github.com/kds908/as-chat/releases/tag/v1.0)

### 功能点
- 用户登录（简易未加密）
- 双方简易文本内容聊天
- 联系人列表
- 未读信息数（总未读和会话未读）
- 消息与联系人以及未读数量刷新

### 说明
该版本采用短轮询，每三秒刷新一次消息与状态信息

### 阶段使用技术
- Spring Boot
- JPA
- Redis
- MySQL
- Thymeleaf
- bootstrap

## 阶段二： Version 2 - 基于 WebSocket 长连接与 ACK 的简易聊天系统
进行中~~


## 阶段三：简单聊天系统功能扩展git 


