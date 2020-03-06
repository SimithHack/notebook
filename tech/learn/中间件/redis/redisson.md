# 参考资料
1. [参考地址](https://github.com/redisson/redisson.wiki.git)

# 使用场景
1. 单节点配置
```java
public RedissonClient redissonClient() throws Exception {
    Config cfg = new Config();
    cfg.useSingleServer()
            .setAddress("redis://master:6379")
        .setRetryAttempts(3);
    cfg.setCodec(new JsonJacksonCodec());
    log.info(cfg.toJSON());
    RedissonClient redissonClient = Redisson.create(cfg);
    return redissonClient;
}
````
