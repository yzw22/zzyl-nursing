# 中州养老（zzyl）

基于 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 3.8.9 二次开发的养老院管理平台，仓库内包含后端服务与 Web 前端。

> 本仓库是学习/实战项目代码，仓库中的密钥已全部替换为占位值，运行前需自行配置。

## 技术栈

| 层次 | 选型 |
| --- | --- |
| 后端 | Spring Boot 3.5.3、Spring Security、MyBatis-Plus 3.5.7、Druid、Redis、JWT、Quartz |
| 前端 | Vue 3 + Vite 5 + Element Plus + Axios |
| 数据库 | MySQL 8.0 |
| 第三方能力 | 阿里云 OSS、百度千帆、OpenAI 兼容大模型（通义千问 / DeepSeek / Ollama）、华为云 IoTDA、微信小程序 |

## 目录结构

```
.
├── backend/                        # 后端（Maven 多模块）
│   ├── zzyl-admin/                 # 启动模块，含 application.yml、Dockerfile
│   ├── zzyl-framework/             # 框架配置：安全、JWT、拦截器
│   ├── zzyl-system/                # 系统管理：用户、角色、菜单、字典
│   ├── zzyl-common/                # 公共工具与基础类
│   ├── zzyl-nursing-platform/      # 养老业务：老人、床位、护理、设备
│   ├── zzyl-ai-agent/              # AI 能力：体检报告分析等
│   ├── zzyl-oss/                   # 阿里云 OSS 文件上传
│   ├── zzyl-quartz/                # 定时任务
│   └── zzyl-generator/             # 代码生成
└── frontend/                       # 前端（Vue 3 + Vite）
    ├── src/api/                    # 接口封装
    ├── src/App.vue
    └── vite.config.js              # 开发端口 5174，/nursing_project 代理到 8080
```

## 快速开始

### 1. 准备环境

- JDK 17+
- Maven 3.8+
- MySQL 8.0，创建数据库 `zzyl`（字符集 `utf8mb4`）
- Redis 6+
- Node.js 18+

### 2. 配置

```bash
cp .env.example .env    # 填入数据库密码、OSS/大模型/华为云/微信等密钥
```

配置项在 `backend/zzyl-admin/src/main/resources/application.yml` 中以 `${变量名:默认值}` 形式引用；
不设环境变量时，未填写的密钥会使用占位值，相关功能（OSS 上传、AI 分析、IoT 设备接入、小程序登录）不可用，其余功能不受影响。

主要配置项：

| 环境变量 | 说明 |
| --- | --- |
| `DB_USERNAME` / `DB_PASSWORD` | MySQL 账号密码，默认库 `zzyl` |
| `DRUID_USERNAME` / `DRUID_PASSWORD` | Druid 监控台登录账号 |
| `JWT_SECRET` | 令牌签名密钥，生产环境务必换成随机长字符串 |
| `OSS_ACCESS_KEY_ID` / `OSS_ACCESS_KEY_SECRET` | 阿里云 OSS 密钥 |
| `BAIDU_QIANFAN_API_KEY` | 百度千帆 API Key（体检报告分析） |
| `OPENAI_API_KEY` / `DEEPSEEK_API_KEY` | 大模型接口密钥 |
| `WECHAT_APP_ID` / `WECHAT_APP_SECRET` | 微信小程序 |
| `HUAWEI_*` | 华为云 IoTDA 设备接入 |

### 3. 启动后端

```bash
cd backend
mvn clean package -DskipTests
java -jar zzyl-admin/target/zzyl-admin.jar
```

或在 IDE 中运行 `com.zzyl.RuoYiApplication`，默认端口 `8080`。

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev          # http://localhost:5174
```

## 部署

`backend/zzyl-admin/Dockerfile` 提供了打包镜像的模板，构建前需先执行 `mvn package` 生成 `zzyl-admin.jar`：

```bash
docker build -t zzyl-admin:latest .
docker run -d -p 8080:8080 \
  -e DB_PASSWORD=*** -e JWT_SECRET=*** \
  -e OSS_ACCESS_KEY_ID=*** -e OSS_ACCESS_KEY_SECRET=*** \
  zzyl-admin:latest
```

## 说明

- 本项目基于 RuoYi 3.8.9 改造，遵循其 MIT 许可协议，见 [backend/LICENSE](backend/LICENSE)。
- 仓库内不包含数据库初始化脚本，需自行准备 `zzyl` 库的表结构与初始数据。
- 已移除所有真实密钥，请勿将真实密钥提交回仓库。
