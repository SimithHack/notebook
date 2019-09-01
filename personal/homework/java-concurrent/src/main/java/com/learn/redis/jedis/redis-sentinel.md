# 主从复制高可用的问题？
```text
问题？
    1 主节点出现问题后，故障恢复需要手动解决。
    2 写能力和存储能力受到限制（其他节点都是副本）
    3 节点的不可用，都可能导致代码的配置的修改
```
# redis-sentinel架构说明
```text
说明
    redis-sentinel也是redis进程，只是它不存储数据，客户端从sentinel获取redis的信息，实际的主从信息由sentinel给我们屏蔽了。
怎么实现故障转移的
    1 当我们的master宕机后，连接出现中断。
    2 多个sentinel发现master有问题
    3 选举出一个sentinel作为领导
    4 选择一个slave作为新的master
    5 通知其余的slave成为新的master的slave
    6 通知客户端组从发生了变化
    7 老的master复活成为新的master的slave
sentinel可以实现多套master-slave的集群方式，这样就可以监控多个集群的状态，每一套通过master-name进行唯一标识
```
# redis-sentinel安装
+ 配置开启主从节点
+ 配置开启sentinel监控主节点（sentinel是特殊的redis节点）
+ 实际是多台机器部署
+ 详细配置节点
> 注意配置的IP地址，需要客户端也能够访问

```text
配置目标
    1 sentinel: 26379 26380 26381
        ===
        port ${port}
        dir "/data/redis"
        logfile "${port}.log"
        sentinel monitor mymaster 127.0.0.1 7000 2 # 监控哪个主节点，从节点有几个
        sentinel down-after-milliseconds mymaster 30000 # 表示多少时间不通就认为是down的
        sentinel parallel-syncs mymaster 1 # 关于同步复制的配置，每次只复制1个
        sentinel failover-timeout mymaster 180000 # 故障恢复的一个超时时间配置
        ===
        详细配置
        ===
        port 26379
        daemonize yes
        pidfile /var/run/redis-sentinel-26379.pid
        logfile "stl-26379.log"
        dir /data/redis
        sentinel monitor mymaster 127.0.0.1 7000 2
        sentinel down-after-milliseconds mymaster 30000
        sentinel parallel-syncs mymaster 1
        sentinel failover-timeout mymaster 180000
        sentinel deny-scripts-reconfig yes
        ===
    2 redis-master 7000
        ===
        port 7000
        daemonize yes
        pidfile /var/run/redis-7000.pid
        logfile "7000.log"
        dir "/data/redis"
        ===
    3 slave 7001 7002
        ===
        port 7001
        daemonize yes
        pidfile /var/run/redis-7001.pid
        logfile "7001.log"
        dir "/data/redis"
        replicaof 127.0.0.1 7000
        ===
启动sentinel
    redis-sentinel stl-26739.conf
```


# 客户端连接
```text
    客户端的实现原理
    1 客户端拿到所有sentinel节点，查看哪一个是可用的
    2 sentinel节点集合+mastername，就会返回master节点给我们
```
# redis-sentinel实现原理
# 常见运维问题处理