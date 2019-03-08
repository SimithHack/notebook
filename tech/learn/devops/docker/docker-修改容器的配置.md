## 修改容器的配置
> 已经运行的容器，要修改端口或者挂接新的数据盘

### 方法1 创建一个新的镜像
终止现有的容器然后基于此容器创建一个新的镜像，新镜像包含要修改的配置

```bash
docker commit https-portal https-portal:2
docker run -p 8080:80 -td https-portal:2
```

### 方法2 修改配置文件
配置文件的路径 /var/lib/docker/containers/container-id/config.v2.json   
容器关闭后，可以使用docker update方法进行修改

### 方法3 修改dockerfile
```yml
domain.com:
  container_name: domain.com
image: wordpress
mem_limit: 500m
restart: always
environment:
  WORDPRESS_DB_HOST: XX.XX.XX.XX
  WORDPRESS_DB_NAME: domain-wp
  WORDPRESS_DB_USER: username
  WORDPRESS_DB_PASSWORD: 'password'
VIRTUAL_HOST: domain.com
volumes: - /home/username/files/domain.com/:/var/www/html:rw
```

执行更新
```bash
docker-compose -f dockerfile.yml up -d --no-recreate
```
