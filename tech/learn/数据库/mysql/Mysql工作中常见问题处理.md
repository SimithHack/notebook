---
题目: Mysql工作中常见问题处理
---

Mysql工作中常见问题处理

<!-- TOC -->

- [1. 连接问题](#1-连接问题)
    - [1.1. 新版本8.0的java.sql.SQLNonTransientConnectionException异常](#11-新版本80的javasqlsqlnontransientconnectionexception异常)

<!-- /TOC -->
---- 

# 1. 连接问题

## 1.1. 新版本8.0的java.sql.SQLNonTransientConnectionException异常
```java
Caused by: java.sql.SQLNonTransientConnectionException: Public Key Retrieval is not allowed
        at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:110) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:835) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:455) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:240) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:207) ~[mysql-connector-java-8.0.13.jar!/:8.0.13]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:95) ~[HikariCP-2.5.1.jar!/:na]
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:101) ~[HikariCP-2.5.1.jar!/:na]
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:341) ~[HikariCP-2.5.1.jar!/:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:506) ~[HikariCP-2.5.1.jar!/:na]
        ... 83 common frames omitted
```

解决方法
```bash
url上指定allowPublicKeyRetrieval=true&useSSL=false参数
jdbc:mysql://localhost:3306/Database_dbName?allowPublicKeyRetrieval=true&useSSL=false;
```
