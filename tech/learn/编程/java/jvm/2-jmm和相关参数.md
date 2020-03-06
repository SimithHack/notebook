---
标题: jmm的相关参数
---

# 打印GC回收日志，并选择使用哪一种GC
```
-verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC
```

[jvm详细参数配置说明](https://www.cnblogs.com/redcreen/articles/2037057.html)

## jmm各项内存调整参数

|参数|意义|默认值|
|:--|:--|:--|
|-Xms|初始堆大小|物理内存的1/64(<1GB)|
|-Xmx|最大堆大小|物理内存的1/4(<1GB)|
|-Xmn|年轻代大小(1.4or lator)|注意：此处的大小是（eden+ 2 survivor space).与jmap -heap中显示的New gen是不同的。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小.增大年轻代后,将会减小年老代大小.此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8|
|-XX:NewSize|设置年轻代大小(for 1.3/1.4)||
|-XX:MaxNewSize|年轻代最大值(for 1.3/1.4)||
|-XX:PermSize|设置持久代(perm gen)初始值|物理内存的1/64|
|-XX:MaxPermSize|设置持久代最大值|物理内存的1/4|
|-Xss|每个线程的堆栈大小|JDK5.0以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K.更具应用的线程所需内存大小进行 调整.在相同物理内存下,减小这个值能生成更多的线程.但是操作统对一个进程内的线程数还是有限制的,不能无限生成,经验值在3000~5000左右一般小的应用， 如果栈不是很深， 应该是128k够用的 大的应用建议使用256k。这个选项对性能影响比较大，需要严格的测试。（校长）和threadstacksize选项解释很类似,官方文档似乎没有解释,在论坛中有这样一句话:"”-Xss is translated in a VM flag named ThreadStackSize”一般设置这个值就可以了。|
|XX:ThreadStackSize|每个线程的堆栈大小||
|-XX:NewRatio|年轻代(包括Eden和两个Survivor区)与年老代的比值(除去持久代)|-XX:NewRatio=4表示年轻代与年老代所占比值为1:4,年轻代占整个堆栈的1/5 Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置。|
|-XX:SurvivorRatio|Eden区与Survivor区的大小比值|设置为8,则两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10|
|-XX:LargePageSizeInBytes|内存页的大小不可设置过大， 会影响Perm的大小|=128m|
|-XX:+DisableExplicitGC|关闭System.gc()||
|-XX:MaxTenuringThreshold|垃圾最大年龄|如果设置为0的话,则年轻代对象不经过Survivor区,直接进入年老代. 对于年老代比较多的应用,可以提高效率.如果将此值设置为一个较大值,则年轻 对象会在Survivor区进行多次复制,这样可以增加对象再年轻代的存活 时间,增加在年轻代即被回收的概率该参数只有在串行GC时才有效.|
|-XX:+UseBiasedLocking|锁机制的性能改善||
|-XX:SoftRefLRUPolicyMSPerMB|每兆堆空闲空间中SoftReference的存活时间||
|-XX:PretenureSizeThreshold|对象超过多大是直接在旧生代分配|	单位字节 新生代采用Parallel Scavenge GC时无效另一种直接在旧生代分配的情况是大的数组对象,且数组中无外部引用对象.|
|-XX:+CollectGen0First|FullGC时是否先YGC||
|-XX:TLABWasteTargetPercent|TLAB占eden区的百分比||


## 例子 
> 堆内存20M，指定eden区域大小为8M

