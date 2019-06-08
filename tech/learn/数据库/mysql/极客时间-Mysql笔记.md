---
题目: MySql 学习笔记
---

<!-- TOC -->

- [1. 给字符串加索引](#1-给字符串加索引)
    - [1.1. 全字符串索引](#11-全字符串索引)
        - [1.1.1. 字符串前缀索引](#111-字符串前缀索引)
        - [1.1.2. 统计前缀多少位的区分度](#112-统计前缀多少位的区分度)
        - [1.1.3. 倒序从存储](#113-倒序从存储)
        - [1.1.4. 总结](#114-总结)

<!-- /TOC -->

# 1. 给字符串加索引
## 1.1. 全字符串索引
```sql
alter table SUser add index index1(email)
```

### 1.1.1. 字符串前缀索引
```sql
alter table SUser add index index1(email(6))
```

### 1.1.2. 统计前缀多少位的区分度
```sql
select 
  count(distinct left(email, 4)) as L4,
  count(distinct left(email, 6)) as L6
from SUser;
```

### 1.1.3. 倒序从存储
```sql
select field_list from t where id_card = reverse('input_id_card_string')
```

### 1.1.4. 总结
+ 直接创建完整的索引，可能比较占用空间，但是它能使用覆盖索引，而且回表的可能性更低。
+ 创建前缀索引，节省空间，但是会增加查询扫描的行数，并且不能使用覆盖索引。
+ 倒叙存储，再创建前缀索引，用于绕过字符串本身前缀的区分不不够的问题。
+ 创建Hash字段索引，查询性能稳定，有额外的存储和计算小号，跟第三种方式一样，都不支持范围扫描。