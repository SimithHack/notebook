### Docker pull error : x509: certificate has expired or is not yet valid
出现这种问题一般是服务器时间不同步，可以使用ntp更新一下系统时间
```bash
# 安装ntp
sudo yum -y install ntp ntpdate
# 更新时间
sudo ntpdate time.apple.com
```
然后docker pull镜像的时候就成功了

## docker修改镜像源
```bash
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://registry.docker-cn.com","http://hub-mirror.c.163.com","https://docker.mirrors.ustc.edu.cn"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## docker版本和内核不一致的问题
```text
docker容器启动报错：docker: Error response from daemon: OCI runtime create failed: container_linux.go:344
docker容器启动报错：
docker: Error response from daemon: OCI runtime create failed: container_linux.go:344: starting container process caused "process_linux.go:297: getting the final child's pid from pipe caused \"read init-p: connection reset by peer\"": unknown.

原因：docker版本过高，内核版本过低造成。
解决办法：降低docker版本或升级内核版本。已知yum安装的docker-18.09 与系统内核3.10版本不符，容器启动失败。
```

# docker registry
本地测试，如果没有证书，可以指定使用http代理https
```
[root@rancher-02 ~]# cat /etc/docker/daemon.json
{
  "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn/"],
  "insecure-registries": ["rancher-03:5000"]
}
```

# 配置gitlab域名
```
配置GitLab域名，否则项目git clone的地址时错
vim  /etc/gitlab/gitlab.rb

编辑：external_url '你的网址'

例如：external_url 'http://192.168.1.100'

编辑完成后，再sudo gitlab-ctl reconfigure一下，使配置生效
```