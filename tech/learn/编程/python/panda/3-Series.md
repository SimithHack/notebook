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



