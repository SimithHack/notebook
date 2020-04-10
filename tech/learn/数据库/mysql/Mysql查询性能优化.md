# Mysql查询优化器的局限性
Mysql的查询优化器并不是每种查询都是最优的，对于那些优化器失效的查询语句，我们可以通过改写让优化器经可能有效的工作。

## 关联子查询
+ where 条件的 IN(子查询)

比如
```sql
select * from file where film_id in(
    select film_id from film_actor where actor_id = 1
)
```
mysql执行上面的语句的做法是：将相关的外层表压到子查询中，Mysql认为这样可以更高效的找到数据行。

mysql改写后的执行逻辑
```sql
select * from film as f where exists (
    select * from film_actor as fa where actor_id=1 and fa.film_id=f.film_id
)
```
这就是一个 **相关子查询（dependent subquery）** 可以使用`explain extended`查看查询改写的样子

这种改写就是一种失败的改写，会导致`film`表全表扫描。可以使用`inner join`进行改写
```sql
select f.* from film as f 
    inner join film_actor as fa on fa.film_id = f.film_id
where fa.actor_id=1
```

## 如何使用关联子查询
要自己测试，然后做出判断，很多时候，关联子查询是一种非常合理，自然，甚至是性能最好的写法。