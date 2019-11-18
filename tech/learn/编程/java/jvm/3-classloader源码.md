# 类加载器
根类
扩展
应用（系统）
用户自定义

## 类名
+ java.lang.String 普通类名
+ javax.swing.JSpinner$DefaultEditor 内部类
+ java.security.KeyStore$Builder$1 内部类的第一个匿名类

## Class类
+ getClassLoader()每个类都有一个classloader引用
+ 数组类的对象不是由类加载器加载的，而是Java虚拟机根据运行期自动创建的。数组类型的getClassLoader()的方法是跟元素的类加载器一样
```java
@Test
public void getArrayClassLoader(){
    //数组的classloader
    String[] array = new String[]{};
    //因为String是由根类加载器加载的，所以，返回为null
    log.info("classloader={}", array.getClass().getClassLoader());
    TestClassLoader[] loaders = new TestClassLoader[1];
    log.info("classloader={}", loaders.getClass().getClassLoader());
    //如果类型是原生的类型的话，没有classloader
    int[] ints = new int[1];
    log.info("classloader={}", ints.getClass().getClassLoader());
}
```
输出结果
```text
12:37:40.567 [main] INFO com.xiefq.learn.TestClassLoader - classloader=null
12:37:40.570 [main] INFO com.xiefq.learn.TestClassLoader - classloader=sun.misc.Launcher$AppClassLoader@18b4aac2
12:39:39.541 [main] INFO com.xiefq.learn.TestClassLoader - classloader=null
```
## 自定义类加载器
> 默认是使用SystemClassloader
```java
protected ClassLoader() {
    this(checkCreateClassLoader(), getSystemClassLoader());
}
```

