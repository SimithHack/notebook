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

优点：
+ 检测并跳过无用的执行阶段
+ 并发执行独立的构建单元
+ 增量传输修改的文件
+ 检测和跳过无用的文件传输
+ 使用dockerfile新特性
+ 避免API的副作用（中间镜像和容器）

怎样开启？
> 在运行docker build命令之前，将环境变量DOCKER_BUILDKIT=1


### dockerfile的格式
 ```bash
 # Comment
INSTRUCTION arguments
```
dockerfile必须以FROM指令开始，指定base image   
以#开始的行为注释行，出现在行间的是参数

### parser指令
它影响指令后边的命令的执行方式，parser指令不会添加新的docker层，也不会出现在build过程中，格式为# directive=value，一个指令只能用一次

+ syntax指令
> 用来指定构建dockfile的构建器，只有BuildKit启用才有效，使用同一个外部构建器可以确保同一个组织里，用的是同一个版本的构建器
docker官方支持两个版本的构建器，一个是稳定版，一个是实验测试版

```bash
# syntax=docker/dockerfile
# syntax=docker/dockerfile:1.0
# syntax=docker.io/docker/dockerfile:1
# syntax=docker/dockerfile:1.0.0-experimental
# syntax=example.com/user/repo:tag@sha256:abcdef...
```
+ escape指令
> 指定在dockerfile里使用的转移字符，它不仅可用来转义行内字符，也可转义行，让dockerfile指令可以跨行书写

在RUN命令里，转义字符是没用的

### 环境变量
> ENV语句可以为其他指令定义变量，在dockerfile里使用$varname或者${varname}来引用环境变量

+ 支持bash规则
${variable:-word} 如果variable没有值，则返回“word”  
$(variable:+word} 如果variable有值，则返回“word”, 空值就返回空值

+ 也可要使用esacape转义，比如
```bash
COPY \$foo /quux 就是字面意思$foo，不解释成环境变量
```

+ 环境变量支持的指令（可以在这些指令里使用）
```bash
ADD
COPY
ENV
EXPOSE
FROM
LABEL
STOPSIGNAL
USER
VOLUME
WORKDIR
```

### FROM 指令
格式如下，用于设置base镜像
```
FROM <image>[:<tag>] [AS <name>]
```
+ ARG是唯一能够在出现在FROM指令之前的指令
+ FROM指令可以出现多次，可以作为其他的依赖关系
+ AS指定别名
+ tag指定拉取的镜像版本

ARG和FROM指令的交互
> FROM指令支持ARG定义的变量

```
ARG  CODE_VERSION=latest
FROM base:${CODE_VERSION}
CMD  /code/run-app

FROM extras:${CODE_VERSION}
CMD  /code/run-extras
```

### RUN指令
> 在当前镜像的最顶层执行命令，并且提交结果。可以使用转义字符进行换行书写。默认是bin/sh 脚本执行命令，也可以指定其他的shell解释器

两种形式
+ RUN <command> 后边直接是shell脚本
+ RUN ["executable", "param1", "param2"] 类似于exec形式，并不会在base镜像里执行

. 换行书写
```bash
RUN /bin/bash -c 'source $HOME/.bashrc; \
echo $HOME'

RUN ["/bin/bash", "-c", "echo hello"] # bash shell
```

RUN命令是有缓存的，可以使用 docker build --no-cache来禁用缓存


### CMD指令
> 一个dockerfile只能有一个CMD指令，多个CMD只有最后一个执行，作用是容器的默认启动执行进程

三种形式
+ CMD ["executable","param1","param2"] (exec form) 推荐模式
+ CMD ["param1","param2"] 为ENTRYPOINT提供默认参数，此时两个指定都必须使用JSON Array的形式
+ CMD command param1 param2 (shell form)

CMD和RUN指令的区别，CMD在build期间是不运行的

### LABEL指令
> 给镜像添加元数据。label镜像之间是可以继承的。同名覆盖原则

格式
```
LABEL <key>=<value> <key>=<value> <key>=<value> ...
```

docker inspect 查看镜像的labels

### EXPOSE指令
> 通知docker，本容器需要监听的端口。它并不是发布此端口，而是作为一种指导，告知运行容器的人。真正发布使用的端口是通过docker run -p来指定。
或者 -P指定，发布所有EXPOSE的端口

命令格式
```bash
EXPOSE <port> [<port>/<protocol>...]
```

例子
```bash
EXPOSE 80/tcp
EXPOSE 80/udp
```

使用docker network命令可以允许相同网络的容器可以访问任何端口

### ENV指令
> 设置环境变量

```bash
ENV myName John Doe
ENV myDog Rex The Dog
ENV myCat fluff
# 或者
ENV myName="John Doe" myDog=Rex\ The\ Dog \
    myCat=fluffy
```

可以通过docker inspect查看容器的环境变量  
docker run --env <key>=<value> 可以在运行容器的时候修改环境变量的值
 

### ADD指令
> 复制文件，并且可以修改权限。目标（src）可以是通配符。dest可以是绝对路径，也可以是相对于WORKDIR的路径

格式
```
ADD [--chown=<user>:<group>] <src>... <dest>
ADD [--chown=<user>:<group>] ["<src>",... "<dest>"]
```

例子
```bash
ADD hom* /mydir/        # adds all files starting with "hom"
ADD hom?.txt /mydir/    # ? is replaced with any single character, e.g., "home.txt"
```

相对和绝对路径的使用
```
ADD test relativeDir/          # adds "test" to `WORKDIR`/relativeDir/
ADD test /absoluteDir/         # adds "test" to /absoluteDir/
```


