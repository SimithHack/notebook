---
标题: docker搭建rabbitmq
时间: 2019-10-11
作者: xiefq
---

<!-- TOC -->

- [1. docker搭建rabbitmq](#1-docker搭建rabbitmq)
    - [1.1. 单机搭建](#11-单机搭建)
        - [1.1.1. 常用案例](#111-常用案例)
        - [1.1.2. 例子](#112-例子)
        - [1.1.3. rabbitmq的用户tags](#113-rabbitmq的用户tags)

<!-- /TOC -->

# 1. docker搭建rabbitmq

## 1.1. 单机搭建
+ 创建数据目录
```sh
sudo mkdir -p /data/rabbitmq/master
sudo chmod 777 /data/rabbitmq/master
```
+ 配置参考
[docker hub官方提供的操作指南](https://hub.docker.com/_/rabbitmq)

+ 自定义覆盖配置文件的目录
/etc/rabbitmq/rabbitmq.conf

### 1.1.1. 常用案例
+ 默认配置运行
```sh
docker run -d --hostname my-rabbit --name some-rabbit rabbitmq:3
```

+ 挂载数据目录
```sh
docker run -d --hostname my-rabbit --name some-rabbit -v /data/rabbitmq:/var/lib/rabbitmq rabbitmq:3
```

+ 限制内存
```sh
-e RABBITMQ_VM_MEMORY_HIGH_WATERMARK=0.4 指定一个百分比，相对于容器的内存
```

+ erlang cookie
```sh
-e RABBITMQ_ERLANG_COOKIE='secret cookie here'
```

+ 设置默认vhost
```sh
-e RABBITMQ_DEFAULT_VHOST=my_vhost
```

+ 指定默认用户名和密码
```sh
-e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password
```

+ 启用插件
```sh
-v /data/rabbitmq/enabled_plugins.conf:/etc/rabbitmq/enabled_plugins
```

### 1.1.2. 例子
```sh
sudo touch /data/rabbitmq/enabled_plugins
sudo chmod 777 /data/rabbitmq/enabled_plugins
sudo tee /data/rabbitmq/enabled_plugins << EOF
[rabbitmq_management].
EOF
docker run -d --hostname=master --name=rb-master --network=host \
-e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin \
-v /data/rabbitmq:/var/lib/rabbitmq \
-v /data/rabbitmq/enabled_plugins:/etc/rabbitmq/enabled_plugins rabbitmq:latest 
```
添加用户
```sh
docker exec -it rb-master /bin/bash
># rabbitmqctl add_user admin admin
># rabbitmqctl set_user_tags admin administrator management
```

### 1.1.3. rabbitmq的用户tags
```text
management ：访问 management plugin；  
policymaker ：访问 management plugin 和管理自己 vhosts 的策略和参数；  
monitoring ：访问 management plugin 和查看所有配置和通道以及节点信息；  
administrator ：一切权限；
```