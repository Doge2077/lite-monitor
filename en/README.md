

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
  <a href="https://github.com/Doge2077/lite-monitor/blob/main/README.md">简体中文</a>
  ｜
  English
</p>

## About Lite-monitor

****

This is a fast, accurate, lightweight server monitoring system with second-level monitoring granularity, supporting historical data view for multi-platform server users:

![](https://image.itbaima.cn/images/40/image-20240422179520537.png)

Supports one-click SSH to the target host, which is convenient for quick operations:

![](https://lys2021.com/wp-content/uploads/2024/04/image-20240422189124020.png)

Supports multi-user management of different hosts:

![](https://image.itbaima.cn/images/40/image-20240422177570347.png)

****

## Server Deployment

****

### Environment Dependencies

****

- JDK17
- SpringBoot3
- Vue3
- MySQL 5.7+
- Redis
- InfluxDB
- RabbitMQ

****

### Manual Deployment

****

MySQL Deployment:

- Set user `root`, password `monitormysqlroot`
- Create database `lite-monitor-db` with character set `utf8mb4`
- Run the import SQL script, the file is [`lite-monitor-db.sql`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-db.sql)

InfluxDB Deployment:

- Access the host's console on port `8086`
- Set the user to `admin` with the password `monitorinfluxdbadmin`
- Create a new Bucket with the name: `lite-monitor`, we recommend setting an expiration time of 7 days

RabbitMQ Deployment:

- Add user `admin`, password `monitorrabbitmqadmin`, virtual host `/`

Redis Deployment:

- No password validation required

Backend Deployment:

- Pull this project repository locally, run `maven` build for `lite-monitor-server`
- If database configurations differ from this example, please modify the configurations in `application-prod.yml`
- Package with `maven`, ensure `prod` environment config is selected and tests are skipped
- Upload the packaged backend `jar` to the monitoring host for running. The host requires `Java17` runtime environment and listens on port `8010` by default

Frontend Deployment:

- Configure the host `ip` in [`main.js`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/main.js) under `axios.defaults.baseURL`
- Configure the `ws` address in [`Terminal.vue`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/component/Terminal.vue) to the host `ip`
- Run `npm` build locally and upload the built frontend files to the host

Nginx Deployment:

- Configure based on actual needs

The default login user is `admin` with the default password `123456`, you can modify the email and password in the [Security] management page

****

### Docker Deployment

****

Pull this project repository locally

Modify frontend routing:

- Configure the host `ip` in [`main.js`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/main.js) under `axios.defaults.baseURL`
- Configure the `ws` address in [`Terminal.vue`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-web/src/component/Terminal.vue) to the host `ip`

Run [`docker-compose.yml`](https://github.com/Doge2077/lite-monitor/blob/main/docker-compose.yml) on the target server

MySQL Initialization:

- Run the import SQL script, the file is  [`lite-monitor-db.sql`](https://github.com/Doge2077/lite-monitor/blob/main/lite-monitor-db.sql)

InfluxDB Initialization:

- Access the host console on port `8086`
- Set user to `admin`, password `monitorinfluxdbadmin`
- Create a new Bucket named `lite-monitor`, we recommend a 7-day expiration time

The default login user is `admin` with the default password `123456`, you can modify the email and password on the [Security] management page

****

## Client Deployment

****

### Environment Dependencies

****

- JDK17
- SpringBoot3

****

### Manual Deployment

****

- Pull the repository locally, run `maven` build for `lite-monitor-client`
- Package with `maven`, ensure `prod` environment config is selected and tests are skipped
- Upload the packaged backend `jar` to the host that needs monitoring. The host needs the `Java17` runtime environment
- The first run will create a `config-local` directory in the current directory, requiring registration with the server. Enter server host `ip:port` and the `token` generated by the server 
- Support for registering the client as a `systemd` service, specific configurations can refer to other documentation