[TOC]

# 环境搭建

![image-20200302215216077](D:\code\notebook\tech\learn\编程\go\go_1.assets\image-20200302215216077.png)

## 编译

> 使用 go build 需要在命令窗口

1 `go build` 默认项目名称

2 `go build -o myname.exe` 生成自己的名字

3 `go install`  编译+发布到bin目录

## 交叉编译

> 在windows平台上编译出其他平台的执行程序

```shell
# 指定目标系统平台和处理器架构就可以了
set CGO_ENABLED=0 #禁用CGO
set GOOS=linux # 设置目标平台为linux
set GOARCH=amd64 # 目标处理器架构
```

# go基础语法

main函数是程序入口函数，函数的外边只能放变量的声明 `变量` `常量` `函数` `类型`

## 变量和常量

* 局部变量声明了必须要使用，不然要报错

  ```go
  package main
  
  import "fmt"
  
  var (
  	name string
  	age  int
  	isOk bool
  )
  
  func main() {
  	fmt.Println("Hello World!")
  	name = "xiefq"
  	age = 16
  	isOk = true
  	fmt.Printf("name:%s age:%d", name, age)
  	//标准局部变量声明
  	var address string
  	address = "你好"
  	println(address)
  	//简短变量声明
  	s3 := "你好"
  	fmt.Println(s3)
  	//类型推导
  	var s4 = 1
  	print(s4)
  	//声明并赋值
  	var s5 string = "xiefq"
  	println(s5)
  	//匿名变量，忽略某个值，也叫哑元变量
  	age, _ := foo()
  	println(age)
  
  }
  func foo() (int, string) {
  	return 10, "nihao"
  }
  
  ```

  * 函数外的每个语句都必须以关键字开始
  * `:=`不能使用在函数外
  * `_`多用于占位，表示忽略值

* 常量

  ```go
  package main
  
  const pi = 3.14
  
  //批量声明
  const (
  	notFound = 404
  	success  = 200
  )
  
  //批量声明常量，如果某一行没有赋值，默认和上一行的值一样
  const (
  	n1 = 100
  	n2
  	n3 = 200
  	n4
  )
  
  func main() {
  	println(n1)
  	println(n2)
  }
  
  ```

  * iota 

    > 它是go语言的常量计数器，只能在常量表达式中使用
    >
    > 在const关键字出现时被重置为0， 每新增一行常量声明将使iota计数一次，可理解为常量语句块的`行计数器`类似于枚举

  ```go
  const (
  	a1 = iota
  	a2
  	a3 = iota
  )
  //插队
  const (
  	c1 = iota //0
  	c2 = 100 // 100
  	c3 = iota // 2
  )
  const (
  	d1, d2 = iota, iota // 0 0
  
  	d3 = iota // 1
  
  	d4 = 5    // 5
  	d5 = iota // 3
  )
  //定义数量级
  const (
  	_  = iota
  	KB = 1 << (10 * iota) // 1左移10位 
  	MB = 1 << (10 * iota)
  	GB = 1 << (10 * iota)
  )
  ```


# 数据类型

## 整型

> 以下在写跨平台的程序的时候要注意

`uint`在32位操作系统上是`int32`，在64位操作系统上是`uint64`

`int`跟`uint`一样

`uintptr`无符号整型，用于存放一个指针

## 八进制和十进制，十六进制

> go语言无法直接定义二进制数

```go
func main() {
	var i1 = 101 //十进制数
	fmt.Println("%d\n", i1)
	var i2 = 077 //八进制
	fmt.Println("%d\n", i2)
	var i3 = 0xaaa //十六进制
	fmt.Println("%d\n", i3)
}
```

## 浮点数

```go
func main() {
	f1 := 1.2 //float64
	fmt.Printf("%T\n", f1)
	var f2 float32 = 1.2
	fmt.Printf(f1 == f2)
}
```

> 以上，f1和f2的类型不同，无法直接比较

![image-20200303214408552](D:\code\notebook\tech\learn\编程\go\go_1.assets\image-20200303214408552.png)

## 复数

`complex64`和`complex128`

`complex64`实部和虚部都是32位，`complex128`实部和虚部都是64位

```go
func main() {
	var c1 complex64 = 1 + 2i
	fmt.Println(c1)
}
```

## 布尔值

> `bool`型无法和整数进行相互转换

```go
func main() {
	b1 := true
	var b2 bool = false
	fmt.Printf("%v", b1)
	fmt.Printf("%v", b2)
}
```

## `fmt.Printf`格式化类型

+ %T 类型
+ %s 字符串
+ %v 值
+ %b 二进制
+ %o 十进制
+ %x 十六进制
+ %d 数字
+ %#v 真实值
+ %p 内存地址

## 字符串

> 双引号是字符串，单引号是字符，分为`byte`和`rune`类型，`rune`类型是utf-8类型

多行字符串

```go
func main() {
	s1 := `
		Are you ok ?
		I'm mi fan
	`
	print(s1)
}
```

## 字符串相关操作

```go
print(s1)
print(len(s1))
print("wokao" + s1)
println(fmt.Sprintf("%s%s", "你好", "吗"))
s2 := strings.Split(s1, "\\s")
fmt.Printf("%T\n", s2)
fmt.Print(len(s2))
s3 := strings.Join(s2, "+")
fmt.Println(s3)
s4 := "ni好"
s5 := fmt.Sprintf("%s的类型是%T", s4, s4)
fmt.Println(s5)
```

* for循环

```go
s1 := "ni好吗"
for _, c := range s1 {
    fmt.Printf("%c\n", c)
}
```

* 把字符串强制转换为rune切片

 ```go
s1 := "ni好吗"
s2 := []rune(s1)
s2[0] = '红'
fmt.Println(string(s2))
 ```

## if for switch case

```go
//如果age只在if里用
if age := 18; age > 15 {
    fmt.Println(age)
} else {
    fmt.Println("less than 15")
}
//for
for i := 0; i < 10; i++ {
    fmt.Println(i)
}
//外边声明
i := 1
for ; i < 10; i++ {
    fmt.Print(i)
}
//for range 遍历列表，数组等
s := "你好啊，中国"
for idx, c := range s {
    fmt.Printf("%d=%c\n", idx, c)
}
//switch case
n := 2
switch n {
    case 1:
    {
        fmt.Println(n)
    }
    case 2:
    fmt.Println(n)
}
//go to
func main() {

	for i := 1; i < 10; i++ {
		for j := 1; j < 10; j++ {
			if j == 2 {
				goto outer
			}
		}
	}
outer:
	fmt.Println("goto")
}
```

##  复合数据类型

### 数组

```go
func main() {
	var a1 [3]bool
	fmt.Println(a1)

	var a2 [3]string
	fmt.Println(a2)

	//数组的初始化
	a3 := [2]int{0, 1}
	fmt.Println(a3)

	a4 := [...]int{1, 2, 4}
	fmt.Println(a4)
    
    //根据索引来初始化
	a5 := [5]int{0: 1, 3: 3}
	fmt.Println(a5) //[1 0 0 3 0]
    
    //多维数组
	a6 := [2][2]int{
		[2]int{1, 2},
	}
	fmt.Println(a6)
}
```

数组是值类型的，赋值的时候是全部copy

```go
a7 := a6
a7[1] = [2]int{3, 4}
fmt.Println(a6, a7)
// [[1 2] [0 0]] [[1 2] [3 4]]
```

### 数组切片

```go
func main() {
	var s1 []int
	fmt.Println(s1 == nil)

	s1 = []int{1, 2}
	fmt.Println(s1, len(s1), cap(s1))

	s2 := []int{3, 4, 5, 6}

	s1 = s2[0:1]
	fmt.Println(s1, len(s1), cap(s1))

	s1 = s2[:]
	fmt.Println(s1, len(s1), cap(s1))

	s1 = s2[:2]
	fmt.Println(s1, len(s1), cap(s1))
}

```

* 切片是引用类型，长度是切片内容的长度，容量是底层数组的容量

* 切片不能直接比较

* 切片的复制是引用拷贝

  ```go
  func main() {
  	s1 := make([]int, 5, 10) // 类型，长度，容量
  	fmt.Println(s1, cap(s1), len(s1))
  	s2 := s1
  	s1[0] = 2
  	s2[0] = 3
  	//输出 一样的
  	fmt.Println(s1, "\n", s2)
  }
  ```

#### 使用make函数创建切片

```go
func main() {
	s1 := make([]int, 5, 10) // 类型，长度，容量
	fmt.Println(s1, cap(s1), len(s1))
}
```

#### 切片添加

```go
func main() {
	s1 := make([]int, 5, 5) // 类型，长度，容量
	s1 = append(s1, 8)
	fmt.Println(s1, cap(s1), len(s1))
}
```

* append函数返回一个新的函数，可能发生扩容。当容量超出的时候。扩容的时候容量翻倍。

还可以添加另一个同类型的数组

```go
s2 := []int{10, 10}
s1 = append(s1, s2...)
fmt.Println(s1, cap(s1), len(s1))
```

```bash
[0 0 0 0 0 8] 10 6
[0 0 0 0 0 8 10 10] 10 8
```

* 注意 `...` 是拆开原数组的意思

#### 切片拷贝

```go
func main() {
	s1 := make([]int, 5, 5) // 类型，长度，容量
	s1 = append(s1, 8)
	s2 := make([]int, 2, 4)
	copy(s2, s1) //把s1 拷贝到 s2, 变成两个不同的数组
	s2[0] = 100
	fmt.Println(s1, s2)
}
```

```bash
[0 0 0 0 0 8] [100 0]
```

* 可以利用切片append进行删除

```go
s1 := make([]int, 5, 5) // 类型，长度，容量
fmt.Println(s1)
//替换
s1 = append(s1[0:0], []int{1, 2, 3, 4, 5}...)
fmt.Println(s1)
//append函数进行删除2-3
s1 = append(s1[:1], s1[3:]...)
fmt.Println(s1)
```

```bash
[0 0 0 0 0]
[1 2 3 4 5]
[1 4 5]
```

## 取地址，指针

go中没有指针操作, `&` 取地址

```go
func main() {
	n := 9
	p := &n
	fmt.Println(n)
	fmt.Println(p)
	fmt.Println(*p)
}
```

```bash
9
0xc0000100a0
9
```

## make 和 new

> 主要用来分配内存

```go
func main() {
	var a *int //int类型的变量，没有初始化就是nil空指针
	*a = 100 //出错
	fmt.Println(*a)
}
```

* 以上是a是int类型的指针，但是没有初始化

```go
func main() {
	var a = new(int)
	fmt.Println(a)
	*a = 100
	fmt.Println(*a)
}
```

```bash
0xc0000a0068
100
```

* make用于map, slice, chan内存的创建，但是它返回的不是指针

> make的签名 `make(Type, len, cap)`

* new用于给基础数据类型申请内存空间，返回的是指针

## map

```go
func main() {
	var m1 map[string]int //空指针
	m1 = make(map[string]int, 10)
	m1["score"] = 22
	m1["age"] = 12
	fmt.Println(m1)
}
```

```bash
map[age:12 score:22]
```

### map的操作

1. 访问

```go
value, isExists := m1["score"]
if isExists {
    fmt.Println(value)
}
```

```bash
22
```

* 如果不存在，返回的是对于类型的 `零值`

2. 遍历

```go
for k, v := range m1 {
    fmt.Println(k, v)
}
```

3. 删除

```go
delete(m1, "score")
```

* 如果删除一个不存在的key，就没有任何操作

4. 提取key

```go
var keys = make([]string, 0, len(m1))
for k := range m1 {
    keys = append(keys, k)
}
fmt.Println(keys)
```

## 函数

```go
func main() {
	fmt.Println(sum(2, 3))
	fmt.Println(sum2(2, 3))
	fmt.Println(f2())
	fmt.Println(f3())
	f4("age", 11, 23, 44)
}

func sum(x int, y int) int {
	return x + y
}

//返回值名称预定义
func sum2(x int, y int) (sum int) {
	sum = x + y
	return
}

//返回两个参数
func f2() (name string, age int) {
	name = "李玲"
	age = 22
	return
}

//参数类型一样的
func f3() (name, address string, age, score int) {
	address = "无锡"
	name = "小菜"
	age = 22
	score = 99
	return
}

//可变参数, y就是一个切片，可变参数必须放在所有参数的后边
func f4(x string, y ...int) {
	fmt.Println(x, y)
}
//使用defer可以将语句延迟到函数返回的时候才执行
func f2() {
	fmt.Println("1")
	defer fmt.Println("2")
	fmt.Println("3")
	fmt.Println("4")
}
```

### 判断回文

```go
s1 := "上海自来水来自海上"
s2 := make([]rune, 0, 10)
for _, c := range s1 {
    s2 = append(s2, c)
}
lenth := len(s2)
fmt.Println(lenth)
for i := 0; i < lenth/2; i++ {
    j := lenth - i - 1
    fmt.Printf("%v\n", s2[i])
    if s2[i] != s2[j] {
        fmt.Printf("%s不是回文, %v!=%v", s1, s2[i], s2[j])
        break
    }
}
```

### defer用法

```go
//返回5
func f1() int {
	x := 5
	defer func() {
		x++ //修改的是x的值
	}()
	return x
}

//返回6
func f2() (x int) {
	defer func() {
		x++
	}()
	return 5 //第一步返回值RET=x=5, 第二步执行defer，defer修改的是x,x+1变为6，RET也变为6
}

//返回6
func f3() int {
	x := 5
	defer func() {
		x++
	}()
	return x //第一步返回值赋值，返回的是RET，RET=x=5,，第二部执行defer, 修改的是x
}
```

## 函数类型与变量

* 函数也是一种类型，可以当参数变量传递

```go
func main() {
	a := f1
	fmt.Printf("%T\n", a)
	fmt.Println(a())
}

//返回5
func f1() int {
	x := 5
	defer func() {
		x++ //修改的是x的值
	}()
	return x
}
```

```bash
func() int
5
```

* 当参数传递

```go
func main() {
	a := sum
	fmt.Printf("%T\n", a)
	fmt.Println(sum_sum(sum, 2))
}

//返回5
func sum(x, y int) int {
	return x + y
}

func sum_sum(f func(x, y int) int, a int) int {
	return f(1, a) + a
}
```

* 当返回值

```go
func main() {
	ten := power(10)
	ba := power(8)
	fmt.Println(ten(2)) //20
	fmt.Println(ba(2))  //16
}

func power(base int) func(b int) int {
	return func(b int) int {
		return base * b
	}
}
```

## 匿名函数

> 只能定义在函数内部，方法块内部

```go
f1:=func(a int) int {
    
}
```

## 闭包

> 闭包是一个函数，这个函数包含了它外部作用域的一个变量. `闭包=函数+引用环境`
>

```go
func main() {
	p2 := wrap(2)
	fmt.Println(p2(10))
	p10 := wrap(10)
	fmt.Println(p10(10))
}
func wrap(base int) func(int) int {
	return func(x int) int {
		return base + x
	}
}
```

