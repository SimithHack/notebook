---
标题: DataFrame和date_range的相关用法
日期: 2019-07-01
---

<!-- TOC -->

- [1. dataframe的相关操作和方法](#1-dataframe的相关操作和方法)
    - [1.1. 字典构造DataFrame](#11-字典构造dataframe)
    - [1.2. Series构造DataFrame](#12-series构造dataframe)
    - [1.3. 二维数组构造DataFrame](#13-二维数组构造dataframe)
    - [1.4. DataFrame的数据处理](#14-dataframe的数据处理)
- [2. datetime相关操作](#2-datetime相关操作)
    - [2.1. 基本用法](#21-基本用法)
    - [2.2. 时间格式处理](#22-时间格式处理)
    - [2.3. pandas里时间戳的用法](#23-pandas里时间戳的用法)
- [3. pandas日期范围生成](#3-pandas日期范围生成)
    - [3.1. 使用时间作为索引](#31-使用时间作为索引)
    - [3.2. 生成DatetimeIndex](#32-生成datetimeindex)
    - [3.3. freq的用法](#33-freq的用法)
    - [3.4. 其他操作](#34-其他操作)

<!-- /TOC -->

# 1. dataframe的相关操作和方法
## 1.1. 字典构造DataFrame
```python
import pandas as pd
import numpy as np

def func1():
    """
    字典构造DataFrame
    :return:
    """
    data = {
        "name": ["张艺谋", "宋祖英", "李宇春", "宋小宝"],
        "age": [12, 32, 15, 88],
        "gender": ["男", "女", "未知", "男"]
    }
    frame = pd.DataFrame(data)
    print(frame)
    # 过滤列（字段）
    print(frame["age"])
    # 过滤多个字段
    print(frame[["age", "gender"]])
    # 行切片 左闭右开
    print(frame[0:1])
    # 自定义列的顺序
    frame2 = pd.DataFrame(data, columns=["name", "gender", "age"])
    print(frame2)
    # 或者只显示某些列
    frame3 = pd.DataFrame(data, columns=["name", "age"])
    print(frame3)
    # 自定义index
    frame4 = pd.DataFrame(data, index=list("abcd"))
    print(frame4)
```

## 1.2. Series构造DataFrame
```python
def func2():
    """
    Series构造
        字典的key作为列标签，Series Index名为行标，注意两个序列相同的Index名才会组合在一起
    :return:
    """
    data = {
        "math": pd.Series(np.random.randint(0, 100, size=2), index=["张三", "李四"]),
        "english": pd.Series(np.random.randint(0, 100, size=3), index=["张三", "李四", "王五"])
    }
    frame = pd.DataFrame(data)
    print(frame)
```

## 1.3. 二维数组构造DataFrame
```python
def func3():
    """
    二维数组创建
    :return:
    """
    data = np.random.randint(40, 100, 9).reshape(3, 3)
    frame = pd.DataFrame(data, index=["张三", "李四", "王五"], columns=["语文", "数学", "英语"])
    print(frame)
    # 使用行名选择行 返回的是一个Series，index同原frame
    zs_score = frame.loc["张三"]
    print(zs_score, type(zs_score))
    # 选择多行，返回DataFrame
    print(frame.loc[["张三", "王五"]])
    # 标签切分，是左闭右闭，loc只能使用index来进行切分
    print(frame.loc["张三":"王五"])
    # 使用数字索引来切分，比如切分出0,1是左闭右开
    print(frame.iloc[0:2])
    # 多值选择
    print(frame.iloc[[2, 1, 0]])
    # 过滤 语文80分的人
    print(frame[frame["语文"] > 80])
    # DataFrame可以通过一个相同阶的bool矩阵进行过滤
    bool_array = (np.random.randint(0, 2, 9) == 1).reshape(3, 3)
    print(frame[bool_array])
    # 切片，每次隔2个
    print(frame.iloc[::2])
```

## 1.4. DataFrame的数据处理
```python
def func4():
    """
    数据的处理
    :return:
    """
    data = np.random.randint(40, 100, 9).reshape(3, 3)
    frame = pd.DataFrame(data, index=["张三", "李四", "王五"], columns=["语文", "数学", "英语"])
    # 头两行
    print(frame.head(2))
    # 最后1行
    print(frame.tail(1))
    # 转置
    print(frame.T)
    # 添加列
    frame["历史"] = np.random.randint(40, 100, 3)
    print(frame)
    # 添加一行
    frame.loc["赵柳"] = np.random.randint(40, 100, 4)
    print(frame)
    # 修改某列
    frame["语文"] = np.random.randint(90, 100, 4)
    print(frame)
    # 删除列
    del frame["数学"]
    print(frame)
    # 删除行
    frame.drop(["张三"], inplace=True)
    print(frame)
    # 删除列另一种方式
    frame.drop("英语", inplace=True, axis=1)
    print(frame)
    # 对齐相加
    data2 = pd.DataFrame(np.random.randint(90, 100, 9).reshape(3, 3), columns=["语文", "历史", "数学"], index=["李四", "王五", "赵柳"])
    print(data2+frame)
    # 按照列（值）排序
    print(frame.sort_values("语文", ascending=True))
    # 按照键值排序
    print(frame.sort_index())
```

# 2. datetime相关操作
## 2.1. 基本用法
```python
import datetime
from dateutil.parser import parse

def func1():
    # 当前日期
    today = datetime.date.today()
    print(today, type(today))
    # 当前时间
    nowtime = datetime.datetime.now()
    print(nowtime, type(nowtime))
    # 日期构造
    print(datetime.date(2019, 1, 29))
    # 获取年，月，日
    print("年{}月{}日{}".format(*[today.year, today.month, today.day]))
    # 日期相加
    d1 = datetime.date(2019, 1, 1)
    delta = datetime.timedelta(days=2, hours=2) # 因为d1是日期，所以小时数是反映不出来的
    d2 = d1+delta
    print(d2, type(d2))
    # 时间相加
    dt1 = datetime.datetime(2019, 1, 1, 0, 0, 0, 0)
    print(dt1, type(dt1))
    dt2 = dt1 + delta
    print(dt2)
```

## 2.2. 时间格式处理
```python
def func2():
    """
    时间格式处理
    :return:
    """
    dstr1 = "20190102112233"
    d1 = parse(dstr1)
    print(d1, type(d1))
    # 启用fuzzy参数可以尽最大努力转换
    dstr2 = "2010年，12月，22日, 22:22:22"
    print(parse(dstr2, fuzzy=True))
    # 时间格式化
    d2 = datetime.datetime.now()
    print(d2.strftime("%Y-%m-%d %H:%M:%S"))
```

## 2.3. pandas里时间戳的用法
```python
def func3():
    """
    panda里时间戳的用法
    :return:
    """
    d1 = "20190123112345"
    d2 = datetime.datetime.now()
    # panda中时间戳的构建方法
    pd1 = pd.Timestamp(d1)
    pd2 = pd.Timestamp(d2)
    print(pd1, type(pd1))
    print(pd2, type(pd2))
    # 数组的转换成DatetimeIndex类型
    darray = [d1, d2]
    pdarray = pd.to_datetime(darray)
    print(pdarray, type(pdarray))
    # 如果有不可转换的，可以加参数进行调整，errors默认是抛出异常 raise
    # errors="ignore"表示转换错误原格式输出
    da1 = ["20170101", "张三", "2019-12-22"]
    pda1 = pd.to_datetime(da1, errors="ignore")
    print(pda1)
    # errors="coerce"表示转换错误时，输出NaT
    pda2 = pd.to_datetime(da1, errors="coerce")
    print(pda2)
```

# 3. pandas日期范围生成
## 3.1. 使用时间作为索引
```python
def func1():
    """
    使用时间作为索引
    :return:
    """
    rng = pd.DatetimeIndex(["20120101", "20190903", "20170304"])
    tmp1 = pd.Series(np.random.randint(50, 100, len(rng)), index=rng)
    print(tmp1)
```

## 3.2. 生成DatetimeIndex
```python
def func2():
    """
    生成DatetimeIndex
    :return:
    """
    # 指定开始日期和结束日期，默认按天递增
    # periods控制生成size
    rng1 = pd.date_range("20120101", "20121201", periods=2)
    print(rng1)
    # 指定name, normalize将开始和结束时间规范化处理，比如输入的开始范围是2019-11-22 12:33:56加了normalize参数后，变为2019-11-22 00:00:00
    rng2 = pd.date_range("20120101", "20121201", periods=10, name="xiefq", normalize=True)
    print(rng2)
    # 控制开闭性, None默认，左闭右闭，left左闭右开
    rng3 = pd.date_range("20120101", "20121201", periods=10, closed="left")
    print(rng3)
    # 工作日
    rng4 = pd.bdate_range(start="20120101", end="20121201")
    print(rng4)
```

## 3.3. freq的用法
```python
def func3():
    """
    freq的用法
    :return:
    """
    # 工作日
    rng1 = pd.date_range("20120101", "20120120", freq="B")
    print(rng1)
    # H每小时,T每分钟，S没秒，L每毫秒，U每微妙
    rng2 = pd.date_range("20120101", "20120120", freq="H")
    print(rng2)
    # 从指定模式开始 W表示以周为单位取周几 MON周一，TUE周二...
    rng3 = pd.date_range("20120101", "20120120", freq="W-MON")
    print(rng3)
    # 从指定模式开始 Q表示季度为周期，最后一个月是第几月 DEC 12月，JAN 1月...的最后一天
    # A 表示以年为周期
    rng4 = pd.date_range("20120101", "20130420", freq="Q-DEC")
    print(rng4)
    # B代表工作日，表示每个季度月的最后一个工作日
    rng5 = pd.date_range("20120101", "20130420", freq="BQ-DEC")
    print(rng5)
    # M代表每月的最后一天，Q-月表示每个季度末最后月的第一个日历日
    rng6 = pd.date_range("2012", "2013", freq="M")
    print(rng6)
```

## 3.4. 其他操作
```python
def func4():
    """
    其他操作
    :return:
    """
    prices = pd.Series(np.random.randint(60, 70, 12), index=pd.date_range("2012", "2013", freq="M"))
    print(prices)
    # 数值后移，其他用NaN填充
    print(prices.shift(2))
    # 对时间戳进行移位，比如，后移2天
    print(prices.shift(2, freq="D"))
    # 前移1分钟
    print(prices.shift(-1, freq="T"))
    # 改变频率 method指定空值的插值模式，ffill表示用前面的值插入，bfill表示用后边的值插入
    print(prices.asfreq("20D", method="ffill"))
    # 混合频率 比如2小时30分钟可以写成 2h30min
    print(prices.asfreq("20D1h30min", method="bfill"))
```