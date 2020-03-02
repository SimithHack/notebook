# redis-sentinel故障转移实验

实验内容演示redis-sentinel故障转移

## 节点内容：
```
sentinel-26381
sentinel-26380
sentinel-26379
redis-6381
redis-6380
redis-6379
```
## 类型
string:
    set name xxx
    mset name xxx age 12
    get name xxx
    mget name age
    setrange 1 name xxx
    setnx name xxx
    object encoding name (44 embstr, >44 raw)
list:
    
