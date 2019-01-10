## GC日志
> 离线分析java程序的性能问题，GC日志对java程序性能开销不大，建议重要的JVM进程都保持开启状态

### 开关GC日志
```
-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintTenuringDistribution
-XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps
```