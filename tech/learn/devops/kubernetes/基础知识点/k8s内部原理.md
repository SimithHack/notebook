## 1 理解架构
+ 集群组件
  - kubernetes control plane(控制面板)
    - etcd 分布式存储
    - api 服务器
    - 调度器
    - 控制管理器
  - worker nodes(工作节点)
    - kubelet
    - kube-proxy
    - container-runtime (docker)
  - 插件
    - kubernetes DNS 服务器
    - 仪表盘
    - Ingress controller
    - heapster
    - 网络插件

  ![关系图](./imgs/00001.png)
  ```
  所有的组件都必须启动，才能使用k8s提供的所有功能
  kubectl get componentstatuses # 此命令可以显示控制面板组件的状态
  ```

+ 组件之间是怎么交互的
> k8s的系统组件只与API server交互，API是唯一和etcd组件交互的组件

一般都是其他组件主动连接APIServer，而在使用kubectl logs，kubectl attach ,kubectl port-forward这些命令的时候
APIServer主动和其他组件建立连接

```
kubectl attach和kubectl exe命令作用大致相同，不同的是，attach是在容器的主进程里运行命令，而exe是单独
开启新的进程
```

work nodes的组件需要在同一个节点上运行，但是 control plane的组件可以跨多个服务器运行，并且同一个
组件可以运行多个实例，以确保高可用性

API server和etcd的实例彼此可以并发执行任务，但是调度器和controller manager一次只能一个
实例运行，其他的都是出于随时待命状态（standby)

+ 组件怎么运行部署

既可以部署在系统层面上，也可以pod的方式运行, kubelet实现了这一目的

kubelets 唯一一个常作为系统进程运行的组件，它可以将其他的组件作为pod运行, 使用kubeadm命令创建

+ k8s怎么使用etcd
> which is a fast, distributed, and consistent key-value store

只有APIServer和etcd交互，并使用乐观锁保证数据的一致性，k8s的所有资源都有一个meta.resourceVersion字段，
当要更资源时候，此字段必须传回到APIServer，

+ 资源在etcd里边是怎么存储的
k8s将etcd的数据存储在/registry目录里，etcdv3不支持目录层次，而是在key上用“/”的方式间接的
表达了目录的概念

可以使用下面的方式查看某一前缀下的子目录，当具体到某一个具体存在的资源时，就会显示此资源
的详细配置
```
etcdctl get /registry/pods --prefix=true
etcdctl get /registry/pods/default/kubia-159041347-wt6ga
```

+ 当etcd集群后，确保一致性
> etcd使用RAFT算法做到这一点
