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
  "registry-mirrors": ["https://yourcode.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```