

# 使用Series表达不可变的数据

以类似数组的方式存储相同类型，支持数据自动对齐。

## 配置panda

```python
# 导入numpy和pandas并配置
import numpy as np
import pandas as pd
pd.set_option("display.notebook_repr_html", False)
pd.set_option("display.max_columns", 8)
pd.set_option("display.max_rows", 10)
pd.set_option("display.width", 80)
# 导入date
import datetime
from datetime import datetime, date
# 导入matplotlib绘图
import matplotlib.pyplot as plt
```

## 创建series

* 使用列表和字典创建

  * 使用列表

  ```python
  s1 = pd.Series(range(10, 12))
  s2 = pd.Series([0, 2, 3])
  s3 = pd.Series(['a'] * 10)
  s4 = pd.Series(list("abcd"))
  print(s1)
  ```
  ```bash
  0    10
  1    11
  dtype: int64
  ```

  * 使用字典

  ```python
  s1 = pd.Series({
      "name": "xiefq",
      "age": 11
  })
  print(s1)
  ```

  ```bash
  name    xiefq
  age        11
  dtype: object
  ```

* 使用numpy的arrays创建

  ```python
  s1 = pd.Series(np.arange(10, 15, 2)) # 类似于range的功能 
  s2 = pd.Series(np.linspace(10, 15, 5)) # 平均产生5个数值在10,15之间
  s3 = pd.Series(np.random.normal(size=5)) # 随机生成5个数
  ```

* 使用scalar值创建

  ```python
  s1 = pd.Series(range(5))
  s2 = s1 * 2
  print(s2)
  ```

  ```bash
  0    0
  1    2
  2    4
  3    6
  4    8
  dtype: int64
  ```

## `.index` 和 `.value` 属性

```python
s1 = pd.Series(range(5))
print(s1.index) # RangeIndex类型
print(s1.values) # numpy.ndarray 类型
```

```bash
RangeIndex(start=0, stop=5, step=1)
[0 1 2 3 4]
```

* RangeIndex 包含了从start 到stop之间，并且步长是step的数值

## `size` 和 `shape` 属性

```python
s1 = pd.Series(range(5))
print(s1.size)
print(s1.shape)
```

```bash
5
(5,)
```

## 创建的时候指定index

```python
s1 = pd.Series(np.linspace(10, 20, 3), index=list("abc"))
print(s1)
print(s1['b'])
```

```bash
a    10.0
b    15.0
c    20.0
dtype: float64
15.0
```

`heads` , `tails` 和 `takes` 

头元素，尾元素，指定位置

```python
s1 = pd.Series(np.linspace(10, 20, 6), index=list("abcefg"))
print(s1.head(n=2)) # 前2个
print(s1.tail(n=2)) # 后两个
print(s1.take([1, 3, 4])) # 返回指定位置
```

```bash
a    10.0
b    12.0
dtype: float64
f    18.0
g    20.0
dtype: float64
b    12.0
e    16.0
f    18.0
dtype: float64
```

## 获取值

```python
s1 = pd.Series(np.linspace(10, 20, 6), index=list("abcefg"))
print(s1[[4, 1]])
print(s1[['a', 'f']])
```

```bash
f    18.0
b    12.0
dtype: float64
a    10.0
f    18.0
dtype: float64
```

上述方式按照位置取值和按照标签取值会引起歧义，推荐使用 `iloc()` 和 `loc()` 方法。使用方式相同。

## Series切片

slice语法 `start:end:step` 并且，每个节段都是可选的。

```python
s1 = pd.Series(np.linspace(10, 20, 6), index=list("abcefg"))
print(s1[2:6:2])
print(s1[:6:3])
print(s1[::-1]) # 倒序
print(s1[0:3][::-1]) # 局部倒序
print(s1[-2:]) # 后2个
```

```bash
c    14.0
f    18.0
dtype: float64
a    10.0
e    16.0
dtype: float64
g    20.0
f    18.0
e    16.0
c    14.0
b    12.0
a    10.0
dtype: float64
c    14.0
b    12.0
a    10.0
dtype: float64
f    18.0
g    20.0
dtype: float64
```

## 通过 `index` 对齐

```python
s1 = pd.Series(np.linspace(10, 20, 6), index=list("abcefg"))
s2 = pd.Series(np.arange(20, 23), index=list("cef"))
s3 = s1 + s2
print(s3)
```

```bash
a     NaN
b     NaN
c    34.0
e    37.0
f    40.0
g     NaN
```

应用一个标量值 apply a scalar to a Series

```python
s1 = pd.Series(np.arange(1, 3), index=list("ab"))
print(s1 * 2)
```

```bash
a    2
b    4
dtype: int32
```

* 应用标量值给Series，实际上的步骤是 
  * 先创建一个相同index的所有元素的值都是scalar的Series `pd.Series(2, index=s1.index)`
  * 然后两个Series相乘 

* 如果不能进行标签对齐的话，panda返回 `NaN`

* panda的index标签不要求唯一，不唯一的标签，做笛卡尔积比如，总共有`n*m`个相同元素。

  ```python
  s1 = pd.Series(np.arange(1, 5), index=list("aaab"))
  s2 = pd.Series(np.arange(1, 5), index=list("aabb"))
  print(s1 + s2)
  ```

  ```bash
  a    2
  a    3
  a    3
  a    4
  a    4
  a    5
  b    7
  b    8
  ```

## Boolean选择

Series里每个值都应用一下boolean表达式，然后得出结果Series

```python
s1 = pd.Series(np.random.randint(10, 100, 5))
print(s1%2 == 0)
```

```bash
0     True
1    False
2    False
3    False
4     True
```

然后可以提取出结果 `print(s1[s1%2==0])`

```bash
0    88
1    16
3    52
```

也可以是多个表达式

`print(s1[(s1>50) & (s1<60)])`

```bash
0    52
```

* `.all()` 方法可以判断是否所有的元素都满足条件

  ```python
  (s>0).all() # 返回True 或者 False
  ```

* `.any()` 只要有一个元素满足条件就可以返回True

* `.sum()` 是统计满足条件的元素有多少个

## panda reindex

* 根据新的label排序
* 对于匹配不到的label，使用`NaN`代替
* 对于匹配不到label的值，我们也可以自定义默认值填充逻辑

```python
s1 = pd.Series(np.random.randint(10, 100, 5))
# s.index的方式指定所以，条目必须和值匹配
s1.index = ['chinese', 'english', 'math', 'history', 'political']
# s.reindex labels的数量不需要通值条目
s2 = s1.reindex(['chinese', 'english', 'computer'])
print(s2)
```

```bash
chinese     59.0
english     46.0
computer     NaN
```

* `index`指定新索引是`in place`的修改，改的就是原`series` ，而`reindex`是修改后生成一个新的series

* `reindex`还能对没匹配到的label使用默认值替代。

  ```python
  s1.reindex(list("abcd"), fill_value=0) # 使用默认值填充
  s2=pd.Series(list("abc"))
  s3=s2.reindex(np.arange(0,5), method="ffill") #使用前一个值填充
  print(s3)
  ```

  ```bash
  0    a
  1    b
  2    c
  3    c
  4    c
  ```

  > `ffill` 是使用前一个值填充 `bfill` 是使用后一个值填充

**整合两个`series` 字段类型不一样但是代表的值是一样的情况**

```python
s1 = pd.Series([1 for _ in range(5)])
s2 = pd.Series(np.random.randint(1, 10, 5), index=['1', '2', '3', '4', '5'])
# 修改s1的数据类型为int
s2.index = s2.index.values.astype(int)
print(s1 + s2)
```

```bash
0    NaN
1    8.0
2    9.0
3    6.0
4    2.0
5    NaN
```

## 原址修改Series

```python
s2=pd.Series(list("abc"))
print(s2[1],"size=", s2.size)
del s2[1]
print(s2, "size=", s2.size)
```

```bash
b size= 3
0    a
2    c
dtype: object size= 2
```

### slice操作

> slice是对原series的视图，对它返回的结果进行修改会反应到原series

```python
s1 = pd.Series(list("abcdef"))
s2 = s1.copy()
s3 = s2[:2]
print(s1, s2, s3)
s3[1] = '*'
print(s1, s2, s3)
```

```bash
0    a
1    b
2    c
3    d
4    e
5    f
dtype: object 0    a
1    b
2    c
3    d
4    e
5    f
dtype: object 0    a
1    b
dtype: object
0    a
1    b
2    c
3    d
4    e
5    f
dtype: object 0    a
1    *
2    c
3    d
4    e
5    f
dtype: object 0    a
1    *
dtype: object
```

* 可以看到 `copy`是完全复制一个新的，而slice是在原series上操作

# DataFrame

每个列就是一个series，可以类比excel的datasheet.

## 创建

1. 创建一列数据

```python
pd.DataFrame(np.arange(1, 6)) 
```

2. 使用多维数组创建多列

```python
pd.DataFrame(np.array([10, 11], [20, 21]))
```

```bash
    0   1
0  10  11
1  20  21
```

使用 `.columns`属性可以访问`DataFrame`的列对象, 如果没有指定`columns`属性， 默认返回`RangeIndex(start=0, stop=9, step=1)`，否则返回`Index(['语文', '数学'], dtype='object')`对象。

3. 指定columns

```python
f1 = pd.DataFrame(np.array([[1, 2], [8, 9]]), columns=["语文", "数学"])
print(f1.columns)
```

```bash
Index(['语文', '数学'], dtype='object')
```

`.len(DataFrame)`返回有多少行，即有多少个`series`

```python
f1 = pd.DataFrame(np.array([[1, 2, 3], [8, 9, 10]]), columns=["语文", "数学", "英语"])
print(len(f1))
```

```bash
2
```

`.size` 属性返回有多少个元素

```python
f1 = pd.read_csv("01.csv", sep=" ")
print(f1)
print(f1.size, f1.shape)
```

```bash
   chinese  math  history
0       92    67       87
1       91    88       81
2       82    76       69
9 (3, 3)
```

## 数据访问

1. `[]`, `.loc[]` , `.iloc[]`

2. 使用`[]`选择`columns`

   ```python
   f1 = pd.read_csv("01.csv", sep=" ")
   print(f1["history"].head(1))
   print(f1[["chinese", "math"]].head(1))
   print(f1.chinese)
   ```

   ```bash
   0    87
   Name: history, dtype: int64
      chinese  math
   0       92    67
   0    92
   1    91
   2    82
   Name: chinese, dtype: int64
   ```

   * `f1.chinese`这种方式对于有空格的column来说是不能用的

3. 使用 `.loc[]` 和 `.iloc[]` 选择行

   ```python
   f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
   print(f1.loc["张三"])
   print(f1.iloc[[0, 2]])
   ```

   ```bash
   chinese    92
   math       67
   history    87
   Name: 张三, dtype: int64
         chinese  math  history
   name                        
   张三         92    67       87
   王五         82    76       69
   ```

   * 使用`.iloc`就只能使用下标索引来选择

4. 使用`.index.get_loc('index_name')`获取数值索引值

   ```python
   print(f1.index.get_loc("张三"))
   ```

5. `scalar`某列或某行获取标量值

   > 获取王五的语文成绩，格式是 `[行，列]`

   ```python
   print(f1.at["王五", "chinese"])
   print(f1.iat[2, 0])
   ```

   ```bash
   82
   82
   ```

## 数据切片-slice

1. 使用 `[]` 切分行

   > 前5行

   ```python
   print(f1[:2])
   print(f1["张三":"李四"])
   ```

   * 为了防止歧义，最好使用 `.iloc` 和 `.loc`进行切分

2. boolean 选择

   > 语文成绩大于90分的学生

   ```python
   print(f1[f1["chinese"]>90])
   ```

   ```bash
         chinese  math  history
   name                        
   张三         92    67       87
   李四         91    88       81
   ```

   * 多个条件注意用 `()`

3. 跨列和行搜索

   > 查询张三和王五的语文和历史成绩

   ```python
   f1.loc[["张三", "王五"]][["chinese", "history"]]
   ```

   ```bash
         chinese  history
   name                  
   张三         92       87
   王五         82       69
   ```

# 维护`DataFrame`结构

## 重命名列

`.rename()`传入一个字典，key 是需要重命名的列，value是命名后的列。

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f2 = f1.rename(columns={"chinese": "语文"})
print(f1.columns)
print(f2.columns)
```

```bash
Index(['chinese', 'math', 'history'], dtype='object')
Index(['语文', 'math', 'history'], dtype='object')
```

* 上边`f1`是没有被修改的，如果要直接在`f1`上修改，需要加 `inplace=True`这个参数

## 添加新列

> 使用 `[]` 或者 `insert` 方法可以添加新列

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f1["english"] = pd.Series(np.random.randint(70, 100, len(f1)), index=f1.index)
print(f1)
```

```bash
      chinese  math  history  english
name                                 
张三         92    67       87       84
李四         91    88       81       83
王五         82    76       69       96
```

> insert可以插入到指定的位置

```python
f1.insert(1, "english", pd.Series(np.random.randint(80, 100, len(f1)), index=f1.index))
print(f1.index)
```

```bash
Index(['chinese', 'english', 'math', 'history'], dtype='object')
```

> `loc`方式添加在尾部添加

```python
f1.loc[:,"new_col"] = 0
print(f1.columns)
```

```bash
Index(['chinese', 'math', 'history', 'new_col'], dtype='object')
```

> 使用拼接添加列

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f2 = pd.DataFrame(pd.Series(np.random.randint(80, 100, len(f1)), index=f1.index), columns=["computer"])
f3 = pd.concat([f2, f1], axis=0)
f4 = pd.concat([f2, f1], axis=1)
print(f3)
print(f4)
```

```bash
      computer  chinese  math  history
name                                  
张三        92.0      NaN   NaN      NaN
李四        96.0      NaN   NaN      NaN
王五        85.0      NaN   NaN      NaN
张三         NaN     92.0  67.0     87.0
李四         NaN     91.0  88.0     81.0
王五         NaN     82.0  76.0     69.0
      computer  chinese  math  history
name                                  
张三          92       92    67       87
李四          96       91    88       81
王五          85       82    76       69
```

* 观察下 `axis=0` 和 `axis=1` 两种情况的差异。

## 重排 列的顺序

* 倒序

  ```python
  f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
  print(f1.columns)
  reversed_columns = f1.columns[::-1]
  f2 = f1[reversed_columns]
  print(f2.columns)
  ----
  Index(['chinese', 'math', 'history'], dtype='object')
  Index(['history', 'math', 'chinese'], dtype='object')
  ```

## 替换某一列的内容

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
print(f1.chinese)
f1.chinese = 0
print(f1.chinese)
----
name
张三    92
李四    91
王五    82
Name: chinese, dtype: int64
name
张三    0
李四    0
王五    0
Name: chinese, dtype: int64
```

> 还可以使用分片替换

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f1.loc[::,"chinese"] = 0
print(f1.chinese)
```

## 删除某一列

`del`关键字，`.pop()`方法或者`.drop`方法都可以删除数据

* `del` 原址删除
* `pop` 删除并返回
* `drop(labels, axis=1)` 返回删除的列，原`dataframe`不变

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f2 = f1.drop(["history"], axis=1)
print(f2)
----
      chinese  math
name               
张三         92    67
李四         91    88
王五         82    76
```

## 添加新行

append方法，添加新行

```python
f1 = pd.read_csv("01.csv", sep=" ", index_col='name', encoding="utf8")
f2 = f1.append(pd.Series([77, 89, 63], index=f1.columns, name="赵六"))
print(f2)
----
      chinese  math  history
name                        
张三         92    67       87
李四         91    88       81
王五         82    76       69
赵六         77    89       63
```

## 添加和替换行

使用`loc[label]`添加行，如果 `label` 存在，就替换，不存在就添加

```python
f1 = pd.read_csv("01.csv", sep=" ",  encoding="utf8", index_col="name")
f1.loc["王孙"]=np.random.randint(60, 100, 3)
f1.loc["张三"]=np.random.randint(60, 100, 3)
print(f1)
----
      chinese  math  history
name                        
张三         61    65       90
李四         91    88       81
王五         82    76       69
王孙         85    70       70
```

## 删除行

* `drop([labels])`删除并返回删除后的结果

  ```python
  f1 = pd.read_csv("01.csv", sep=" ",  encoding="utf8", index_col="name")
  droped_items = f1.drop(["张三", "李四"])
  print(droped_items)
  ----
        chinese  math  history
  name                        
  王五         82    76       69
  ```

* 通过boolean选择删除

  ```python
  f1 = pd.read_csv("01.csv", sep=" ",  encoding="utf8", index_col="name")
  bool_sel = f1.chinese > 90
  f2 = f1[bool_sel].copy()
  ```

  > 这种删除，以及用slice进行删除一样，原DataFrame并没删除，而且对新的DataFrame进行修改会影响到老的

# 索引数据

类似与数据库索引，加快我们数据的查找。Panda的索引类型有 `RangeIndex` ，`Int64Index`, `CategoricalIndex`, `Float64Index`, `DatetimeIndex`, `PeriodIndex`. 

**数据准备**

> 随机产生10000个随机数

```python
f1=pd.DataFrame({"foo": np.random.random(10000), "key": range(10000, 19999)})
print(f1.tail(2))
----
           foo    key
9998  0.883271  19998
9999  0.900487  19999
```

使用 `jupter-notebook`的`timeit`语句，检查此时过滤用时

```python
%timeit f1[f1.key==19998]
----
703 µs ± 27.4 µs per loop (mean ± std. dev. of 7 runs, 1000 loops each)
```

可以发现，经过了7次运行，每次1000次循环，最快`703us`

## 添加索引

```python
f2 = f1.set_index(["key"])
%timeit f2.loc[10500]
----
148 µs ± 5.09 µs per loop (mean ± std. dev. of 7 runs, 10000 loops each)
```

最快`148 us`

索引可以加快查询速度，但是需要花时间去构造并且需要消耗更多的内存。

### Index

默认就是这种索引类型

```python
f1 = pd.DataFrame({
    "name": ["张三", "李四"],
    "age": [23, 25]
})
----
Index(['name', 'age'], dtype='object')
```

> 这种索引要求存储的值必须是可hash的python类型，它使用的是hash查找。通常用于字母列。

### Int64Index & RangeIndex

> 当没有指定index类型，这个是默认的索引类型。这种索引查找速度非常高效，因为它使用的是连续内存的数组。RangeIndex是Int64Index的优化版本。节省了内存占用。

```python
f1 = pd.DataFrame(np.arange(10, 20), index=np.arange(10, 20))
print(f1.index)
----
Int64Index([10, 11, 12, 13, 14, 15, 16, 17, 18, 19], dtype='int64')
```

如果不指定`index`则使用`rangeindex`

```python
f1 = pd.DataFrame(np.arange(10, 20))
print(f1.index)
----
RangeIndex(start=0, stop=10, step=1)
```

### Float64Index

```python
f1 = pd.DataFrame(np.arange(10, 20), index=np.arange(0.0, 10.0))
print(f1.index)
----
Float64Index([0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0], dtype='float64')
```

### IntervalIndex

```python
f1 = pd.DataFrame(np.arange(10, 15), index=pd.IntervalIndex.from_breaks(range(6)))
print(f1)
print(f1.index)
----
         0
(0, 1]  10
(1, 2]  11
(2, 3]  12
(3, 4]  13
(4, 5]  14
IntervalIndex([(0, 1], (1, 2], (2, 3], (3, 4], (4, 5]],
              closed='right',
              dtype='interval[int64]')
```

### CategoricalIndex

### DatetimeIndex

```python
d1 = pd.date_range(start="2020-01-01", periods=5, freq="d")
f1 = pd.DataFrame(np.random.randint(20, 50, 5), index=d1)
print(f1)
----
             0
2020-01-01  38
2020-01-02  46
2020-01-03  48
2020-01-04  28
2020-01-05  37
```

### PeriodIndex

```python
d1 = pd.PeriodIndex(["2020-01-01", "2020-02-01"], freq="h")
print(d1)
----
PeriodIndex(['2020-01-01 00:00', '2020-02-01 00:00'], dtype='period[H]', freq='H')
```

## MultiIndex

```python
f1 = pd.DataFrame({
    "name": ["张三", "李四", "王五", "张三"],
    "age": [22, 24, 25, 34],
    "score": [92, 87, 76, 97]
})
f2 = f1.set_index(["name", "age"])
print(f2)
print(f2.index)
----
          score
name age       
张三   22      92
李四   24      87
王五   25      76
张三   34      97
MultiIndex([('张三', 22),
            ('李四', 24),
            ('王五', 25),
            ('张三', 34)],
           names=['name', 'age'])
```



## 使用索引选取值

使用 `loc[]`, `iloc[]` , `[]`, `at[]` 获取

### 索引间移动数据

`DataFrame.reset_index()`重置索引，将原先的索引作为普通的列。

```python
f1 = pd.DataFrame({"name": ["张三", "李四", "王五"], "age": [22, 24, 25]})
index_moved_to_col = f1.reset_index()
print(index_moved_to_col)
----
   index name  age
0      0   张三   22
1      1   李四   24
2      2   王五   25
----
f1 = pd.DataFrame({"name": ["张三", "李四", "王五"], "age": [22, 24, 25], "score": [92, 87, 76]})
f2 = f1.set_index("name")
index_moved_to_col = f2.reset_index()
new_f = index_moved_to_col.reindex(index=[0],columns=["name"])
----
  name
0   张三
```

> 可以通过 `get_level_values(0)` `levels[0]` 来获取每个level的Index对象。

### 选择行

```python
print(f2.xs("张三", level=0)) #第一索引，名字叫张三的
print(f2.xs(24, level=1)) #搜索第二索引，age是24的
----
     score
age       
22      92
24      97
      score
name       
李四       87
张三       97
```

可以链式选择

```python
print(f2.xs("张三").xs(24))
----
score    97
Name: 24, dtype: int64
```

> 如果，第一个`xs("张三")`返回的不再是`multiindex`的话，就不能继续用`xs(value, level)`的参数列表了。