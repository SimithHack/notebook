## 部署一个有状态的应用程序流程
+ 应用程序描述
    ```text
    POST请求时，将请求body写到一个文件里，Get请求时返回hostname并返回文件的内容
    ```
+ 创建PersistentVolume
    - 创建3个pv
    ```yaml
    kind: List
    apiVersion: v1
    items:
    - apiVersion: v1
      kind: PersistentVolume
      metadata:
        name: pv-a
      spec:
        capacity:
          storage: 1Mi
        accessModes:
          - ReadWriteOnce
        persistentVolumeReclaimPolicy: Recycle
        gcePersistentDisk:
          pdName: pv-a
          fsType: nfs4
    - apiVersion: v1
      kind: PersistentVolume
      metadata:
        name: pv-b
    ...
    ```
+ 创建 governing 服务

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kubia
spec:
  clusterIP: None # 使之成为headless service
  selector:
    app: kubia
  ports:
  - name: http
    port: 80
```

+ 创建 statefulset

```yaml
apiVersion: apps/vibeta1
kind: StatefulSet
metadata:
  name: kubia
spec:
  serviceName: kubia
  replicas: 2
  template:
      metadata:
        labels:
          app: kubia
      spec:
        containers:
        - name: kubia
          image: luksa/kubia-pet
          ports:
          - name: http
            containerPort: 8080
          volumeMounts:
          - name: data
            mountPath: /var/data
  volumeClaimTemplates:
    - metadata:
      name: data      
    spec:
      resources:
        requests:
          storage: 1Mi
      accessModes:
      - ReadWriteOnce
```

## 通过api server 代理访问pod
<apiServerHost>:<port>/api/v1/namespaces/default/pods/kubia-0/proxy/<path>

## 通过api server 代理访问service
/api/v1/namespaces/<namespace>/services/<service name>/proxy/<path>

从行为上来看，StatefulSet更像ReplicaSet，在做版本更新的时候，他不 会自动roll out而需要手动去删除老版本的pod，然后让StatefulSet使用新的模板去创建新的pod

**注意**
```
在新版本的k8s里，> 1.7 , StatefulSet也支持rolling out
```

### 集群数据存储
就是在每个有状态的服务节点上，利用k8s的peer discovery的能力，去请求其他服务的同等接口，然后将返回的数据进行整合zc 

为什么StatefulSet必须要等节点挂了之后才能重新启动，因为，要替换一个节点，那就必须要和被替换的pod一个标识
