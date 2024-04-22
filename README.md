<p align="center">
  <a href="https://github.com/Doge2077/lite-monitor">
    <img src="https://image.itbaima.cn/images/40/image-2024042216963953.png" width="450"></a>
</p>
<p align="center"> 
  <a href="https://git.io/typing-svg"><img src="https://readme-typing-svg.demolab.com?font=Righteous&size=20&duration=3000&pause=1000&color=5CE1E6&center=true&vCenter=true&repeat=false&width=435&lines=Simple+Fast+Accurate+LowMemoryOccupying" alt="Typing SVG" /></a>
</p>
<p align="center">
  <img src="https://img.shields.io/badge/oshi-67E1E6?style=for-the-badge&logoColor=white" alt="" />
  <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="" />
  <img src="https://img.shields.io/badge/vue-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D" alt="" />
  <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white" alt="" />
  <img src="https://img.shields.io/badge/InfluxDB-22ADF6?style=for-the-badge&logo=InfluxDB&logoColor=white" alt="" />
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white" alt="" />
  <img src="https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white" alt="" />
  <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white" alt="" />
</p>

****

<p align="center">
  简体中文
  ｜
  <a href="https://github.com/krahets/hello-algo/blob/main/en/README.md">English</a>
</p>

## 关于 Lite-monitor

****

这是一个快速、准确、轻量化的服务器监控系统，拥有秒级的监控粒度，支持历史数据查看便于拥有多平台服务器的用户集中管理：

![](https://image.itbaima.cn/images/40/image-20240422179520537.png)

支持一键 SSH 到目标主机，便于快速操作：

![](https://image.itbaima.cn/images/40/image-20240422189124020.png)

支持多用户管理不同主机：

![](https://image.itbaima.cn/images/40/image-20240422177570347.png)

****

## 服务端部署

****

### 环境依赖

****

- JDK17
- SpringBoot3
- Vue3
- MySQL 5.7+
- Redis
- InfluxDB
- RabbitMQ

****

### 手动部署

****

部署 MySQL：

- 设置用户 `root`，密码 `monitormysqlroot`
- 创建数据库 `lite-monitor-db`，字符集设置为 `utf8mb4`
- 执行导入 SQL 脚本，文件为 [`lite-monitor-db.sql`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-db.sql)

部署 InfluxDB：

- 访问主机的 `8086` 端口进入 InfluxDB 的控制台
- 设置用户为 `admin`，密码 `monitorinfluxdbadmin`
- 创建新的 Bucket，名称为：`lite-monitor`，推荐设置过期时间为 7 天

部署 RabbitMQ：

- 添加用户 `admin`，密码 `monitorrabbitmqadmin`，虚拟主机 `/`

部署 Redis：

- 无需设置密码验证

部署后端：

- 拉取本项目仓库到本地，对 `lite-monitor-server` 执行 `maven` 构建
- 如果数据库等配置不与本例相同，请注意修改 `application-prod.yml` 中的配置
- 使用 `maven` 打包，注意勾选 `prod` 环境配置并跳过测试
- 将打包好的后端 `jar` 上传到管理监控的主机执行，主机需要 `Java17` 运行环境，默认监听 `8010` 端口

部署前端：

- 配置 [`main.js`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/main.js) 的 `axios.defaults.baseURL` 中的主机 `ip`
- 配置 [`Terminal.vue`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/component/Terminal.vue) 的 `ws` 地址为主机 `ip`
- 本地执行 `npm` 构建，将打包后的前端文件上传到主机

部署 Nginx：

- 根据实际需要进行配置

默认登录用户为 `admin`，密码默认为 `123456`，可在【安全】管理界面修改邮箱和密码

****

### Docker 部署

****

拉取本项目仓库到本地

修改前端路由：

- 配置 [`main.js`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/main.js) 的 `axios.defaults.baseURL` 中的主机 `ip`
- 配置 [`Terminal.vue`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/component/Terminal.vue) 的 `ws` 地址为主机 `ip`

执行 [`docker-compose.yml`](https://github.com/Doge2077/lite-monitor/blob/main/docker-compose.yml) 到目标服务器

初始化  MySQL：

- 执行导入 SQL 脚本，文件为  [`lite-monitor-db.sql`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-db.sql)

初始化 InfluxDB:

- 访问主机的 `8086` 端口进入 InfluxDB 的控制台
- 设置用户为 `admin`，密码 `monitorinfluxdbadmin`
- 创建新的 Bucket，名称为：`lite-monitor`，推荐设置过期时间为 7 天

默认登录用户为 `admin`，密码默认为 `123456`，可在【安全】管理界面修改邮箱和密码

****

## 客户端部署

****

### 环境依赖

****

- JDK17
- SpringBoot3

****

### 手动部署

****

- 拉取本项目仓库到本地，对 `lite-monitor-client` 执行 `maven` 构建
- 使用 `maven` 打包，注意勾选 `prod` 环境配置并跳过测试
- 将打包好的后端 `jar` 上传到需要被监控的主机执行，主机需要 `Java17` 运行环境
- 首次运行会在当前目录创建 `config-local` 目录，并要求注册到服务端，输入服务端主机 `ip:port` 和服务端生成的 `token` 即可
- 支持将客户端以 `systemd` 方式注册为服务，具体配置可参考其他资料

****
