# 安装
## rpm-安装
1. 检查是否安装
```sh
# 检查包是否安装
rpm -qa | grep -i mysql
# 查看用户组
cat /etc/passwd| grep mysql
cat /etc/group | grep mysql
# 
yum install libaio
groupadd mysql
useradd -r -g mysql -s /bin/false mysql
```
2. 启动
3. 修改密码
```sh
mysqladmin -u root password xxxx
```
4. 设置开机自启动
```sh
chkconfig mysql on
```
5. 完整的命令
```sh
shell> groupadd mysql
shell> useradd -r -g mysql -s /bin/false mysql
shell> cd /usr/local
shell> tar xvf /path/to/mysql-VERSION-OS.tar.xz
shell> ln -s mysql-8.0.17-linux-glibc2.12-x86_64 mysql
shell> cd mysql
shell> mkdir mysql-files
shell> chown mysql:mysql mysql-files
shell> chmod 750 mysql-files
shell> bin/mysqld --initialize --user=mysql
shell> bin/mysql_ssl_rsa_setup
shell> bin/mysqld_safe --user=mysql &
# Next command is optional
shell> cp support-files/mysql.server /etc/init.d/mysql.server
```
根据提示还需要创建/var/log/mariadb/mariadb.log，并且mysql用户可以访问

输出
```
[root@localhost mysql]# bin/mysqld --initialize --user=mysql
2019-09-23T14:02:18.753095Z 0 [Warning] [MY-011070] [Server] 'Disabling symbolic links using --skip-symbolic-links (or equivalent) is the default. Consider not using this option as it' is deprecated and will be removed in a future release.
2019-09-23T14:02:18.755321Z 0 [System] [MY-013169] [Server] /usr/local/mysql-8.0.17-linux-glibc2.12-x86_64/bin/mysqld (mysqld 8.0.17) initializing of server in progress as process 7478
2019-09-23T14:02:20.252087Z 5 [Note] [MY-010454] [Server] A temporary password is generated for root@localhost: wi;<YqZ/)9Bd
2019-09-23T14:02:20.939722Z 0 [System] [MY-013170] [Server] /usr/local/mysql-8.0.17-linux-glibc2.12-x86_64/bin/mysqld (mysqld 8.0.17) initializing of server has completed
```
```
[root@localhost mysql]# 2019-09-23T14:06:35.198056Z mysqld_safe Logging to '/var/log/mariadb/mariadb.log'.
2019-09-23T14:06:35.236460Z mysqld_safe Starting mysqld daemon with databases from /var/lib/mysql
2019-09-23T14:06:36.193255Z mysqld_safe mysqld from pid file /var/run/mariadb/mariadb.pid ended
```
## mysql的目录
+ 数据目录 /var/lib/mysql
+ 配置文件 /usr/share/mysql

## 修改配置文件
/usr/share/mysql/my-huge.cnf拷贝到/etc/my.cnf

### 修改字符集和数据的存储路径
1. 查看字符集
```sh
show varibles like 'character%';
show varibles like '%char%';
#都可以
``` 
2. 修改
```cnf
[client]
password = xxx
port = 3306
default-character-set = uft8
[mysqld]
port=3306
character_set_server=utf8
character_set_client=utf8
collation-server=utf8_general_ci
lower_case_table_names=1 
max_connections=1000
[mysql]
default-character-set=utf8
```
### mysql主要配置文件
1. 二进制日志log-bin
> 用于主从复制
![](./imgs/06.png)

2. 错误日志log-error
> 默认关闭，记录严重的警告和错误信息，每次七档和关闭的详细信息等

3. 查询日志log
> 默认关闭，记录查询的sql语句，如果开启会降低MySQL的整体性能，因为记录日志也是需要消耗系统资源的

4. 数据文件
```text
a) frm文件
    存放表结构
b) myd
    存储表数据
c) myi
    存放表索引
```

5. 如何配置
windows叫 my.ini文件；Linux叫my.cnf文件

# 优化分析
```text
性能下降SQL慢
执行时间长
等待时间长
```
查询语句写的烂，索引失效（单值，复合）

关联查询太多join

服务器调优以及各种参数设置（缓冲，线程数）

create index idx_user_name on user(name, email) 