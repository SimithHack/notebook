# etcd介绍

> **etcd** is a strongly consistent, distributed key-value store that provides a reliable way to store data that needs to be accessed by a distributed system or cluster of machines. It gracefully handles leader elections during network partitions and can tolerate machine failure, even in the leader node.
>
> 高一致性，分布式简直存储，提供分布式系统和集群集群可靠的存储数据。非常优雅的在网络分区处理`leader election` 并且容忍机器宕机。甚至是 `leader` 节点宕机也无问题。

* 完全复制 集群中的每个节点都可使用完整的文档

* 高一致性
* restful api

Your applications can read from and write data into etcd. A simple use case is storing database connection details or feature flags in etcd as key-value pairs. These values can be watched, allowing your app to reconfigure itself when they change. Advanced uses take advantage of etcd’s consistency guarantees to implement database leader elections or perform distributed locking across a cluster of workers.

你的应用程序可以从 `etcd` 里读写数据。一个简单的应用是存储关于数据连接信息或者特性标签的键值对。这些值可以被监控，允许你的应用在配置值修改的时候重新配置自己。**热加载** 。

## etcd使用场景

* 服务发现

* 配置中心

* 分布式锁

  因为etcd使用raft算法保持了数据的强一致性，某次操作存储到集群中的值必然是全局一致的，所以，很容易实现分布式锁。锁服务有两种使用方式 `保持独占` 和 `控制时序`

  * 保持独占

    > 即 所有获取锁的用户，只有一个可以得到

    etcd提供了分布式锁原语操作 `CAS` . 通过设置 prevExist 值，可以保证在多个节点同时去创建某个目录时，只有一个能成功。

  * 控制时序

     所有获得锁的用户都会被安排执行，但是获取锁的顺序是全局唯一的。etcd会自动在目录下生成一个当前节点最大的键值。存储新的值（客户编号）。

## zookeeper vs etcd

**zookeeper**

* zk部署复杂，而且 `paxos` 强一致性算法本身就很复杂。官方也只提供了 Java 和 c 两种接口。

* java 编写实现
* 发展缓慢

**etcd**

* go语言编写，部署简单，Http接口，使用简单。Raft 算法，理解简单
* 数据持久化，一更新，就持久化
* 安全 支持SSL客户端安全认证

 # etcd 安装部署

* 下载

  ```
  https://github.com/etcd-io/etcd/releases/
  ```

* 安装引导

  ```
  http://play.etcd.io/install
  ```

* 选择使用API的版本

  ```
  export ETCDCTL_API=3
  ```

  

# zookeeper 

## 应用场景

* DNS服务
* 配置管理
* 集群成员管理
* 服务发现

## 数据模型

