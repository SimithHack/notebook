# 怎么数据库优化？步骤。
1. 观察数据库运行情况，至少跑1天，查看生产的慢SQL情况。
2. 开启慢查询日志，设置阈值，比如超过5秒的就是慢日志，并将它抓取出来
3. explain + 慢SQL分析
4. show profile
5. 运维经理 或者 DBA，对SQL数据库进行参数调优

## 永远小表驱动大表
比如，我们的代码
```java
//mysql来说更喜欢这种
for(int i to 5){
    for(int j to 1000)
}
//不喜欢这种
for(int i to 1000){
    for(int j to 5)
}
```
小的数据集，驱动大的数据集
```sql
select * from A where id in (select id from B)
```
> 后边哪个子查询 (select id from B) 相当与外层循环  
当B表的数据集必须小于A表的数据集时，用in优于exists  

```sql
select * from A where exists (select 1 from B where B.id = A.id)
相当于
for select * from A
for select * from B where B.id = A.id
```
> 当A表的数据集小于B表的数据集时，用exists优于in

### exists和in的区别
+ exists是用主查询的数据拿到子查询里边去验证，根据验证的True还是False来决定是否保留主查询的数据    
    - 因此子查询中的select * 也可以是select 1，官方说实际执行时会忽略掉select清单，因此没有什么区别
    - exists子查询的实际执行过程可能经过了优化而不是我们理解上的逐条对比。如果担心效率问题，可以进行实际检验确认是否效率问题
    - exists子查询往往也可以用条件表达式，其他子查询或者join来替代，何种最优需要具体问题具体分析
+ 
