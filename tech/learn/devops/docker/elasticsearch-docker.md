# Docker搭建elasticsearch
## Dockerfile
```text
from ubuntu
run groupadd -r es && useradd -r -g es es
add es.tar.gz /
copy elasticsearch.yml /elasticsearch-7.3.1/config/
expose 9200
run mkdir -p /data/es && \
        mkdir -p /data/logs && \
        chown -R es:es /data && \
        chmod -R 777 /data && \
        chown -R es:es /elasticsearch-7.3.1
user es
entrypoint ["/elasticsearch-7.3.1/bin/elasticsearch"]
~
```

## elasticsearch.yml
```yml
cluster.name: test
node.name: node1
path.data: /data/es
path.logs: /data/logs
network.host: 0.0.0.0
http.port: 9200
discovery.seed_hosts: ["127.0.0.1"]
cluster.initial_master_nodes: ["node1"]
```
+ 集群搭建的话，只需要修改里边的配置文件就可以了，比如discovery.seed.hosts里的配置

## 运行
```sh
docker run --name es --network host -d es:latest
# 如果需要挂接磁盘
docker run --name=es --network=host --privileged=true -v /data/es/data:/data/es -v /data/es/logs:/data/logs -d es:latest
# 但是要确保 chmod -R 777 /data/es
```

# kibana的搭建
## Dockerfile
```text
from ubuntu
run groupadd -r es && useradd -r -g es es
add kibana.tar.gz /
copy kibana.yml /kibana-7.3.1-linux-x86_64/config/
run chown -R es:es /kibana-7.3.1-linux-x86_64/
user es
entrypoint ["/kibana-7.3.1-linux-x86_64/bin/kibana"]
```

## kibana.yml
```yml
server.port: 5601
server.host: "master"
server.name: "master"
elasticsearch.hosts: ["http://localhost:9200"]
kibana.index: ".kibana"
i18n.locale: "zh-CN"
```
