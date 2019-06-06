---
标题: 容器网络虚拟化
---

## 桥接
+ 容易产生网络风暴

+ NAT方式
    + 物理机打开核心转发功能
    + NAT背后的服务器暴露出去 SNAT DNAT
    + 网络容易管理，但是效率低

+ 叠加网络 overlay network
    + 隧道转发

+ Docker网络
    + docker network ls
    + docker0 物理机上创建的软交换机
        + 每个容器，创建一对网卡，一个在docker0上，一个在容器上
        + yum -y install bridge-utils工具可以查看docker0上关联的接口
            + brctl show
        + ip link show 可以查看 xxxxx@ifx，表示这个网卡是在容器中
        + iptables -t nat -vnL 可以查看路由规则

    + docker container create --network xxx