---
标题: zookeeper环境搭建
时间: 2019-10-12
作者: xiefq
---

<!-- TOC -->

- [1. zookeeper搭建](#1-zookeeper搭建)
    - [1.1. 要点](#11-要点)
    - [1.2. 服务规划](#12-服务规划)
    - [1.3. 准备配置文件](#13-准备配置文件)
    - [1.4. 准备dockerfile](#14-准备dockerfile)
    - [1.5. 配置详解](#15-配置详解)
    - [1.6. 例子](#16-例子)
    - [1.7. 全部脚本文件](#17-全部脚本文件)

<!-- /TOC -->

# 1. zookeeper搭建
[参考官网](https://hub.docker.com/_/zookeeper)

## 1.1. 要点
+ 数据挂载目录 /data和/datalog
+ 配置文件挂载目录 /conf/zoo.cfg

## 1.2. 服务规划
|服务端口|名称|myid
|:--|:--|:--|
|2181|zoo1|1|
|2182|zoo2|2|
|2183|zoo3|3|

## 1.3. 准备配置文件
+ zoo1.cfg
```cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data/1
clientPort=2181
server.1=0.0.0.0:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
```
+ zoo2.cfg
```cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data/2
clientPort=2182
server.1=zoo1:2888:3888
server.2=0.0.0.0:2888:3888
server.3=zoo3:2888:3888
```
+ zoo3.cfg
```cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data/3
clientPort=2183
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=0.0.0.0:2888:3888
```

## 1.4. 准备dockerfile
+ zoo1.df
```sh
FROM ubuntu:latest
ADD apache-zookeeper-3.5.5.tar.gz /
RUN tee /apache-zookeeper-3.5.5/conf/myid <<EOF
1
EOF
COPY zoo1.cfg /apache-zookeeper-3.5.5/conf/zoo.cfg
```

## 1.5. 配置详解
```text
tickTime： 基本事件单元，以毫秒为单位。这个时间是作为 Zookeeper 服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每隔 tickTime时间就会发送一个心跳。

dataDir：存储内存中数据库快照的位置，顾名思义就是 Zookeeper 保存数据的目录，默认情况下，Zookeeper将写数据的日志文件也保存在这个目录里。

clientPort： 这个端口就是客户端连接 Zookeeper 服务器的端口，Zookeeper会监听这个端口，接受客户端的访问请求。

initLimit： 这个配置项是用来配置 Zookeeper接受客户端初始化连接时最长能忍受多少个心跳时间间隔数，当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是10*2000=20 秒。

syncLimit： 这个配置项标识 Leader 与 Follower之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 5*2000=10 秒

server.A = B:C:D : A表示这个是第几号服务器, B 是这个服务器的 ip 地址；
C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；
D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader
```

## 1.6. 例子
+ 准备数据目录
```sh
sudo mkdir -p /data/zookeeper/1
sudo mkdir -p /data/zookeeper/2
sudo mkdir -p /data/zookeeper/3
sudo chmod -R 777 /data/zookeeper
```
+ 准备配置文件
```sh
for i in `seq 1 3`
do
tee zoo1.cfg << EOF
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data
clientPort=218${i}
server.1=master:2888:3888;2181
server.2=master:2888:3888;2182
server.3=master:2888:3888;2183
EOF
done
```

+ 准备dockerfile文件
```sh
for i in `seq 1 3`
do
sudo echo ${i} > /data/zookeeper/${i}/myid
sudo chmod 777 /data/zookeeper/${i}/myid
tee zoo${i}.df << EOF
    FROM ubuntu:latest
    ADD ./apache-zookeeper-3.5.5-bin.tar.gz /
    ADD ./openjdk-8u40-b25-linux-x64-10_feb_2015.tar.gz /
    ENV JAVA_HOME=/java-se-8u40-ri
    ENV PATH=${PATH}:${JAVA_HOME}/bin
    COPY ./zoo${i}.cfg /apache-zookeeper-3.5.5-bin/conf/zoo.cfg
    ENTRYPOINT ["/apache-zookeeper-3.5.5-bin/bin/zkServer.sh", "start-foreground"]
EOF
done
```

+ 编译
```sh
i=1
for f in `ls | grep df`
do
docker build -t zoo${i}:latest -f ${f} .
let i=i+1
done
```

+ 运行
```sh
for i in `seq 1 3`
do
docker run -d --name=zoo${i} --network=host --hostname=zoo${i} -v /data/zookeeper/${i}:/data zoo${i}:latest
done
```

+ 清除
```sh
docker stop zoo1 zoo2 zoo3
docker rm zoo1 zoo2 zoo3
docker rmi zoo1 zoo2 zoo3
```

## 1.7. 全部脚本文件
cluster.sh
```sh
workdir=`pwd`

echo "安装开始"
echo "清除环境"
docker stop zoo1 zoo2 zoo3
docker rm zoo1 zoo2 zoo3
docker rmi zoo1 zoo2 zoo3

echo "创建数据目录"
sudo mkdir -p /data/zookeeper/1
sudo mkdir -p /data/zookeeper/2
sudo mkdir -p /data/zookeeper/3
sudo chmod -R 777 /data/zookeeper

echo "写入配置文件"
for i in `seq 1 3`
do
tee zoo${i}.cfg << EOF
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data
clientPort=218${i}
server.1=master:2881:3881
server.2=master:2882:3882
server.3=master:2883:3883
EOF
done

echo "写入dockerfile文件"
for i in `seq 1 3`
do
sudo echo ${i} > /data/zookeeper/${i}/myid
sudo chmod 777 /data/zookeeper/${i}/myid
tee zoo${i}.df << EOF
FROM ubuntu:latest
ADD /apache-zookeeper-3.5.5-bin.tar.gz /
ADD openjdk-8u40-b25-linux-x64-10_feb_2015.tar.gz /
ENV JAVA_HOME=/java-se-8u40-ri
ENV PATH=${PATH}:${JAVA_HOME}/bin
COPY zoo${i}.cfg /apache-zookeeper-3.5.5-bin/conf/zoo.cfg
ENTRYPOINT ["/apache-zookeeper-3.5.5-bin/bin/zkServer.sh", "start-foreground"]
EOF
done

echo "编译镜像"
i=1
for f in `ls | grep df`
do
docker build -t zoo${i}:latest -f ${f} .
let i=i+1
done

sleep 5s

echo "启动容器"
for i in `seq 1 3`
do
docker run -d --name=zoo${i} --network=host --hostname=zoo${i} -v /data/zookeeper/${i}:/data -p 288${i}:2888 -p 388${i}:3888 zoo${i}:latest
done

echo "安装完成"
```