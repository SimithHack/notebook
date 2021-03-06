package com.learn.transaction;

/**
 * 分布式事务测试
 * 什么是分布式事务：涉及操作多个数据库的事务，为了保证分布式系统中数据的一致性。
 * 事务的执行流程 1 建立连接--开启事务--执行方法--回滚或者提交事务
 * 分布式事务面临的问题：其他的事务不知道彼此是否成功，导致数据不一致
 * 解决思路：
 *      让子事务执行完后，先不提交事务，而是等待父事务的通知
 */
public class DistributeTransTest {
}
