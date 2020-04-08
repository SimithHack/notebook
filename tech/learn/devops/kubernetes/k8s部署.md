# k8s 安装部署
它所代理的所有 Pod 的 IP 地址，都会被绑定一个这样格式的 DNS 记录
```
<pod-name>.<svc-name>.<namespace>.svc.cluster.local
```

## StatefulSet的定义

```yml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: web
spec:
  serviceName: "nginx" # 告诉 StatefulSet 控制器，在执行控制循环（Control Loop）的时候，请使用 nginx 这个 Headless Service 来保证 Pod 的“可解析身份”
  replicas: 2
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.9.1
        ports:
        - containerPort: 80
          name: web
```
它创建出来的Pod, 编号都是从 0 开始累加，Pod 的创建，也是严格按照编号顺序进行的。前一个还没创建成功，后一个是不会创建的。

StatefulSet 就保证了 Pod 网络标识的稳定性。

Kubernetes 就成功地将 Pod 的拓扑状态（比如：哪个节点先启动，哪个节点后启动），按照 Pod 的“名字 + 编号”的方式固定了下来。

尽管 web-0.nginx 这条记录本身不会变，但它解析到的 Pod 的 IP 地址，并不是固定的。这就意味着，对于“有状态应用”实例的访问，你必须使用 DNS 记录或者 hostname 的方式，而绝不应该直接访问这些 Pod 的 IP 地址。


```sh
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
       http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
systemctl enable --now kubelet
```

+ 下载镜像
> 可以在下载之前通过 `kubeadm config images list` 查看依赖哪些镜像

```sh
docker pull wangjohn/kube-apiserver:v1.17.0 
docker pull wangjohn/kube-scheduler:v1.17.0 
docker pull gotok8s/kube-controller-manager:v1.17.0 
docker pull wangjohn/kube-proxy:v1.17.0 
docker pull thejosan20/pause:3.1 
docker pull azhu/etcd:3.4.3-0 
docker pull azhu/coredns:1.6.5 


docker tag wangjohn/kube-proxy:v1.17.0 k8s.gcr.io/kube-proxy:v1.17.0
docker tag wangjohn/kube-apiserver:v1.17.0 k8s.gcr.io/kube-apiserver:v1.17.0
docker tag wangjohn/kube-scheduler:v1.17.0 k8s.gcr.io/kube-scheduler:v1.17.0
docker tag gotok8s/kube-controller-manager:v1.17.0 k8s.gcr.io/kube-controller-manager:v1.17.0
docker tag thejosan20/pause:3.1 k8s.gcr.io/pause:3.1
docker tag azhu/etcd:3.4.3-0 k8s.gcr.io/etcd:3.4.3-0
docker tag azhu/coredns:1.6.5 k8s.gcr.io/coredns:1.6.5
```
+ 执行某些命令
```shell
sysctl net.bridge.bridge-nf-call-iptables=1
sysctl net.ipv4.ip_forward=1
sudo swapoff -a
```

+ 初始化
```
kubeadm init
```

+ 记录token
```token
kubeadm join 192.168.211.130:6443 --token 6cidxv.5sn1yyq2oujytskd \
    --discovery-token-ca-cert-hash sha256:135544e655e652c28ffbe3847d1fd83b5a5487a3a93a41a3fa6fa2253b0623e3
```

+ 首次运行
```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

+ 部署网络插件
```
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```
有可能镜像下载不下来，主要通过`kubectl describe pod -n kube-system xxx`查看具体的镜像名称，手动下载下来。

## 额外知识点
一旦某个节点被加上了一个 Taint，即被“打上了污点”，那么所有 Pod 就都不能在这个节点上运行.
```
$ kubectl taint nodes node1 foo=bar:NoSchedule
```
这个 Taint 只会在调度新 Pod 时产生作用，而不会影响已经在 node1 上运行的 Pod.

有个别的 Pod 声明自己能“容忍”这个“污点”，即声明了 Toleration. 就可以在节点上运行。
标记污点的方法:
```yml
apiVersion: v1
kind: Pod
...
spec:
  tolerations:
  - key: "foo"
    operator: "Equal"
    value: "bar"
    effect: "NoSchedule"
```
> 这个 Pod 能“容忍”所有键值对为 foo=bar 的 Taint（ operator: “Equal”，“等于”操作）

## 部署dashboard
+ 部署
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
```

+ 修改Service为NodePort

```bash
kubectl edit service kubernetes-dashboard -n kubernetes-dashboard
```

```yaml
apiVersion: v1
kind: Service
metadata:
  annotations:
    kubectl.kubernetes.io/last-applied-configuration: |
      {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"labels":{"k8s-app":"kubernetes-dashboard"},"name":"kubernetes-dashboard","namespace":"kubernetes-dashboard"},"spec":{"ports":[{"port":443,"targetPort":8443}],"selector":{"k8s-app":"kubernetes-dashboard"}}}
  creationTimestamp: "2020-03-10T01:34:45Z"
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
  resourceVersion: "33948"
  selfLink: /api/v1/namespaces/kubernetes-dashboard/services/kubernetes-dashboard
  uid: 8fdf61c6-7157-450d-982d-838f539e3826
spec:
  clusterIP: 10.103.212.74
  externalTrafficPolicy: Cluster
  ports:
  - nodePort: 32123
    port: 443
    protocol: TCP
    targetPort: 8443
  selector:
    k8s-app: kubernetes-dashboard
  sessionAffinity: None
  type: NodePort
status:
  loadBalancer: {}
```

* 重新制作证书

  因为dashboard默认证书直接过期，不可用，我们需要重新为它签发证书

  * 签发自签名根证书

    ```bash
    openssl genrsa -out root.key 2048
    ```

    ```bash
    openssl req -new -x509 -key root.key -out root.crt -days 3650 -subj "/C=CN/ST=SC/L=CD/O=BT/OU=RD/CN=BT"
    ```

  * 签发`dashboard` 证书

    ```bash
    openssl genrsa -out dashboard.key 2048
    ```

    ```bash
    openssl req -new -sha256 -key dashboard.key -out dashboard.csr -subj "/C=CN/ST=SC/L=CD/O=BT/OU=RD/CN=192.168.211.150"
    ```

    ```ini
    # 证书配置文件 dashboard.cnf
    extensions = btree
    [btree]
    keyUsage = digitalSignature
    extendedKeyUsage = clientAuth,serverAuth
    subjectKeyIdentifier = hash
    authorityKeyIdentifier = keyid,issuer
    subjectAltName = IP:192.168.211.150,IP:192.168.211.151,IP:192.168.211.152,IP:127.0.0.1,DNS:192.168.211.2,DNS:8.8.8.8
    ```

    ```bash
    openssl x509 -req -sha256 -days 3650 -in dashboard.csr -out dashboard.crt -CA root.crt -CAkey root.key -CAcreateserial -extfile dashboard.cnf
    ```

  * 挂载证书

    ```bash
    # 删除部署, 如果之前已经部署过
    kubectl delete -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
    ```

    ```bash
    # 创建 kubernetes-dashboard-certs
    kubectl create secret generic kubernetes-dashboard-certs --from-file="dashboard.crt,dashboard.key" -n kubernetes-dashboard
    ```

    ```bash
    # 重新部署 还需要修改NodePort
    kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml
    ```

+ 用户账号创建
[参考地址](https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md)

```yml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
```
```yml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user你
  namespace: kubernetes-dashboard
```
* 获取token

```
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
```
+ 证书生成
```
openssl 
```

+ 访问控制
```
1 获取可用的secret
kubectl -n kubernetes-dashboard get secret
NAME                               TYPE                                  DATA   AGE
default-token-slbp9                kubernetes.io/service-account-token   3      146m
kubernetes-dashboard-certs         Opaque                                0      146m
kubernetes-dashboard-csrf          Opaque                                1      146m
kubernetes-dashboard-key-holder    Opaque                                2      146m
kubernetes-dashboard-token-hbpk5   kubernetes.io/service-account-token   3      146m

2 获取token
kubectl -n kubernetes-dashboard describe secrets kubernetes-dashboard-token-hbpk5
.....
namespace:  20 bytes
token:      eyJhbGciOiJSUzI1NiIsImtpZCI6InFGSEJheWlMZGNGdGNpSGxUcTVadmZyYzB3NG5DVFNBTEJqeFhoMzVia1UifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi0yNHM0diIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjljMTFiNzc0LTJhN2UtNGM0MC05MTc4LTMxMDYzYTdkODVjZCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlcm5ldGVzLWRhc2hib2FyZDprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.CEnIS75bRWjx-lgJWEwblDROB7SnGBfgn8uHPbbwtOHQ7Fe860ArzvPKMgJ-PZkQJ28ioXr5-9zH8g33vDcrDscEuXOu_2n6ftmKysI0fAC1Bv6v99iJ3JfASIv8RCgUYmc4hWQYOva5xzDcEACny8tOWHiyunIglmHWRegmm5fIJGibcyZDcyvlzj9E3xWVHyqvapNo41zc8r5Z8FgKSTsjT97L0ja0Yr_fBwCQ9CkYVC4OMI3g8GZHg5AJ3Ko2ICXuOV8HpAWdJHwKIbjehtRRO6T634HmM9J1MV5XXe9IQUVcOmdbd65732__aagAAK07NrycqGiliGndyCg62g
.....

3 访问
http://192.168.211.130:8002/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```


## 扩展磁盘容量
```
df -h 查看当前分区使用情况；
fdisk /dev/sdb 对磁盘进行操作（新建分区及格式化）
n
p
回车 默认分区号；
回车 默认磁盘创建开始位置；
回车 默认磁盘创建结束位置；
t 设置分区类型
8e LVM类型
w 保存
重启
lvm 进入lvm管理
lvm>pvcreate /dev/sda3 创建逻辑卷
lvm>vgextend centos /dev/sda3 //将初始化过的分区加入到虚拟卷
lvm>vgdisplay -v
lvm>pvdisplay //查看卷容量
lvm>lvextend -L +79G /dev/mapper/centos-root 实际要比磁盘空间小

lvm>quit
xfs_growfs /dev/mapper/centos-root 扩展容量
df -h 进行查看扩容
```
# k8s中“编排”核心概念
Pod这种对象，对容器进行了进一步封装，向外提供许多可以控制容器内部状态和行为的API接口，
而，k8s就是利用这些API，进行编排功能实现。

+ 控制器
看一个Deployment这个基本控制器的定义
```yml
apiVersion: apps/v1 # 定义使用api版本
kind: Deployment
metadata:
    name: nginx-deployment
spec:
    selector:
        matchLabels:
            app: nginx # 定义出作用于哪些pod
    replicas: 2
#----------------上边是控制器本身的定义-----------------------------
#-------------下边是控制器所控制的对象的定义------------------------
    template:
        metadata:
            labels:
                app: nginx
        spec:
            containers: 
            - name: nginx
              image: nginx:latest
              ports:
              - containerPort: 80
```
由`kubernate-controller-manager`组件负责对这些控制器进行操作。

在`kubernetes/pkg/controller`目录下定义了许多基本的控制器
```
$ ls -d */              
deployment/   job/     podautoscale/     daemon/
...
```

+ 控制循环
> 不断的将实际状态调整为期望状态的过程

实际状态来源：
```
1) k8s集群的本身状态（心跳检测容器的汇报，节点状态。监控系统的监控数据），主动收集的某些信息。
```
期望状态：来源于用户提交的yml定义。

+ 面向API编程

所有API对象的`metadata`里有一个字段`ownerReference`保存当前这个API对象的拥有者信息。

# Deployment原理
它实现的一个重要功能“水平扩展和收缩”。滚动更新的实现依赖于k8s里的一个很重要的API对象（ReplicaSet）.

ReplicaSet的定义
```yml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: nginx-set
  labels:
    app: nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
```
它由`副本数目`定义和`Pod模板`定义两部分组成。`Deployment`对象实际操作的是`ReplicaSet`对象，而不是pod。

控制关系如图：

![](./imgs/02.png)

层层控制。`ReplicaSet`保证系统中`Pod`的个数永远等于指定的个数。`Deployment`只允许容器的`restartPolicy=Always`的原因：
只有在容器能保证自己始终是`Running`状态的前提下，`ReplicaSet`调整`Pod`个数才有意义。

Deployment就是通过控制ReplicaSet中Pod副本的个数来实现水平扩展/收缩的。

+ 手动扩展
```sh
kubectl scale deployment nginx-deployment --replicas=4
```

## 滚动更新
> 将一个集群中正在运行的多个 Pod 版本，交替地逐一升级的过程，就是“滚动更新”

```sh
# 后边添加--record 的作用是：记录下你每次操作所执行的命令，以方便后面查看。
kubectl create -f nginx-deployment.yaml --record
```

+ 实时查看Deployment对象的状态
```
kubectl rollout status deployment/nginx-deployment
```
Deployment 只是在 ReplicaSet 的基础上，添加了 UP-TO-DATE 这个跟版本有关的状态字段。

可以直接使用 kubectl edit 指令编辑 Etcd 里的 API 对象
```
$ kubectl edit deployment/nginx-deployment
```

保证服务的连续性，Deployment Controller 还会确保，在任何时间窗口内，只有指定比例的 Pod 处于离线状态。
默认都是 DESIRED 值的 25%. 

+ RollingUpdateStrategy
```yml
spec:
...
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
```
maxSurge 指定的是除了 DESIRED 数量之外，在一次“滚动”中，Deployment 控制器还可以创建多少个新 Pod；而 maxUnavailable 指的是，在一次“滚动”中，Deployment 控制器可以删除多少个旧 Pod。

Deployment 的控制器，实际上控制的是 ReplicaSet 的数目，以及每个 ReplicaSet 的属性。这样的多个 ReplicaSet 对象，Kubernetes 项目就实现了对多个“应用版本”的描述。

![](./imgs/03.png)

而每个ReplicaSet就代表了应用的每次升级版本，k8s可以通过历史版本进行回滚。`kubectl rollout undo` 命令，就能把整个 Deployment 回滚到上一个版本

kubectl set image的指令，直接修改 nginx-deployment 所使用的镜像。
```
kubectl set image deployment/nginx-deployment nginx=nginx:1.91
```

使用 `kubectl rollout history` 命令，查看每次 Deployment 变更对应的版本。创建这个 Deployment 的时候，指定了–record 参数.

```sh
# 看到每个版本对应的 Deployment 的 API 对象的细节
$ kubectl rollout history deployment/nginx-deployment --revision=2
# 指定恢复到某一版本
$ kubectl rollout undo deployment/nginx-deployment --to-revision=2
```

执行一条 `kubectl rollout pause` 指令, 使得我们对 Deployment 的多次更新操作，最后 只生成一个 ReplicaSet。再执行一条 `kubectl rollout resume` 指令，就可以把这个 Deployment“恢复”回来

+ 控制这些“历史”ReplicaSet 的数量
spec.revisionHistoryLimit

# StatefulSet 有状态Pod
> StatefulSet 这个控制器的主要作用之一，就是使用 Pod 模板创建 Pod 的时候，对它们进行编号，并且按照编号顺序逐一完成创建工作。而当 StatefulSet 的“控制循环”发现 Pod 的“实际状态”与“期望状态”不一致，需要新建或者删除 Pod 进行“调谐”的时候，它会严格按照这些 Pod 编号的顺序，逐一完成这些操作。

Deployment 对应用做了一个简单化假设, 它认为，一个应用的所有 Pod，是完全一样的。

不满足：主从关系、主备关系。数据存储类应用等有状态应用。

StatefulSet 把真实世界里的应用状态，抽象为了两种情况。
+ 拓扑状态
应用的多个实例之间不是完全对等的关系，必须按照某些顺序启动。并且，新创建出来的 Pod，必须和原来 Pod 的网络标识一样。

+ 存储状态
应用的多个实例分别绑定了不同的存储数据。

> StatefulSet 的核心功能，就是通过某种方式记录这些状态，然后在 Pod 被重新创建时，能够为新 Pod 恢复这些状态。

## Headless Service
定义Service，用户只要能访问到这个 Service，它就能访问到某个具体的 Pod。Service是如何被访问的。

+ 以 Service 的 VIP（Virtual IP，即：虚拟 IP）方式。
它会把请求转发到该 Service 所代理的某一个 Pod 上

+ 以 Service 的 DNS 方式
只要我访问“my-svc.my-namespace.svc.cluster.local”这条 DNS 记录，就可以访问到名叫 my-svc 的 Service 所代理的某一个 Pod。

有两种处理方式：
```
1) Normal Service
访问“my-svc.my-namespace.svc.cluster.local”解析到的，正是 my-svc 这个 Service 的 VIP

2) Headless Service
访问“my-svc.my-namespace.svc.cluster.local”解析到的，直接就是 my-svc 代理的某一个 Pod 的 IP 地址

```
Headless Service 不需要分配一个 VIP，而是可以直接以 DNS 记录的方式解析出被代理 Pod 的 IP 地址。

Headless Service的定义
```yml
apiVersion: v1
kind: Service
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  ports:
  - port: 80
    name: web
  clusterIP: None #关键点，它其实就是一个Service只是，设置clusterIp为None
  selector:
    app: nginx
```
> 意味着 这个 Service 被创建后并不会被分配一个 VIP，而是会以 DNS 记录的方式暴露出它所代理的 Pod。
它所代理的所有 Pod 的 IP 地址，都会被绑定一个这样格式的 DNS 记录
```
<pod-name>.<svc-name>.<namespace>.svc.cluster.local
```

## StatefulSet的定义
```yml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: web
spec:
  serviceName: "nginx" # 告诉 StatefulSet 控制器，在执行控制循环（Control Loop）的时候，请使用 nginx 这个 Headless Service 来保证 Pod 的“可解析身份”
  replicas: 2
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.9.1
        ports:
        - containerPort: 80
          name: web
```
它创建出来的Pod, 编号都是从 0 开始累加，Pod 的创建，也是严格按照编号顺序进行的。前一个还没创建成功，后一个是不会创建的。

StatefulSet 就保证了 Pod 网络标识的稳定性。

Kubernetes 就成功地将 Pod 的拓扑状态（比如：哪个节点先启动，哪个节点后启动），按照 Pod 的“名字 + 编号”的方式固定了下来。

尽管 web-0.nginx 这条记录本身不会变，但它解析到的 Pod 的 IP 地址，并不是固定的。这就意味着，对于“有状态应用”实例的访问，你必须使用 DNS 记录或者 hostname 的方式，而绝不应该直接访问这些 Pod 的 IP 地址。

# 第二次部署

```shell
kubeadm init --control-plane-endpoint=192.168.211.150

kubeadm join 192.168.211.150:6443 --token o9edjn.idnlie1oijmgumpu \
    --discovery-token-ca-cert-hash sha256:badad3aba062a60ee0b0d600f0977e76e722cdea55d806283d3940b8a3a4cdb1
```

## 在k8s平台上安装rancher

```shell
helm repo add rancher-stable https://releases.rancher.com/server-charts/stable
kubectl create namespace cattle-system
```

```
helm install rancher rancher-stable/rancher \
  --namespace cattle-system \
  --set hostname=master
```

```
kubectl apply \
    -f https://raw.githubusercontent.com/jetstack/cert-manager/release-0.6/deploy/manifests/00-crds.yaml
    
kubectl label namespace cattle-system certmanager.k8s.io/disable-validation="true"

kubectl -n cattle-system rollout status deploy/rancher
```