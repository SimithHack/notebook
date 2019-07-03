# compose 安装
+ 1 下载
```sh
wget https://dl.bintray.com/docker-compose/master/:docker-compose-Linux-x86_64
```

+ 2 赋予执行权限，并加入执行路径
```sh
mv docker-compose-Linux-x86_64 docker-composer
chmod +x docker-composer
sudo ln -s /home/xiefq/soft/docker/docker-compose /usr/bin/docker-compose
```
验证安装成功与否
```sh
[xiefq@localhost ~]$ docker-compose --version
docker-compose version 1.25.0dev, build cf419dce
```

# compose方式安装zookeeper集群(单机集群模式)
## 创建数据和日志目录
```sh
mkdir -p /data/zk/data/1 && \
  mkdir -p /data/zk/data/2 && \
  mkdir -p /data/zk/data/3 && \
  mkdir -p /data/zk/logs/1 && \
  mkdir -p /data/zk/logs/2 && \
  mkdir -p /data/zk/logs/3
```

## 准备compose

[zookeeper.yml]
```yml
version: '3.1'

services:
  zoo1:
    image: zookeeper
    restart: always
    hostname: zoo1
    ports:
      - 2181:2181
    volumes:
      - /data/zk/data/1:/data
      - /data/zk/logs/1:/datalog
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=0.0.0.0:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=zoo3:2888:3888;2181

  zoo2:
    image: zookeeper
    restart: always
    hostname: zoo2
    ports:
      - 2182:2181
    volumes:
      - /data/zk/data/2:/data
      - /data/zk/logs/2:/datalog
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=0.0.0.0:2888:3888;2181 server.3=zoo3:2888:3888;2181

  zoo3:
    image: zookeeper
    restart: always
    hostname: zoo3
    ports:
      - 2183:2181
    volumes:
      - /data/zk/data/3:/data
      - /data/zk/logs/3:/datalog
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=0.0.0.0:2888:3888;2181
```

## 启动构建
```sh
docker-compose -f zookeeper.yml up
```