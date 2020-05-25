# k8s上安装kafka

## 部署registry

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: docker-registry
spec:
  replicas: 1
  selector: 
    matchLabels:
      app: docker-registry
  template:
    metadata:
      labels:
        app: docker-registry
    spec:
      containers:
      - name: registry
        image: registry:2
        imagePullPolicy: IfNotPresent
        volumeMounts:
        - name: root-dir
          mountPath: /var/lib/registry
        ports:
        - name: port
          containerPort: 5000
          hostPort: 5000
      tolerations:
      - effect: NoSchedule
        key: node-role.kubernetes.io/master
      nodeName: master
      volumes:
      - name: root-dir
        hostPath:
          path: /data/docker-registry
          type: DirectoryOrCreate
```

修改`/etc/docker/daemon.json`

```json
{
        "registry-mirrors":[
            "https://reg-mirror.qiniu.com/", 
            "https://registry.docker-cn.com"
        ],
        "insecure-registries": ["http://192.168.219.68"]
}
```

重启

```bash
systemctl daemon-reload
systemctl restart docker
```

## 部署zookeeper

1. 制作镜像 zk.df

   > 注意一定要是64位的jdk，另外zk必须在前台运行

   ```bash
   FROM master:5000/ubuntu:latest
   ADD apache-zookeeper-3.6.0-bin.tar.gz ./
   ADD jdk-8u241-linux-x64.tar.gz ./
   ADD zoo.cfg apache-zookeeper-3.6.0-bin/conf/
   ENV JAVA_HOME /jdk1.8.0_241
   ENV PATH $PATH:$JAVA_HOME/bin
   RUN mkdir -p /data/zookeeper
   ENTRYPOINT ["apache-zookeeper-3.6.0-bin/bin/zkServer.sh", "start-foreground"]
   ```

2. zoo.cfg

   ```cfg
   # The number of milliseconds of each tick
   tickTime=2000
   # The number of ticks that the initial 
   # synchronization phase can take
   initLimit=10
   # The number of ticks that can pass between 
   # sending a request and getting an acknowledgement
   syncLimit=5
   # the directory where the snapshot is stored.
   # do not use /tmp for storage, /tmp here is just 
   # example sakes.
   dataDir=/data/zookeeper
   # the port at which the clients will connect
   clientPort=2181
   # the maximum number of client connections.
   # increase this if you need to handle more clients
   #maxClientCnxns=60
   #
   # Be sure to read the maintenance section of the 
   # administrator guide before turning on autopurge.
   #
   # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
   #
   # The number of snapshots to retain in dataDir
   #autopurge.snapRetainCount=3
   # Purge task interval in hours
   # Set to "0" to disable auto purge feature
   #autopurge.purgeInterval=1
   
   ## Metrics Providers
   #
   # https://prometheus.io Metrics Exporter
   #metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
   #metricsProvider.httpPort=7000
   #metricsProvider.exportJvmInfo=true
   ```

3. 编译

   ```bash
   docker build -t master:5000/zk:3.6.0 -f zk.df .
   ```

4. 运行

   ```yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: zk-server
   spec:
     replicas: 1
     selector: 
       matchLabels:
         app: zk-server
     template:
       metadata:
         labels:
           app: zk-server
       spec:
         containers:
         - name: zk
           image: master:5000/zk:3.6.0
           imagePullPolicy: Always
           volumeMounts:
           - name: data-dir
             mountPath: /data/zookeeper
           ports:
           - name: port
             containerPort: 2181
         volumes:
         - name: data-dir
           hostPath:
             path: /data/zookeeper
             type: DirectoryOrCreate
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: zk-service
   spec:
     type: ClusterIP
     selector: 
       app: zk-server
     ports:
     - name: zk-port
       port: 2181
       targetPort: 2181
   ```

   

## 单节点测试

* 下载kafka最新版

  https://mirror.bit.edu.cn/apache/kafka/2.4.1/kafka_2.11-2.4.1.tgz

* 编写

  1. server.properties

  ```conf
  broker.id=0
  num.network.threads=3
  num.io.threads=8
  socket.send.buffer.bytes=102400
  socket.receive.buffer.bytes=102400
  socket.request.max.bytes=104857600
  log.dirs=/data/kafka-logs
  num.partitions=1
  num.recovery.threads.per.data.dir=1
  offsets.topic.replication.factor=1
  transaction.state.log.replication.factor=1
  transaction.state.log.min.isr=1
  log.retention.hours=168
  log.segment.bytes=1073741824
  log.retention.check.interval.ms=300000
  zookeeper.connect=zk-service.default.svc.cluster.local:2181
  zookeeper.connection.timeout.ms=6000
  group.initial.rebalance.delay.ms=0
  ```

  2. kafka.df

  ```df
  FROM master:5000/ubuntu:latest
  ADD kafka_2.11-2.4.1.tgz ./
  ADD jdk-8u241-linux-x64.tar.gz ./
RUN rm -rf kafka_2.11-2.4.1/config/server.properties
  ADD server.properties kafka_2.11-2.4.1/config/
  ENV JAVA_HOME /jdk1.8.0_241
  ENV PATH $PATH:$JAVA_HOME/bin
  RUN mkdir -p /data/kafka-logs
  ENTRYPOINT ["/kafka_2.11-2.4.1/bin/kafka-server-start.sh", "/kafka_2.11-2.4.1/config/server.properties"]
  ```
  
  3. docker打包
  
  ```bash
  docker build -t master:5000/kafka:latest -f kafka.df .
  ```
  
  3. kafka-service.yaml
  
  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: kafka-server
  spec:
    replicas: 1
    selector: 
      matchLabels:
        app: kafka-server
    template:
      metadata:
        labels:
          app: kafka-server
      spec:
        containers:
        - name: zk
          image: master:5000/kafka:latest
          imagePullPolicy: Always
          volumeMounts:
          - name: log-dir
            mountPath: /data/kafka-logs
          ports:
          - name: port
            containerPort: 9092
        volumes:
        - name: log-dir
          hostPath:
            path: /data/kafka-logs
            type: DirectoryOrCreate
  ---
  apiVersion: v1
  kind: Service
  metadata:
    name: kafka-service
  spec:
    type: ClusterIP
    selector: 
      app: kafka-server
    ports:
    - name: kafka-port
      port: 9092
      targetPort: 9092
  ```
  
  5. 运行
  
  ```bash
  kubectl apply -f kafka-service.yaml
  ```
  
  6. 创建ingress
  
     > 规划 kafka.k8s

