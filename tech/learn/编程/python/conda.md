# 虚拟环境
> 虚拟环境的目的是为了给不同的工程创建相互独立的运行环境。在虚拟环境下，每一个工程都有自己独立的依赖包，与其他的工程无关，而且，不同的虚拟环境下，相同的包也可以有不同的版本。虚拟环境的数量没有限制。可以使用conda轻松创建多个虚拟环境。

# anaconda自带的base虚拟环境
在用户目录下的Anaconda3目录下有一套完整的python解析器，并且jupter在启动时候也会使用这个base虚拟环境

# 查看虚拟环境的信息
+ 1 conda info --envs 
> 查看虚拟环境的环境变量

+ 2 conda deactivate 
> 就会退出当前虚拟环境

+ 3 conda activate base 
> 进入哪个虚拟环境

# 虚拟环境的目录
在用户目录下的Anaconda3的envs目录

+ 1 conda create -n name
> 创建一个虚拟环境

+ 2 conda list
> 可以查看当前环境下的包

+ 3 conda install pytorch torchvision cudatoolkit=10.0
> 虚拟环境下安装pytorch

# 让jupter自由切换虚拟环境
> 默认调用的是base虚拟环境

可以选择菜单 Kernel --> change kernel 来选择我们的虚拟环境

可以在base环境下输入以下命令也可以让jupter看到我们的虚拟环境
+ 1 安装nb_conda
```
conda install nb_conda
```

+ 2 安装ipykernel
```
conda install ipykernel
```

+ 3 为虚拟环境下创建kernel文件
```
conda install -n pytorch ipykernel
conda install -n tensorflow2 ipykernel
```