### 配置文件路径
```$xslt
/etc/nginx, /usr/local/etc/nginx, or /usr/local/nginx/conf
```

+ 指令类型
    + 简单指令
    + 区块指令
+ 指令上下文 context
    + main
    ```
    1 user nginx进程使用哪个用户账号 user user_name
    2 work_process 指定nginx开启多少个主进程 设置为auto自动配置为当前服务器的CPU处理器数量  
    3 error_log 指定错误日子的文件地址，格式为 error_log path/xxx.log error
      这个指令可以放在多个指令块里，比如 manin, http, mail, stream, server, location
      日志级别（info, notice, warn, error, crit, alert, emerg）
    4 pid 存储进程ID的文件路径
       linux存储进程ID，可以用来查看该进程运行了多长时间，使用 ps -p 1234[pid] -o etime=
    ```
    + Events Context
    ```
    1 只能在main context里定义，一个nginx配置里只能又一个event的定义
    2 work_process指定最大连接数
    3 use 会根据nginx运行的平台决定使用哪种方法，"select"是最差的选项（Windows平台上就是这种）
      其他的还有“poll”, "kqueue", "epoll", "eventport"
    4 multi_accept 默认关闭，表示一个work_process一次只接受一个请求,建议在生产环境开启
    5 accept_mutex 默认开启，表示work_process按顺序一个一个处理请求
    6 accept_mutext_delay 在work_process接受新请求时最大等待时间
    ```
    + Http Context
    ```
    1 include /etc/nginx/mine.types include指令可以引用外部配置
      minetype可以指导浏览器怎么展示内容而不是下载文件，也可以自定义mine类型，专属自己的应用程序
    2 default_type application/octet-stream 配置如果nignx不能决定哪一种映射类型时候默认的minetype
    3 log_format 配置日志格式，第二个参数是名称
      log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                              '$status $body_bytes_sent "$http_referer" '
                              '"$http_user_agent" "$http_x_forwarded_for"';

    ```
