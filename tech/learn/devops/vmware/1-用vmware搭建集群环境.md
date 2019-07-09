---
标题: 使用vmware搭建集群环境
时间: 2019-07-05
作者: xiefq
---

# 准备阶段
+ 安装vmware

![](./imgs/1.png)

+ 配置NAT
> 留意红色部分，安装vmware后会默认分配一个NAT网段，如果不是需要的，可以点击“DHCP配置”进行修改

![](./imgs/2.png)

+ 记录配置信息

![](./imgs/3.png)

比如，我的NAT配置关键信息：网段为192.168.211.0/24，网关为192.168.211.2

+ 虚拟机里安装centos 7
> 安装一个就可以了，其他两个slave可以通过下面方式进行复制，这样可以节省很多磁盘空间

![](./imgs/4.png)

# 配置网络
我们的计划是

|IP地址|hostname|
| :----: | :----: |
|192.168.211.130|master|
|192.168.211.131|slave01|
|192.168.211.132|slave02|

+ master网卡配置
```bash
[xiefq@master ~]$ sudo vi /etc/sysconfig/network-scripts/ifcfg-ens33

TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
IPADDR=192.168.211.130
NETMASK=255.255.255.0
GATEWAY=192.168.211.2
DNS1=8.8.8.8
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens33
UUID=f5440ad0-8670-4375-abbb-185e719de6f1
DEVICE=ens33
ONBOOT=yes
```

+ master hostname配置
```bash
[xiefq@master ~]$ sudo vi /etc/hostname

master
```

+ 添加hosts
```bash
[xiefq@master ~]$ cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
192.168.211.130 master
192.168.211.131 slave01
192.168.211.132 slave02
```

+ 重启
```bash
sudo reboot
```

# 安装docker
[这个网站下载RPM安装包](https://download.docker.com/linux/centos/7/x86_64/stable/Packages/) 安装过程比较繁琐，少这个少那个包的
[二进制安装](https://download.docker.com/linux/static/stable/)

下面我们使用二进制方式安装

+ 安装
```sh
$ tar -xvf *.tar
$ sudo cp -a docker/* /usr/bin
$ sudo dockerd &
```

+ 将当前用户添加进用户组
```sh
$ sudo groupadd docker
$ sudo gpasswd -a $USER docker
$ newgrp docker
```

+ 修改docker仓库为国内仓库
```bash
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "selinux-enabled": true,
  "registry-mirrors": ["https://yourcode.mirror.aliyuncs.com"]
}
EOF
```
杀死dockerd进程，然后重启

# faq
+ 出现 chmod: changing permissions of ‘/proc/self/attr/keycreate’: Permission denied 错误是因为selinux开启了
```
selinux-enabled=true
```