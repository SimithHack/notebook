---
title: k8s-应用版本更新
category: faq
---

<!-- TOC -->

- [0.1. k8s-应用版本更新](#01-k8s-应用版本更新)
    - [0.1.1. 停止有bug版本的应用更新](#011-停止有bug版本的应用更新)
    - [0.1.2. 使用kubectl apply 命令更新探针逻辑](#012-使用kubectl-apply-命令更新探针逻辑)
- [0.2. 给rollout配置deadline](#02-给rollout配置deadline)

<!-- /TOC -->
## 0.1. k8s-应用版本更新

### 0.1.1. 停止有bug版本的应用更新
minReadySeconds用以调整老版本滚出的时间，其实这个参数的真正作用是阻止发布功能有缺陷的应用程序版本，而并不是
闹着玩的减慢滚出时间。

minReadySeconds属性控制一个新创建的pod，至少要等待多长时间才会被认作可用。因为有maxUnavailble的控制，新创建
的节点没有被变为可用之前，老版本的节点是不会滚出的。如果在minReadySeconds时间内，readiness探针检测到函数功能
不正常，就会阻止此节点变为可用。

所以 readiness探针的实现逻辑 很重要

### 0.1.2. 使用kubectl apply 命令更新探针逻辑
```yml
apiVersion: apps/v1beta1
kind: Deployment
metadata:
  name: kubia
spec:
  replicas: 3
  minReadySeconds: 10
  strategy:
    rollingUpdate:
      maxSurge: 1
      maxUnavailble: 0 #一个一个更新
    type: rollingUpdate
  template:
    metadata:
      name: kubia
      lables:
        app: kubia
    spec:
      containers:
      - image: luksa/kubia:v3
      name: nodejs
      readinessProbe: # 定义探针
        periodSeconds: 1
        minReadySeconds: 10 # 如果不配置此属性，可能pod刚开始是返回success的，但是后来就崩溃了，这将导致错误功能的程序上线
        httpGet:
          path: /
          port: 8080
```

然后使用kubectl apply -f xxx.yaml进行更新

可以通过kubectl rollout status deployment name 来查看状态

## 0.2. 给rollout配置deadline
