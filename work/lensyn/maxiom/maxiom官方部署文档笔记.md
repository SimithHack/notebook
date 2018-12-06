### 1 Installation overview
Installing Maximo Asset Management requires **system administrator** rights and privileges.

+ 数据库可选择随产品一起发布的IBM DB2，也支持其他的数据库
+ 应用服务器可支持websphere，weblogic,使用的是商业版本的Java 2 开发。
+ 可配置单独的Http服务和J2EE应用服务器一起协同工作
+ 可以配置LDAP目录服务器，提供认证管理
+ 支持多国语言

![maximo组件](https://raw.githubusercontent.com/SimithHack/notebook/master/work/lensyn/maxiom/imgs/components.png)

### 部署方案
You use the Maximo Asset Management installation program to install IBM middleware products.

两种方式部署 Maxiom Asset Management
+ Single-server
> The single-server topology consists of loading all Maximo Asset
  Management components, including all Maximo Asset Management
  middleware, the Maximo Asset Management administrative workstation,
  process managers, and other components, onto one server.
  
将所有的中间件，数据库，工作台都部署到一台服务器上

+ Multi-server
> splitting Maximo Asset Management
  components across several different servers.

```
【注意】
因为安装的时候会重启DB2数据库，所以不要和其他的服务共享数据库服务
```

websphere 提供集群支持，和自动性能优化

### 安全访问规划
+ administration user
    > 初始化配置，添加用户 默认值是 maxadmin

+ system registration user
    > 自助注册用户 默认 maxreg

+ system integration user
    > 企业是配置 默认 maxintadm

以上三个用户默认提供，可以后边修改满足权限安全需求

安装maximo asset management时会让选择管理用户和组的方法，这个方法适用于所有其他的产品。但是，如果有些产品先于mam安装，此时选择和先安装的产品一致
的方法。

如果使用WebLogic服务器，MAM会使用自己内部自己的认证机制，从而LDAP就步骤是必须的了。

#### 安全选项
> 决定系统认证和授权的方法

+ Use J2EE application security for authentication and user and group management
    > 在LDAP服务器里创建用户和安全组，这些用户和安全组信息会被MAM的定时任务更新到数据库中。在maximo添加的用户不会同步到LDAP中。

+ Use J2EE application security for authentication and user management
+ Use Maximo internal authentication

### 服务能力规划
> 使用从各个层面收集的数据，在中心点分析问题。也就是日志监控和分析的能力

IBM Support Assistant Workbench

### 语言和用户体验支持
> 可配置多国语言和多种皮肤

### 部署场景
+ Deploying with automatic middleware configuration
    > 自动配置中间件，适用于演示环境

+ Deploying automatically reusing existing middleware
    > 部分中间件或者全部中间件已经装好了，适用于对数据库和应用服务器都很精通的管理员

+ Deploying manually reusing existing middleware
    > 中间件已经存在，但是因为公司的规章制度，不允许使用自动配置工具，所以所有的中间件配置都需要手动配置
    适用于对数据库和应用服务器都很精通的管理员

+ Deploying automatically in a cluster environment
    > 配置集群环境，适用于对应用服务了如指掌的的管理员。并且用IBM的 websphere应用服务器创建集群环境

### 在一个管理工作站上执行多个产品的安装
> 比如测试环境，开发环境，培训环境。每一个产品的实例都需要单独的数据库实例。