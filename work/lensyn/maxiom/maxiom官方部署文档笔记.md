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

