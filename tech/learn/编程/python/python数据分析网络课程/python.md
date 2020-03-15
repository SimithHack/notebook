# 函数

## 装饰器

```python
from functools import wraps

def wrapper1(func):
    @wraps(func)
    def inner(*args, **kargs):
        print("log1 begin")
        retn = func(*args, **kargs)
        print("log1 end")
        return retn
    return inner

def wrapper2(func):
    @wraps(func)
    def inner(*args, **kargs):
        print("log2 begin")
        retn = func(*args, **kargs)
        print("log2 end")
        return retn
    return inner

@wrapper1
@wrapper2
def say():
    print("hello dog")
```

```bash
log1 begin
log2 begin
hello dog
log2 end
log1 end
```

* 带参数的装饰器

> 再包装一层

```python
from functools import wraps
def logger(flag=False):
    def outer(func):
        @wraps(func)
        def inner(*args, **kargs):
            if flag:
                print("log1 begin")
                retn = func(*args, **kargs)
                print("log1 end")
                return retn
        return inner
    return outer


@logger(True)
def say():
    print("hello dog")

@logger(False)
def eat():
    print("eat dog")

say()
eat()
```

```bash
log1 begin
hello dog
log1 end
```

## 迭代器

如果一个数据类型中油 `iter` 方法，则这个数据类型就是 `可以迭代` 的类型

如果一个数据类型中油 `iter` 和 `next` 两个方法，那么这个数据类型就是一个 `迭代器` 类型

* 节省内存空间

 ```python
print("__iter__" in dir(2))
print("__iter__" in dir([2]))
print("__iter__" in dir({2}))
print("__iter__" in dir({"a": 1}))
print("__iter__" in dir((1,2)))
print("__iter__" in dir(list((1,2))))
print("__iter__" in dir("a"))
 ```

```bash
False
True
True
True
True
True
True
```

* 文件操作是迭代器

```python
f = open(r"d:\code\python\learn\1.txt", "r", encoding="utf-8")
print("__iter__" in dir(f))
print("__next__" in dir(f))
for l in f:
    print(l)
```

* 可迭代类型，可以通过 `__iter__` 方法转换成迭代器。

> 对于可迭代类型，直接通过 `__next__` 是取不到值的，需要通过先调用 `__iter__` 方法转换为迭代器类型

```python
a=[1,2,3]
iter = a.__iter__()
print(iter.__next__())
```

* for循环不能迭代不可迭代的对象

## 生成器

> 生成器函数没有返回值，只有 `yield` 它实际就是一个迭代器. 相当于暂停键

```python
def simple_gen():
    print("1")
    yield 1
    print("2")
    yield 2

gen = simple_gen()
for i in gen:
    pass
```

```bash
1
2
```

第二个例子

```python
def eat(n):
    for i in range(n):
        yield "eat %s"%i

eatter = eat(1000)

for i in range(10):
    print(eatter.__next__())

print("="*10)
print(eatter.__next__())
```

```bash
eat 0
eat 1
eat 2
eat 3
eat 4
eat 5
eat 6
eat 7
eat 8
eat 9
==========
eat 10
```

## 列表推导式

```python
[x for x in range(10) if x%3 == 0]
```

列表生成式

```python
gen = (i*2 for i in range(5))
//有下面三种取值方式
gen.__next__()
for n in gen:
    pass
list(gen)
```

* 所有的生成器都满足迭代器的特性
* 生成器不取值是不执行的

生成器的面试题，这种题for循环拆开

```python
def demo():
    for i in range(4):
        yield i
g=demo()
g1=(i for i in g)
g2=(i for i in g1)
print(list(g1))
print(list(g2))
```

```bash
[0, 1, 2, 3]
[]
```

因为，g1已经取完了，demo才是生成器的根

## 三元运算

```python
a = a if b>1 else b
```

## 匿名函数lambda

```python
lambda a, b: a+b
```

## 内置函数

* `locals()` 和 `globals()` 

```python
name="hel"
def func():
    a=1
    b=2
    print(locals())
    print(globals())
```

```bash
{'a': 1, 'b': 2}
{'__name__': '__main__', '__doc__': None, '__package__': None, '__loader__': <_frozen_importlib_external.SourceFileLoader object at 0x0000024C9A1ACF88>, '__spec__': None, '__annotations__': {}, '__builtins__': <module 'builtins' (built-in)>, '__file__': 'D:/code/python/learn/day4/wrapper_my.py', '__cached__': None, 'name': 'hel', 'func': <function func at 0x0000024C9A2990D8>}
```

* `eval`

> 调用字符串的代码，并返回值

* `exec`

> 效果和 `eval` 一样，区别是没有返回值

eval和exec，处理用户的输入，处理网络输入都要注意安全性

* callable

可调用

```python
a=1
print(callable(a))
x = lambda : print("hello")
print(callable(x))
```

* dir

查款一个数据可以调用哪些方法

* print用法

  * 指定结束符

    ```python
    print("1", end="\t")
    print("1", end="\t")
    print("1", end="\t")
    ```

    ```bash
    1	1	1	
    ```

  * 指定输出到文件，为None的时候输出到console

    ```python
    with open("1.txt", "a") as f:
        print("hello", file=f)
    ```

  * print来一个99乘法表

    ```python
    for i in range(1, 10, 1):
        for j in range(1, i+1):
            print("%s x %s = %s"%(i, j, i*j), end="\t")
        print()
    ```

  * 打印进度条

    ```python
    import time
    for i in range(0, 101, 2):
        time.sleep(0.1)
        char_num=i//2
        per_str="\r%s%%\t: %s" % (i, '*'*char_num)
        print(per_str, end='', flush=True)
    ```

    ```bash
    100%	: **************************************************
    ```

* type 求类型
* hash 就hash值
* id() 求内存地址

* 和数值相关

  | 函数名                                 | 说明     |
  | -------------------------------------- | -------- |
  | bool(), int(), float(), complex()      | 强制转换 |
  | bin(), oct, hex                        | 进制转换 |
  | abs, divmod, round, pow, sum, min, max | 数学运算 |

  ```python
  print(hex(8))
  print(pow(2, 3))
  print(2**3)
  print(sum([1,2,3,4,5]))
  print(max([x for x in range(1, 10)]))
  print(float("2.3"))
  print(round(2.118, 2))
  print(divmod(5,2)) #商，余
  print(min([1,-4,2,3],key=abs)) # 求绝对值最小的值
  ```

  ```bash
  0x8
  8
  8
  15
  9
  2.3
  2.12
  (2, 1)
  1
  ```

* `ord` ascii码， chr() 反

* `repr` 打印某个变量值的时候，更容易区分类型

* `list.reverse()` `reversed()函数`

  > 前者将list列表翻转，reversed()返回一个翻转的迭代器

* `filter()` 函数返回的是一个生成器

  ```python
  a=[x for x in range(1, 10)]
  retn = filter(lambda n : n%3 == 0, a)
  # 只有访问迭代器的时候，func才会被执行
  for i in retn:
      print(i)
  ```

* `map` 也是返回一个生成器

  ```python
  a=[x for x in range(1, 10)]
  retn = map(lambda n : n%3 == 0, a)
  # 只有访问迭代器的时候，func才会被执行
  for i in retn:
      print(i, end=" ")
  ```

  ```bash
  False False True False False True False False True 
  ```

* `enumerate` 函数，返回一个枚举. 给可迭代的对象添加序号

  ```python
  a=[x for x in range(1, 10)]
  for en in enumerate(a, 2):
      print("index=%s, value=%s"%en)
  ```

  ```bash
  index=2, value=1
  index=3, value=2
  index=4, value=3
  index=5, value=4
  index=6, value=5
  index=7, value=6
  index=8, value=7
  index=9, value=8
  index=10, value=9
  ```

* `zip` ，拉链方法

  ```python
  key=["name", "age"]
  value=["xiefq", 32]
  retn = zip(key, value)
  for it in retn:
      print(it)
  ```

  返回元组

  ```bash
  ('name', 'xiefq')
  ('age', 32)
  ```

  返回字典

  ```python
  dict(zip(key,value))
  ```

  min的另一种用法

  ```python
  age=[1,21,-32,44]
  column=["age"]
  retn = zip(column*len(age), age)
  min_value = min(retn, key=lambda entry: abs(entry[1]))
  print(min_value
  ```

  ```bash
  ('age', -32)
  ```

* `sorted` 函数

  ```python
  age=[1,21,-32,44]
  l = sorted(age, reverse=True)
  print(l)
  l2 = sorted(age, key=lambda x: x%10) # 根据除余后来排序
  print(l2)
  ```

  ```bash
  [44, 21, 1, -32]
  [1, 21, 44, -32]
  ```


# 常用模块

> 大部分内置模块，都是c实现，python的源码是看不到的，只是一堆注释说明

## 模块分类

* 内置模块
* 扩展模块/三方模块 django, flask, beautifulsoup, pandas
* 自定义模块

## random模块

```python
import random
retn = random.random()
print(retn)
i1 = random.randint(1, 20)
print(i1)
# 随机范围+步长
i2 = random.randrange(1, 20, 2)
print(i2)
# 打乱顺序
a1 = [1,2,3,5,6]
random.shuffle(a1)
print(a1)
# 随机抽取一个
c1 = random.choice(a1)
print(c1)
# 随机抽取多个
s1 = random.sample(a1, 2)
print(s1)
```

```bash
0.8846932749016855
17
1
[5, 3, 2, 1, 6]
5
[2, 6]
```

### 产生4位的随机验证码

```python
import random
a1 = [chr(x) for x in range(ord('a'), ord('z')+1)]
A1 = [chr(x) for x in range(ord('A'), ord('Z')+1)]
i1 = [str(x) for x in range(10)]
my_list = list(a1)
my_list.extend(A1)
my_list.extend(i1)
print("".join(random.sample(my_list, 4)))
```

```bash
['l', 1, 'a', 8]
```

## 正则模块

```python
import re
matches = re.findall('\d+', "wokao123kdakf")
if len(matches) > 0:
    print(matches[0])
```

### match

match默认从头开始匹配，search是任意位置

```python
s1 = "I'm 34"
retn1 = re.search("(\d+)", s1)
print(retn1.group())
retn2 = re.match("(\d+)", s1)
print(retn2)
s2 = "34 years old"
retn3 = re.match("(\d+)", s2)
print(retn3.group())
```

```bash
34
None
34
```

从输出结果可见，match的功能用法

### split

> 分割

```bash
s1="wo12ta12323dd"
a1 = re.split("\d+", s1)
print(a1)
```

### sub

> 替换

```python
s1="wo12ta12323dd"
a1 = re.sub("\d+", "--", s1, 1)
print(a1)
```

```bash
wo--ta12323dd
```

把s1中，正则匹配的，用 `--` 替换, 后边是替换的个数，不写就全部替换

### subn

> 用法类似于 `sub` 只是它返回一个元组，第一个是替换后的结果，第二个是替换的个数

### compile

> 将正则表达式预先编译，用在多次使用的场景

```python
rule = re.compile("\d+")
rule.match()
rule.split()
rule.sub()
...
```

### finditer

> 返回一个迭代器，节省空间

## 正则分组，分组优先显示

```python
s1="suasan12tom18lili21"
# 优先显示分组
retn1 = re.findall("[a-z]+(\d+)", s1)
print(retn1)
# 取消分组优先
retn2 = re.findall("[a-z]+(?:\d+)", s1)
print(retn2)
# 起名字
retn2 = re.search("[a-z]+(?P<age>\d+)", s1)
print(retn2.group("age"))
```

```bsh
['12', '18', '21']
['suasan12', 'tom18', 'lili21']
12
```

### 练习-爬天气

```python
def get_cities():
    rsp = requests.get("http://www.weather.com.cn/forecast/")
    content = rsp.content.decode("utf-8")
    cp = re.compile("href=\"(?P<url>[^\"]*?)\".*?target=\"_blank\">(?P<city>.*?)</a>", re.S)
    cities = []
    for wrapperRe in re.finditer(".*?class=\"maptabboxin\">(?P<wrapper>.*?)</div>", content, re.S):
        wrapper=wrapperRe.group("wrapper")
        for c in cp.finditer(wrapper):
            city = {"city": c.group("city").strip(), "url": c.group("url").strip()}
            get_weather(city)


def get_weather(city):
    rsp = requests.get(city["url"])
    content = rsp.content.decode("utf-8")
    wr = re.findall("class=\"t\".*?class=\"sk\".*?class=\"tem\".*?<span>(?P<weather>.*?)</span>", content, re.S)
    if len(wr) > 0:
        print("城市：%s, 温度%s度" %(city["city"], wr[0]))
```

```bash
城市：北京, 温度1度
城市：天津, 温度5度
城市：石家庄, 温度5度
城市：太原, 温度-4度
城市：呼和浩特, 温度-4度
城市：保定, 温度3度
城市：大同, 温度-8度
城市：包头, 温度-7度
城市：承德市, 温度-3度
城市：晋中, 温度0度
城市：通辽, 温度-2度
```

## os模块

os.path.getsize("绝对路径")，获取文件的大小

os.path.isfile() 是否文件

os.path.isdir() 是否文件夹

os.path.join() 跨平台文件路径拼接

os.listdir() 文件夹下所有的文件或文件夹

### 练习：计算文件夹的大小

```python
def get_size(path):
    size=0
    if os.path.isdir(path):
        for f in os.listdir(path):
            file_path = os.path.join(path, f)
            if os.path.isdir(file_path):
                size += get_size(file_path)
            else:
                size += os.path.getsize(file_path)
    elif os.path.isfile(path):
        size += os.path.getsize(path)
    return size
```

### 其他方法

### os 方法

![](python.assets\image-20200312213416741.png)

* 文件操作
* 文件操作
* 执行操作系统命令
* 工作目录相关

 ![image-20200312220007797](python.assets\image-20200312220007797.png)

## 时间模块

时间格式化

* %y 两位数年份
* %Y 四位数年份
* %m 月份
* %d 月内的某一天
* %H 24小时制
* %I  12小时制
* %a 本地简化星期名称
* %A 本地完整星期名称
* %b 本地简化月份名称
* %B 本地完整月份名称
* %c 本地相应的日期表示和时间表示
* %j 年内的某一天 （001-366）
* %p 本地A.M或者P.M的等价符
* %U 一年中的星期数（00，53）
* %w 星期（0-6），星期天位星期的开始
* %W 一年中的星期数
* %x 本地相应的日期表示
* %z 本地时区名称

### 几种时间格式之间的转换

![image-20200312224207662](python.assets\image-20200312224207662.png)

```python
stime="2020010111:33:23"
t1 = time.strptime(stime, "%Y%m%d%H:%M:%S")
s1 = time.mktime(t1)
print(s1)
```

```python
stamp = 1577849603
stuc_t = time.localtime(stamp)
print(stuc_t)
string_t = time.strftime("%Y-%m-%d %H:%M:%S", stuc_t)
print(string_t)
```

### datetime

可以计算时间的加减，可以拼汉字

```python
from datetime import datetime,timedelta
n = datetime.now()
print(n.day)

t1=datetime(2017, 11, 23)
t2=datetime(2018, 11, 23)
delta= t2-t1
print(type(delta))
print(t2.date())
tmp = t1 + timedelta(days=2)
print(tmp.date())
```

## 练习：求两个时间的时间差

```python
import time
from datetime import datetime,timedelta
d1 = datetime.now()
d2 = datetime(2010, 3, 13, 16, 50, 20)
delta = d1-d2
print(d1)
print(delta.total_seconds())
print(time.localtime(0))
print(time.localtime(delta.total_seconds()))
```

## sys模块

### sys.modules 

> 当前代码执行位置，解释器中导入的所有模块, key是模块的名称，value是这个模块对于的内存位置。

用处： 通过名字拿到内存地址，就可以使用这个模块。

### sys.path

> python找代码的所有路径，类似于Java的 classpath . 类名不要起内置模块的名字，有覆盖内置模块的风险。

### sys.argv

> 命令行传递过来的参数，第一个参数是脚本路径。因为执行的时候 `python path.py arg1 arg2 ...`

53