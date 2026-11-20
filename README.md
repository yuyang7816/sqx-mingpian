<div align="center">

# **省钱兄 JAVA 名片分享系统全套开源（JAVA + Uni-app + Vue）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-功能说明.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F.svg)](docs/服务端-功能说明.md)
[![Vue](https://img.shields.io/badge/Vue-2.5-42b883.svg)](mingpian-admin/package.json)
[![uni-app](https://img.shields.io/badge/uni--app-用户端-2b9939.svg)](mingpian-uniapp/manifest.json)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄名片分享系统全套开源**是一套面向 **个人数字名片、企业名片与人脉交换** 的应用，内置 **名片创建与样式切换、名片夹与互换、人脉搜索、名片榜单、企业数字名片与成员管理、拍摄识别、会员 VIP、钱包与提现、邀请团队、AI 核查、站内消息与客服** 等能力，采用前后端分离架构，**服务端、管理端与 C 端用户应用** 源码完整开放。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）与 **MyBatis-Plus** 构建，单模块工程，负责 REST 接口、定时任务、数据库访问，以及短信、对象存储、支付、地图、AI 核查等外部能力。  
管理端基于 **Vue 2 + Element UI + Webpack** 开发，供平台运营在电脑浏览器中维护 **用户与名片审核、企业与模板、认证、会员与订单、财务、装修与系统配置** 等。登录页名称是「**省钱兄名片分享系统**」。  
用户端基于 **uni-app（Vue 3）** 开发，应用名「**名片分享**」，覆盖 **H5** 与微信小程序。底部导航为 **首页 · 名片夹 · 拍摄名片 · 企业 · 我的**。


- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-mingpian.git](https://gitee.com/maxiaodong1996/sqx-mingpian.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-mingpian.git](https://github.com/yuyang7816/sqx-mingpian.git)


## **2. 移动端界面预览**

下面五张图是当前用户端真实页面：首页、名片夹、企业、我的、登录。

<div align="center">

<table>
<tr>
<td align="center" style="padding:4px 6px;"><img src="https://mpfx.xianmaxiong.com/file/uploadPath/2026/11/20/dfd761f63a0075ea0c8abee886f207bf.png" width="160" alt="首页" style="display:block;border-radius:6px;" /><br/>首页</td>
<td align="center" style="padding:4px 6px;"><img src="https://mpfx.xianmaxiong.com/file/uploadPath/2026/11/20/18d082cee4853406cc8855bc75af08f3.png" width="160" alt="名片夹" style="display:block;border-radius:6px;" /><br/>名片夹</td>
<td align="center" style="padding:4px 6px;"><img src="https://mpfx.xianmaxiong.com/file/uploadPath/2026/11/20/305b02fab44d1cf391f669d4af718b8f.png" width="160" alt="企业" style="display:block;border-radius:6px;" /><br/>企业</td>
<td align="center" style="padding:4px 6px;"><img src="https://mpfx.xianmaxiong.com/file/uploadPath/2026/11/20/6d33cb4dc52cc508eb15a39e1ab6ed2c.png" width="160" alt="我的" style="display:block;border-radius:6px;" /><br/>我的</td>
<td align="center" style="padding:4px 6px;"><img src="https://mpfx.xianmaxiong.com/file/uploadPath/2026/11/20/82b5914d409d11877b5e2b89f64e3868.png" width="160" alt="登录" style="display:block;border-radius:6px;" /><br/>登录</td>
</tr>
</table>

</div>

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和移动端都要等服务端已经启动，才能登录和加载数据。

**说明：** 仓库里**不包含** `node_modules`（体积大、随系统生成）。克隆或下载后，在 **`mingpian-admin`**、**`mingpian-uniapp`** 目录下执行 `npm install`。详细步骤见 [docs](docs/服务端-运行教程.md)。

### **4.1 服务端**

安装 **JDK 8**、**Maven 3.6+**、**MySQL 5.7 或 8.0**。新建数据库 **`mpfx`**，导入 [`mingpian-service/db/mpfx.sql`](mingpian-service/db/mpfx.sql)。本地连接信息在 **`mingpian-service/src/main/resources/application-prod.yml`** 中配置，**JDBC 里的库名须与建库名相同**。

[`application.yml`](mingpian-service/src/main/resources/application.yml) 默认 **端口 7777**，上下文路径 **`/sqx_fast`**，激活环境是 **`prod`**（读的就是 `application-prod.yml`）。请按本机环境修改数据源地址、端口和密码。本机调试请连 `127.0.0.1`，不要使用仓库里曾经写过的远程库地址。

在 `mingpian-service` 目录执行 Maven 打包后，运行 **`com.sqx.SqxApplication`**。控制台出现 **「名片分享启动成功」** 即表示启动完成。接口文档：`http://127.0.0.1:7777/sqx_fast/swagger-ui.html`。

Redis 在 `application.yml` 中默认关闭（`spring.redis.open`、`sqx.redis.open` 均为 `false`）。若打开 Redis 缓存，请同步修改 `spring.redis` 段并先启动本机 Redis。

更完整的说明见 [服务端功能说明](docs/服务端-功能说明.md) 与 [服务端运行教程](docs/服务端-运行教程.md)。

### **4.2 管理端**

安装 **Node.js**（建议 18）。这个后台使用 **Webpack 4**。Node 17 及以上若出现 `error:0308010C:digital envelope routines::unsupported`，启动前执行：

```powershell
$env:NODE_OPTIONS="--openssl-legacy-provider"
```

进入 `mingpian-admin` 目录，**首次运行前**安装依赖并启动：

```bash
cd mingpian-admin
npm install
npm run dev
```

开发站点默认端口 **8001**（见 `config/index.js`）。请把 `src/utils/httpRequest.js` 里写死的线上域名改成 `http://127.0.0.1:7777/sqx_fast/`。只改代理不够，请求没有走代理前缀。

演示管理端账号：`admin` / `123456`（以库中数据为准）。登录页名称为「**省钱兄名片分享系统**」。

### **4.3 用户端（C 端）**

**HBuilderX（推荐）：** 打开 `mingpian-uniapp` 目录，运行到浏览器或微信开发者工具。应用名称是「**名片分享**」，底部为 **首页 · 名片夹 · 拍摄名片 · 企业 · 我的**。

`uni_modules/uview-plus` 已经在项目里。日历和日期选择还会引用 npm 包 `dayjs`。首次运行前在用户端目录安装依赖，否则会报 `Failed to resolve import "dayjs/esm/index"`：

```bash
cd mingpian-uniapp
npm install
```

开发时请确认 [`mingpian-uniapp/common/config.js`](mingpian-uniapp/common/config.js) 中的接口地址指向本机或已部署的服务端。文件里现为线上地址 `https://mpfx.xianmaxiong.com/sqx_fast`。本地联调改为：

```javascript
const ROOTPATH1 = "http://127.0.0.1:7777/sqx_fast";
const ROOTPATH = "http://127.0.0.1:7777/sqx_fast";
```

用户端账号需要自行注册。验证码依赖短信密钥，未配置时验证码发不出去。

短信、微信、支付、对象存储、地图、AI 核查等密钥需自行申请，并在管理端「系统配置」填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录只放三端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要提交。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `mingpian-service` | **服务端**。接口、权限、名片、企业、支付、消息 |
| `mingpian-admin` | **管理端**。平台运营在电脑浏览器里打开的后台 |
| `mingpian-uniapp` | **用户端（C 端）**。首页、名片夹、企业、我的 |
| `docs` | 功能说明、运行教程与移动端预览图 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `mingpian-service`**

| 路径 | 是什么 |
|---|---|
| `src/main/java/com/sqx/SqxApplication.java` | 启动入口 |
| `src/main/java/com/sqx/modules/businessCard` | 个人名片 |
| `src/main/java/com/sqx/modules/companyCard` | 企业名片、成员与相关数据 |
| `src/main/java/com/sqx/modules/cardList` | 名片夹、收藏与互换 |
| `src/main/java/com/sqx/modules/verify` | AI 核查 |
| `src/main/java/com/sqx/modules/vipDetails` | 会员 |
| `src/main/java/com/sqx/modules/pay` | 支付 |
| `src/main/java/com/sqx/modules/sys` | 登录、角色、菜单、系统配置 |
| `src/main/resources` | `application.yml`、`application-prod.yml`、`application-dev.yml` |
| `db/mpfx.sql` | 数据库脚本。库名 `mpfx` |

同级还有企业动态、模板、访客、邀请、消息、帮助、Banner、文件上传等包，都在 `com.sqx.modules` 下。

### **6.3 管理端 `mingpian-admin`**

| 路径 | 是什么 |
|---|---|
| `src/views/common` | 登录页、首页 |
| `src/views/user` | 用户列表与详情 |
| `src/views/autonym` | 名片、认证、企业相关页面 |
| `src/views/banner`、`src/views/fitment` | Banner 与首页装修 |
| `src/views/finance`、`src/views/orderCenter` | 财务与订单 |
| `src/views/modules/sys` | 管理员、角色、菜单、字典、系统配置 |
| `src/utils/httpRequest.js` | 请求地址。本地调试要改成 `127.0.0.1:7777` |
| `config/index.js` | 开发端口 `8001`、代理配置 |

路由里还能看到陪玩、美食、礼品一类页面，那是后台模板留下来的。左侧菜单里没有的，日常不用。名片分享以用户、名片、企业、会员、订单、财务、系统配置为主。

### **6.4 用户端 `mingpian-uniapp`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页：人脉搜索、我的名片、添加名片、访问数据 |
| `pages/Business` | 名片夹：最近添加、姓名排序、联系次数、分组 |
| `pages/Enterprise` | 企业数字名片介绍与企业主页 |
| `pages/Shoot` | 拍摄名片 |
| `pages/my` | 我的：访客、会员、AI 核查、邀请、客服、设置 |
| `pages/public` | 登录、注册、找回密码 |
| `my` | 会员、钱包、榜单、认证、企业分包、设置等 |
| `common/config.js` | 接口地址 |
| `pages.json` | 页面路由与底部导航 |
| `manifest.json` | 应用名称「名片分享」、小程序与 H5 配置 |
| `uni_modules/uview-plus` | 界面组件 |
| `package.json` | `dayjs`、`clipboard`。运行前执行 `npm install` |

界面组件使用 **uView Plus**。按钮和弹窗只负责界面，收款和发短信由服务端按系统配置去调用。

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **首页人脉搜索、添加名片、更换样式、切换名片与分享**  
· **名片夹收藏、最近添加、姓名排序、联系次数与分组**  
· **拍摄名片，把纸质名片信息落到数字名片**  
· **企业数字名片、成员邀请、企业动态与名片数据**  
· **最近访客、最近查看、最近邀请、互换次数**  
· **名片榜单**  
· **手机号验证码登录、注册与找回密码**  
· **VIP 会员、钱包余额、充值与提现申请**  
· **邀请好友与团队分销**  
· **AI 核查企业、认证中心、意见反馈与帮助中心**  
· **管理端审核名片与认证、维护样式模板、会员价格、订单财务与系统配置**  
· **图片按系统配置存到本机，或阿里云 OSS、七牛、腾讯云 COS**

业务细节见 [服务端功能说明](docs/服务端-功能说明.md)、[用户端功能说明](docs/用户端-功能说明.md) 与 Swagger `swagger-ui.html`。

版本以仓库里的配置文件为准。没写死版本号的，按上文快速开始安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端 |
| 框架 | **Spring Boot** | **2.6.11** | 提供接口，默认端口 `7777`，路径 `/sqx_fast` |
| 持久化 | **MyBatis-Plus** | **3.2.0** | 读写 MySQL |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 5.7 或 8.0 | 业务数据，脚本见 `db/mpfx.sql`，库名 `mpfx` |
| 连接池 | **Druid** | 随工程 | 管理数据库连接 |
| 缓存 | **Redis** | 可选，默认关闭 | 登录态、缓存等 |
| 接口文档 | **Swagger** | 随工程 | 浏览器打开 `swagger-ui.html` |
| 安全 | **Shiro + JWT** | 随工程 | 管理端与 App 认证 |
| 对象存储 | 本地 / 阿里云 OSS / 七牛 / 腾讯云 COS | 随工程 | 头像、名片图、企业资质 |
| 支付 | 微信、支付宝、余额 | 随工程 | 会员与充值。密钥需自行申请 |
| 短信 | 腾讯云、阿里云或短信宝 | 随工程 | 注册、登录、找回密码 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | 18 推荐 | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | **2.5.16** | 后台每一个页面 |
| 构建 | **Webpack** | **4.47** | 开发时编译，默认端口 `8001` |
| 界面组件 | **Element UI** | **2.8.2** | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | **3.0** | 登录页和各业务页的地址 |
| 状态 | **Vuex** | **3.0** | 记住登录等信息 |
| 请求 | **Axios** | **0.17** | 把操作发给服务端 |
| 图表 | **ECharts** | 随工程 | 数据中心 |

### **7.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | Vue 3 | 同一套页面可运行到浏览器、微信小程序、App |
| 开发工具 | **HBuilderX** | 正式版 | 编译并运行 |
| 界面组件 | **uView Plus** | 随工程 | 按钮、表单、弹窗、日期选择 |
| 日期 | **dayjs** | **^1.11.3** | 日历与日期选择，需 `npm install` |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端 |
| 微信小程序 | 微信开发者工具 | 可选 | 需自己的小程序 AppId |

## **8. 开源与安全说明**

本仓库为**对外开源**版本。提交前请自查：

| 类型 | 处理方式 |
|---|---|
| 数据库账号 | `application-prod.yml` 连本机 MySQL。地址、端口、密码改成你自己的，不要提交生产库账号 |
| JWT 密钥 | `application.yml` 中为占位字符串，**上线前必须修改** |
| `db/mpfx.sql` | 开源包里的客服微信、地图 Key、短信与支付密钥等已清空，导入后到管理端「系统配置」填写 |
| 管理端 / 用户端默认接口 | 仓库里可能仍指向线上域名。本地调试改为 `127.0.0.1:7777`，勿把含生产域名和密钥的私有配置提交到 Git |

若你曾在公开仓库误提交过密码，请**立即轮换**数据库、微信、支付宝、OSS 等全部密钥。

## **9. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **10. 社区与支持**

运行、二次开发或商务合作，可以联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付、对象存储等密钥需要自行申请，并在管理端系统配置中填写。仍解决不了时，可通过官方渠道说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="https://tu.mianyazuju.com/2026/11/18/9740a7b2-c829-4e90-bc83-f787b2b5eeac.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
