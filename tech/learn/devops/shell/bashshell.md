## bash shell编程
<!-- TOC -->

- [bash shell编程](#bash-shell编程)
    - [目录相关的操作](#目录相关的操作)
    - [返回值相关](#返回值相关)
    - [防火墙控制](#防火墙控制)

<!-- /TOC -->

### 目录相关的操作
+ 判断目录是否存在
```bash
if [ ! -d "/myfolder" ]; then
  mkdir /myfolder
fi
```

+ 判断文件,目录是否存在或者具有权限
```bash
# -x 参数判断 $folder 是否存在并且是否具有可执行权限
if [ ! -x "$folder"]; then
  mkdir "$folder"
fi
```

+ 判断文件是否存在
```bash
# -f 参数判断 $file 是否存在
if [ ! -f "$file" ]; then
  touch "$file"
fi
```

### 返回值相关
+ 命令返回值
>使用转义包命令包裹
```bash
es_id=`awk '/Loaded/ { print $4}' tmp | awk 'BEGIN {FS=":"} {print $2}'`
```

### 防火墙控制
+ 开启某个端口
```bash
firewall-cmd --zone=public --permanent --add-port=8080/tcp
```

+ 某个端口只对某个IP开启
```bash
firewall-cmd --permanent --add-rich-rule="rule family="ipv4" source address="192.168.211.129" port protocol="tcp" port="9200" accept"
```

+ 关闭某个端口
```bash
sudo firewall-cmd --remove-port=9200/udp --permanent
```

## curl文件上传
curl -H "Content-Type: multipart/form-data" -F "file=@/export/logs/auth-gateway/timeout-request.log"  http://localhost:8080/fileUpload/image.html