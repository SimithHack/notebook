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
