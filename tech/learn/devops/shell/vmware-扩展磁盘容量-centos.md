---
title: vmware扩展磁盘容量
author: xiefq
date: 2019-04-17
---
## 关机虚拟机，扩展磁盘容量
![](./imgs/0001.png)

## 检查一下服务器的空磁盘
```sh
[xiefq@localhost elasticsearch]$ df -Th
Filesystem              Type      Size  Used Avail Use% Mounted on
/dev/mapper/centos-root xfs        17G   17G   20K 100% /
devtmpfs                devtmpfs  1.9G     0  1.9G   0% /dev
tmpfs                   tmpfs     1.9G  8.0K  1.9G   1% /dev/shm
tmpfs                   tmpfs     1.9G  8.7M  1.9G   1% /run
tmpfs                   tmpfs     1.9G     0  1.9G   0% /sys/fs/cgroup
/dev/sda1               xfs      1014M  143M  872M  15% /boot
tmpfs                   tmpfs     378M     0  378M   0% /run/user/1000
```
没有找到空的xfs磁盘

## 检查scsi设备
```
[xiefq@localhost elasticsearch]$ sudo ls /sys/class/scsi_device/
1:0:0:0  2:0:0:0
```

## rescan设备
> 必须要用root用户才能执行，sudo不行

```
[root@localhost ~]# echo 1 > /sys/class/scsi_device/1\:0\:0\:0/device/rescan
[root@localhost ~]# echo 1 > /sys/class/scsi_device/2\:0\:0\:0/device/rescan
```

## 扩展分区
