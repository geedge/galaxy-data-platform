# 银河数据服务平台
[![](https://img.shields.io/badge/MIT-License-blue)](https://git.mesalab.cn/galaxy/galaxy-data-platform/-/blob/master/LICENSE)
[![](https://img.shields.io/badge/SpringBoot-2.2.0.RELEASE-green)](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/index.html)
## Introduction
统一的接口支持流式、批量作业，提供交互式SQL分析和例行调度，让您更方便编写和提交ETL程序，高效执行大数据计算程序，让大数据处理变得如此简单。
## Technology stack
**Spring Boot 2.2.0.RELEASE** https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/index.html

**Spring Boot Admin 2.3.0** https://codecentric.github.io/spring-boot-admin/2.3.0/
## 系统架构图
![系统架构图](images/architecture-v2.0.jpg)
## 启动注意事项
idea下启动项目需要设置一个启动参数,具体操作如下:
Edit Configuration > Environment > Working directory: **$MODULE_DIR$**
