## 运行容器
+ 交互式命令
```
docker run -i -t imagename /bin/bash
```
+ 暂时退出交互式命令
```
ctr+q
```
+ 重新连接到容器
```
docker attach imagename|imageid
```
+ 后台运行
```
docker run -d imagename
```
## 查看日志
docker logs
  -t 显示时间戳
  -f 跟踪显示最新日志
  --tail 10 显示最近10条数据
```
docker logs -tf imagename
```

## 查看容器的进程
docker top imagename

## 在运行的容器中启动新的进程
docker exec
  -d 后台进程
  -i 交互式
  -t 输出到控制台
```
docker exec -i -t containerid /bin/bash #启动一个新的bash进程
```

## 停止容器
+ docker kill
> 直接杀死

+ docker stop
> 发送给容器停止命令
