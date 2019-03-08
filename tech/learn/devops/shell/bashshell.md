## bash shell编程
### 1 目录相关的操作
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

### 2 返回值相关
+ 命令返回值
>使用转义包命令包裹
```bash
es_id=`awk '/Loaded/ { print $4}' tmp | awk 'BEGIN {FS=":"} {print $2}'`
```
