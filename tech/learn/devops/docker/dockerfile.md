## dockerfile 学习笔记
> 自动从文件中读取命令，构建镜像

### docker build 命令
> 从docker file和context构建镜像

+ context
> 构建上下文是指定路径和URL下的一堆文件集合，path是本地文件路径，url是git地址

```bash
docker build .
```
上述命令就是将当前文件夹作为context，发送到docker daemon。在大多数情况下，尽量确保context目录问空，或者只包含需要的文件

这些路径下的文件就可以在dockerfile里使用

+ 排除某些文件
在context目录下，创建一个.dockerignore文件可以指定排除哪些文件

```bash
# comment
*/temp*
*/*/temp*
temp?
```

+ 指定dockerfile文件
```bash
$ docker build -f /path/to/a/Dockerfile .
```

+ 指定镜像的tag
```bash
$ docker build -t shykes/myapp .
```

+ 同时发布到多个镜像库，指定多个tag
```bash
docker build -t shykes/myapp:1.0.2 -t shykes/myapp:latest .
```
+ docker daemon执行构建流程

在docker daemon运行dockerfile里的指令之前，daemon首先会对dockerfile进行语法校验，然后逐条命令执行，自动清除上下文。命令之间是完全独立执行
甚至可以再创建一个临时镜像出来，docker是允许这样做的，产生临时镜像可以加快docker的构建过程，当出现“Using cache”信息时，表示docker daemon在
使用临时镜像。

### BuildKit
从18.09后，可以使用BuildKit来构建镜像了
