---
标题: python函数
---
<!-- TOC -->

- [1. 函数](#1-函数)
    - [1.1. 参数](#11-参数)
        - [1.1.1. 位置参数](#111-位置参数)
        - [1.1.2. 关键字参数](#112-关键字参数)
        - [1.1.3. 默认值](#113-默认值)
        - [1.1.4. 星号参数](#114-星号参数)
        - [1.1.5. 关键字参数的传递-处理可变参数](#115-关键字参数的传递-处理可变参数)
        - [1.1.6. 多种传参方式结合](#116-多种传参方式结合)
        - [1.1.7. 星号的作用](#117-星号的作用)
    - [1.2. 柯里化函数调用](#12-柯里化函数调用)
    - [1.3. 函数的自身特征查询](#13-函数的自身特征查询)
        - [1.3.1. inspect模块](#131-inspect模块)
- [装饰器](#装饰器)
    - [用处](#用处)
    - [闭包closures的概念](#闭包closures的概念)

<!-- /TOC -->


# 1. 函数
python中的函数作为数据对象可以作为参数传递，有各种定义方式和使用场景

## 1.1. 参数
### 1.1.1. 位置参数
### 1.1.2. 关键字参数
### 1.1.3. 默认值
```python
def add_prefix(mystr, prefix="xiefq_"):
    return prefix + mystr
add_prefix("你好")
```

### 1.1.4. 星号参数
星号定义的参数，可以接收1+个参数，它会自动封装成tupple
```python
def add_to_cart(*item):
  for itm in item:
    print(itm)

# 传参方式
add_to_cart(item1)
add_to_card(item1, item2)
add_to_cart(*["nihao", " ",  "nichicao", 1]) #数组需要在前面加一个星号
```

### 1.1.5. 关键字参数的传递-处理可变参数
将关键字参数自动封装为字典
```python
def fuc_call(**args):
  print(args)

fuc_call(name="xiefq", age=22)
```

<mark>位置参数自动封装成tuple是不可变的， 关键字参数封装成字典是可变的</mark>

### 1.1.6. 多种传参方式结合
规则和参数列表出现的顺序如下
> 必须参数，可选参数，位置参数，关键字参数  
典型的函数签名公式：def create_element(name, editable=True, *children, **attributes)

如果采用关键字传参方式，也可以忽略这个顺序，即，任何出现在可变参数后边的参数，必须以关键字的方式传递，否则的话要严格遵顼参数顺序规则。

如果要将函数声明为必须关键字参数，定义如下

```python
def keyword_func(*, name, age):
    print(name)
keyword_func("wocao", 22) #错误调用
keyword_func(name="wocao", age=22) #正确调用
```
但是这种方式尽量少用

### 1.1.7. 星号的作用
+ 一个星号用于位置参数
+ 二个星号用户关键字参数

## 1.2. 柯里化函数调用
就是将一个多参数的函数变成少参数的函数。每调用一次返回一个更少参数的函数，直到所有的参数满足后，返回结果  
标准functools里提供了partial()函数，可以将一个函数充分分解成多个函数
```python
import functools, os

def load_file(file, base_path='/', mode='rb'):
  pass

load_readonly = functools.partial(load_file)
load_writable = functools.partial(load_file, mode='w')

load_readonly("1.txt")
load_writable("2.txt")
```
这种方式也叫装饰模式

## 1.3. 函数的自身特征查询
> 在Java中，类比于Java的反射，比如查询函数的签名，参数类型，注释等

### 1.3.1. inspect模块
+ getfullargspec()函数
> 获取函数的参数列表，返回一个tuple，tuple的值如下
  + args 显示声明参数名称
  + varargs: 可变位置参数
  + varkv: 可变关键字参数
  + defualts: 显示什么的参数的默认值
  + kwonlyargs: 关键字-only的参数名称
  + kwonlydefaults: 关键字-only的参数默认值
  + annotations: 注解

例如：  
```python
def example(p1, p2="p2", p3, *vps, ko1, ko2="ko2", **kw):
  pass

import inspect
inspect.getfullargspec(example)
```
返回结果
```python
FullArgSpec(args=['p1', 'p2'], varargs='vps', varkw='kw', defaults=('p2',), kwonlyargs=['ko1', 'ko2'], kwonlydefaults={'ko2': 'ko2'}, annotations={})
```

# 装饰器
> 接受一个函数，然后返回另一个带有装饰功能的函数

+ 装饰器只能在源码级别上使用，如果要使用别人的函数，需要手动将别人的函数传入装饰器里

## 用处
+ 访问控制
+ 清除临时资源
+ 错误处理
+ 缓存
+ 日志

## 闭包closures的概念

