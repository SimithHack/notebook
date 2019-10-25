## 对于JAVA GC的理解（上）

## 虚拟机
+ KVM 嵌入式，手机平台等
+ classic jvm
+ ExtractVm
+ Hotspot
+ JRockit
  > Bea(被oracle收割) 专注服务器应用 垃圾回收 missioncontrol服务套件（诊断内存泄漏，运行代价低）

## 类的加载
```
对于静态字段来说，只有直接定义了该字段的类才会被初始化，当一个类在在初始化时，要求其全部都已经加载完
-XX:+TraceClassLoading 打印出类的加载信息
```
+ `-XX:+<option>` 表示开启option选项
+ `-XX:-<option>` 表示关闭option选项
+ `-XX:<option>=<value>` 将option的值设置为value

### 静态代码块，常量，静态常量
+ 静态代码块在类加载的时候就会执行
```java
public class Test1 {
    public static void main(String[] args) {
        System.out.println(InnerClass.name);
    }
}
class InnerClass {
    public static String name = "xiefq";
    static {
        System.out.println("static block");
    }
}
```
输出结果是
```
static block
xiefq
```
但是如果我们修改为 `public static final String name = "xiefq"` 执行结果如下
```
xiefq
```
这是因为，常量在编译阶段，会存入到调用这个常量所在类的常量池中，所以，本质上并没有直接引用到定义常量的类，因此并不会出发定义
常量的类的初始化。甚至将InnerClass即定义常量的类的class文件删除都是不会发生执行运行期的错误的。

反编译后我们看里边的信息 `javap -c`

+ 如果静态常量是方法执行的结果，就不会在编译的时候做上述处理
```java
public class Test1 {
    public static void main(String[] args) {
        System.out.println(InnerClass.name);
    }
}
class InnerClass {
    public static String name = UUID.randomUUID().toString();
    static {
        System.out.println("static block");
    }
}
```
输出结果
```
static block
83a70b8b-fc61-41ff-b619-d23d0de59cc0
```

+ 关于数组的初始化

定义数组不会导致主动使用
```java
public class Test1 {
    public static void main(String[] args) {
        InnerClass[] ic = new InnerClass[1];
    }
}
class InnerClass {
    static {
        System.out.println("static block");
    }
}
```
没有任何输出，因为它不是InnerClass的实例，它是类型是 `[Lcom.xiefq.learn.classloader.InnerClass;` 它是一个数组
它是JVM在运行其动态生成的。对于原生类型的数组类型，比如 `int[] a = new int[1];` 的类型是 `[I`

+ 关于接口的初始化
当一个接口被初始化的时候，并不要求其父接口完成初始化
```java
public class Test1 {
    public static void main(String[] args) {
        System.out.println(InnerClass2.b);
    }
}
interface InnerClass1 {
    public static int a = 5;
}
interface InnerClass2 extends InnerClass1 {
    public static int b = 6;//接口本身里边的成员变量就是 public static final的
}
```
innerclass1是不会被加载的，但是如果我们把 `int b=new Random().nextInt()`的时候，就会让`InnerClass1`加载。

但是我们改成下面的情况
```java
public class Test1 {
    public static void main(String[] args) {
        System.out.println(InnerClass2.b);
    }
}
interface InnerClass1 {
    public static int a = new Random().nextInt();
}
interface InnerClass2 extends InnerClass1 {
    public static int b = 6;
} 
```
`InnerClass1`的常量是不会触发初始化的，证明，接口的常量是不会主动初始化父亲接口的常量。除非主动使用。

看下面这个例子
```java
public class Test1 {
    public static void main(String[] args) {
        Singleton s = Singleton.getInstance();
        System.out.println(Singleton.count1);
        System.out.println(Singleton.count2);
    }
}
class Singleton {
    public static int count1;
    public static Singleton instance = new Singleton();
    private Singleton(){
        count1++;
        count2++;
    }
    public static int count2 = 0;
    public static Singleton getInstance(){
        return instance;
    }
}
```
输出结果是
```
1
0
```
`Singleton.getInstance();`这句代码，调用静态类的静态方法，主动使用，类会被加载。相关的静态变量赋值和0值初始化。然后，类被
加载，构造方法被调用，`count1`和`count2`的值都是1，接着真正的初始化`public static int count2 = 0` `count2`又变为0了。

### 助记符
+ ldc表示将int,float或是String类型的常量值从常量池中推送值栈顶
+ bipush表示将单字节（-128-127）的常量值推送至栈顶
+ sipush表示将一个短整型常量值推送到栈顶
+ iconst_1表示将数字1推送到栈顶
+ aneewarray 表示创建一个引用类型的数组（接口，数组，类）并将其引用压入栈顶
+ newarray 创建一个指定的原始类型的数组（int,char）等，并将其压入栈顶