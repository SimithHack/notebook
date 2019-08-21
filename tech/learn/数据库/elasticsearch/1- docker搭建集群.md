# 环境准备
+ 机器节点

|IP|hostname|
|:--:|:--:|
|192.168.211.130|master
|192.168.211.131|slave01
|192.168.211.132|slave02

+ dokcer
```bash
[xiefq@master ~]$ docker -v
Docker version 18.06.3-ce, build d7080c1
```

+ 拉取elasticsearch镜像
```sh
[xiefq@master ~]$ docker pull elasticsearch:7.2.0
7.2.0: Pulling from library/elasticsearch
8ba884070f61: Downloading    6.3MB/75.4MB
2211b14f8b24: Downloading  5.191MB/43.16MB
617ccdb47f3d: Download complete
915ee6b2c338: Waiting
b414b7f29a7d: Waiting
547bfdd35d62: Waiting
8353a2ed248c: Waiting

```

+ 修改vm.max_map_count
```
sudo vi /etc/sysctl.conf
vm.max_map_count=262144
```

# 运行
分别在三台机器上执行

+ master
```sh
docker run -p 9200:9200 -p 9300:9300 -e "node.name=es_master" -e "discovery.seed_hosts=slave01,slave02" -e "cluster.initial_master_nodes=es_master" -e "cluster.name=xiefq" -e "bootstrap.memory_lock=true" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" -v /data/elasticsearch/data:/usr/share/elasticsearch/data --network host --name=es_master -d elasticsearch:7.2.0
```

+ slave01
```sh
docker run -p 9200:9200 -p 9300:9300 -e "node.name=es_01" -e "discovery.seed_hosts=master,slave02" -e "cluster.initial_master_nodes=es_master" -e "cluster.name=xiefq" -e "bootstrap.memory_lock=true" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" -v /data/elasticsearch/data:/usr/share/elasticsearch/data --network host --name=es_01 -d elasticsearch:7.2.0
```

+ slave02
```sh
docker run -p 9200:9200 -p 9300:9300 -e "node.name=es_02" -e "discovery.seed_hosts=master,slave01" -e "cluster.initial_master_nodes=es_master" -e "cluster.name=xiefq" -e "bootstrap.memory_lock=true" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" -v /data/elasticsearch/data:/usr/share/elasticsearch/data --network host --name=es_02 -d elasticsearch:7.2.0
```