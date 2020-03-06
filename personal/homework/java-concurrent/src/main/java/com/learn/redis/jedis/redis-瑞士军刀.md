# bitmap
```shell
# 给位图指定索引设置值
setbit key offset value
# 获取指定位置的值
getbit key offset
# 统计个数
bitcount key [start end]
```

## bitop
> 做多个bitmap的交集 and ，并集 or ，异或 xor，非 not，然后把操作结果保存在destkey中  
命令格式 bitop op destkey key [key ...]
```sh
bitop and keyname key1 key2
```

## bitpos
> 计算位图的哥偏移量等于 targetbit 的位置  
命令格式 bitpos key targetBit [start end]

## 使用场景
> 独立用户访问次数统计，这样可用节省很多的空间

![](./imgs/01.png)

# 持久化
持久化的方式
+ 快照 
> Mysql Dump 和 redis的RDB
+ 写日志
> Mysql的binglog , Hbase的Hlog 和 redis的AOF

## RDB
二进制文件，存储在磁盘中，redis启动载入（复制媒介）
触发redis生成RDB文件的机制
+ save 同步的方式
+ bgsave 异步方式
+ 自动方式

### save方式
执行一个save命令就可了，它会阻塞。如果有老的文件需要新老替换
```sequence
title: redis save命令执行流程
participant 客户端
participant Reis服务器
participant 磁盘RDB文件

note left of 客户端: 调用save命令
note left of Reis服务器: 阻塞生成RDB文件
note right of 磁盘RDB文件: redis启动后自动恢复

客户端->Reis服务器
Reis服务器->磁盘RDB文件
磁盘RDB文件-->Reis服务器
```

### bgsave 
> 后台执行