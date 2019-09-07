<!-- TOC -->

- [0.1. 运行容器](#01-运行容器)
- [0.2. 查看日志](#02-查看日志)
- [0.3. 查看容器的进程](#03-查看容器的进程)
- [0.4. 在运行的容器中启动新的进程](#04-在运行的容器中启动新的进程)
- [0.5. 停止容器](#05-停止容器)

<!-- /TOC -->
## 0.1. 运行容器
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
## 0.2. 查看日志
docker logs
  -t 显示时间戳
  -f 跟踪显示最新日志
  --tail 10 显示最近10条数据
```
docker logs -tf imagename
```

## 0.3. 查看容器的进程
docker top imagename

## 0.4. 在运行的容器中启动新的进程
docker exec
  -d 后台进程
  -i 交互式
  -t 输出到控制台
```
docker exec -i -t containerid /bin/bash #启动一个新的bash进程
```

## 0.5. 停止容器
1. docker kill
> 直接杀死

2. docker stop
> 发送给容器停止命令

3. 覆盖entrypoint
```text
docker run --name mysql -v /data/mysql:/var/lib/mysql/ --network host -e MYSQL_ROOT_PASSWORD=pwd@123! --user mysql:mysql --rm --entrypoint="/bin/bash" mysql:latest -c "whoami && id"
```
> 注意使用-c指定 entrypoint的参数

4. docker在挂载磁盘卷的时候，经常没有权限
> 指定privileged=true  
原因是：centos7中安全模块selinux把权限禁掉了
```sh
# 解决办法1 ，在运行的时候指定 --privileged=true
docker run --name mysql -v /data/mysql:/var/lib/mysql/ --network host -e MYSQL_ROOT_PASSWORD=pwd@123! --privileged=true -d mysql:latest
# 解决办法2，临时关闭selinux然后再打开
setenforce 0
setenforce 1
# 解决办法3，添加linux规则，把要挂载的目录添加到selinux白名单

# 更改安全性文本的格式如下
chcon [-R] [-t type] [-u user] [-r role] 文件或者目录
 
选项参数： 
-R  ：该目录下的所有目录也同时修改； 
-t  ：后面接安全性本文的类型字段，例如 httpd_sys_content_t ； 
-u  ：后面接身份识别，例如 system_u； 
-r  ：后面街觇色，例如 system_r
```