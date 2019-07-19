# 下载文件
[es7.2](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.0-linux-x86_64.tar.gz)
[kibana7.2](https://artifacts.elastic.co/downloads/kibana/kibana-7.2.0-linux-x86_64.tar.gz)

# 制作镜像
## es集群镜像制作
+ dockerfile
```config
FROM ubuntu
ADD elasticsearch-7.2.0-linux-x86_64.tar.gz /
ADD sysctl.conf /etc/
ADD limits.conf /etc/security/
ENV JAVA_HOME=/elasticsearch-7.2.0/jdk/ \
    PATH=$PATH:/elasticsearch-7.2.0/jdk//bin
COPY elasticsearch.yml /elasticsearch-7.2.0/config/
COPY jvm.options /elasticsearch-7.2.0/config/
COPY script.sh /
RUN mkdir /data && \
    mkdir /logs && \
    chmod +x /script.sh
VOLUME ["/data", "/logs"]
ENTRYPOINT ["/script.sh"]
```
+ elasticsearch.yml
```yml
cluster.name: xfq
#node.attr.rack: r1
node.name: node1
path.data: /data
path.logs: /logs
network.host: 0.0.0.0
bootstrap.memory_lock: false
http.port: 9200
discovery.seed_hosts: ["master", "slave01", "slave02"]
cluster.initial_master_nodes: ["node1", "node2"]
gateway.recover_after_nodes: 3
# 必须制定具体的index名才能删除
action.destructive_requires_name: true
```
+ script.sh
```sh
#!/bin/bash
echo -e "\033[34m 配置重要环境变量 \033[0m"
useradd elasticsearch -p es
chown -R elasticsearch:elasticsearch /elasticsearch-7.2.0
chown -R elasticsearch:elasticsearch /data
chown -R elasticsearch:elasticsearch /logs
echo -e "\033[34m 启动ElasticSearch \033[0m"

su elasticsearch << EOF
/elasticsearch-7.2.0/bin/elasticsearch -d -p pid
EOF
```

+ limits.conf
```conf
elasticsearch  hard  nofile  65535
elasticsearch  soft  nofile  65535
```

+ sysctl.conf
> 这个文件需要在在主机里运行
```conf
vm.max_map_count=262144
```
立马生效的话，需要执行
sudo sysctl -w vm.max_map_count=262144

+ jvm.options
```
-Xms1g
-Xmx1g

-XX:+UseConcMarkSweepGC
-XX:CMSInitiatingOccupancyFraction=75
-XX:+UseCMSInitiatingOccupancyOnly

-Des.networkaddress.cache.ttl=60
-Des.networkaddress.cache.negative.ttl=10

-XX:+AlwaysPreTouch

-Xss1m

-Djava.awt.headless=true

-Dfile.encoding=UTF-8

-Djna.nosys=true

-XX:-OmitStackTraceInFastThrow


-Dio.netty.noUnsafe=true
-Dio.netty.noKeySetOptimization=true
-Dio.netty.recycler.maxCapacityPerThread=0

-Dlog4j.shutdownHookEnabled=false
-Dlog4j2.disable.jmx=true

-Djava.io.tmpdir=${ES_TMPDIR}

-XX:+HeapDumpOnOutOfMemoryError


-XX:HeapDumpPath=data

-XX:ErrorFile=logs/hs_err_pid%p.log

8:-XX:+PrintGCDetails
8:-XX:+PrintGCDateStamps
8:-XX:+PrintTenuringDistribution
8:-XX:+PrintGCApplicationStoppedTime
8:-Xloggc:logs/gc.log
8:-XX:+UseGCLogFileRotation
8:-XX:NumberOfGCLogFiles=32
8:-XX:GCLogFileSize=64m

9-:-Xlog:gc*,gc+age=trace,safepoint:file=logs/gc.log:utctime,pid,tags:filecount=32,filesize=64m
9-:-Djava.locale.providers=COMPAT
```

+ 其他命令
```sh
docker build -t es:7.2 .
sudo mkdir -p /data/es/data
sudo mkdir -p /data/es/logs
docker run --name es -d --network host -v /data/es/data:/data -v /data/es/logs:/logs es:7.2
```