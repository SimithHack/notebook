---
标题: 使用docker 部署redis-sentinel集群
时间: 2019-09-06
作者: xiefq
---

<!-- TOC -->

- [1. 前提概要](#1-前提概要)
- [2. 计划](#2-计划)
- [3. 步骤](#3-步骤)
    - [3.1. redis节点](#31-redis节点)
        - [3.1.1. 配置文件](#311-配置文件)
        - [3.1.2. redis节点的dockerfile](#312-redis节点的dockerfile)
        - [3.1.3. 编译镜像并运行，注意privileged=true](#313-编译镜像并运行注意privilegedtrue)
        - [3.1.4. 主从关系创建](#314-主从关系创建)
    - [3.2. sentinel节点配置](#32-sentinel节点配置)
        - [3.2.1. 配置文件](#321-配置文件)
        - [3.2.2. dockerfile](#322-dockerfile)
        - [3.2.3. 编译并启动](#323-编译并启动)

<!-- /TOC -->

# 1. 前提概要
> 准备在一台虚拟机下安装一个redis master-slave集群，并且通过哨兵来管理集群，目的是为了更好的学习和工作开发

虚拟机配置
```text
CPU: 双核
RAM：2G
OS : centos7
hostname: master
```

# 2. 计划
+ sentinel节点
> 端口分别为 26379 26380 26381

+ redis节点
> 端口分别为 6379 6380 6381 其中6379为主节点，其余两个为从节点

# 3. 步骤
1. 从官网拉去最新的redis镜像
```sh
docker pull redis:latest
```
2. 创建工作目录，里边准备如下文件以及内容
```sh
# docker file和配置文件目录
mkdir -p ~/redis
# 数据存放目录
mkdir -p /data/redis
```
## 3.1. redis节点
### 3.1.1. 配置文件
redis-6379.conf，redis-6380.conf, redis-6381.conf 注意修改里边的端口
```conf
bind 0.0.0.0
protected-mode no
port 6381
tcp-backlog 511
timeout 0
tcp-keepalive 300
daemonize no
supervised no
pidfile /var/run/redis_6381.pid
loglevel notice
logfile "redis-6381.log"
databases 1
always-show-logo yes
#save 900 1
#save 300 10
#save 60 10000
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump-6381.rdb
dir /data/redis
replica-serve-stale-data yes
replica-read-only yes
repl-diskless-sync no
repl-diskless-sync-delay 5
repl-disable-tcp-nodelay no
replica-priority 100
lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no
appendonly yes
appendfilename "appendonly-6381.aof"
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes
aof-use-rdb-preamble yes
lua-time-limit 5000
slowlog-log-slower-than 10000
slowlog-max-len 128
latency-monitor-threshold 0
notify-keyspace-events ""
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
list-max-ziplist-size -2
list-compress-depth 0
set-max-intset-entries 512
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
hll-sparse-max-bytes 3000
stream-node-max-bytes 4096
stream-node-max-entries 100
activerehashing yes
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit replica 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60
hz 10
dynamic-hz yes
aof-rewrite-incremental-fsync yes
rdb-save-incremental-fsync yes
```
+ 我们禁用掉了rdb存储方案，使用AOF

### 3.1.2. redis节点的dockerfile
> 注意：redis-6379.conf redis-6380.conf redis-6381.conf 修改它们的不同的地方
```df
FROM redis
add redis-6380.conf /usr/local/etc/redis/
CMD [ "redis-server", "/usr/local/etc/redis/redis-6380.conf" ]
```

### 3.1.3. 编译镜像并运行，注意privileged=true
```sh
docker build -t redis:6379 -f redis-6379.df .
docker run --name=redis-6379 --network=host -d -v /data/redis:/data/redis --privileged=true redis:6379

docker build -t redis:6380 -f redis-6380.df .
docker run --name=redis-6380 --network=host -d -v /data/redis:/data/redis --privileged=true redis:6380

docker build -t redis:6381 -f redis-6379.df .
docker run --name=redis-6381 --network=host -d -v /data/redis:/data/redis --privileged=true redis:6381
```

### 3.1.4. 主从关系创建
> 注意不要使用127.0.0.1，要使用域名地址或者IP地址
```sh
docker exec -it redis:6379 /bin/bash
redis-cli -p 6380
replicaof master 6379
exit
redis-cli -p 6381
replicaof master 6379
```

## 3.2. sentinel节点配置
### 3.2.1. 配置文件
> sentinel-26379.conf sentinel-26380.conf sentinel-26381.conf 注意文件中相关配置的修改  
同时注意不要使用回环地址

```conf
port 26381
daemonize no
pidfile /var/run/redis-sentinel-26381.pid
logfile "sentinel-26381.log"
dir /data/redis
sentinel monitor mymaster master 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes
```

### 3.2.2. dockerfile
> sentinel-26379.conf sentinel-26380.conf sentinel-26381.conf 注意相关地方的修改
```df
FROM redis
add sentinel-26379.conf /usr/local/etc/redis/
CMD [ "redis-server", "/usr/local/etc/redis/sentinel-26379.conf" ]
```

### 3.2.3. 编译并启动
```sh
docker build -t sentinel:26379 -f sentinel:26379.df .
docker run --name=sentinel-26379 --network=host -d -v /data/redis:/data/redis --privileged=true sentinel:26379

docker build -t sentinel:26380 -f sentinel:26380.df .
docker run --name=sentinel-26380 --network=host -d -v /data/redis:/data/redis --privileged=true sentinel:26380

docker build -t sentinel:26381 -f sentinel:26381.df .
docker run --name=sentinel-26381 --network=host -d -v /data/redis:/data/redis --privileged=true sentinel:26381
```
