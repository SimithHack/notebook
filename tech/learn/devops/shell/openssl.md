参考 https://blog.csdn.net/liuchunming033/article/details/48470575

## 什么是x509证书链
```
x509证书一般会用到三类文件，key，csr，crt。
Key是私用密钥，openssl格式，通常是rsa算法。
csr是证书请求文件，用于申请证书。在制作csr文件的时候，必须使用自己的私钥来签署申请，还可以设定一个密钥。
crt是CA认证后的证书文件（windows下面的csr，其实是crt），签署人用自己的key给你签署的凭证。
```
## 概念
```
首先要有一个CA根证书，然后用CA根证书来签发用户证书。
用户进行证书申请：一般先生成一个私钥，然后用私钥生成证书请求(证书请求里应含有公钥信息)，再利用证书服务器的CA根证书来签发证书。
特别说明:
（1）自签名证书(一般用于顶级证书、根证书): 证书的名称和认证机构的名称相同.
（2）根证书：根证书是CA认证中心给自己颁发的证书,是信任链的起始点。任何安装CA根证书的服务器都意味着对这个CA认证中心是信任的。
数字证书则是由证书认证机构（CA）对证书申请者真实身份验证之后，用CA的根证书对申请人的一些基本信息以及申请人的公钥进行签名（相当于加盖发证书机构的公章）后形成的一个数字文件。数字证书包含证书中所标识的实体的公钥（就是说你的证书里有你的公钥），由于证书将公钥与特定的个人匹配，并且该证书的真实性由颁发机构保证（就是说可以让大家相信你的证书是真的），因此，数字证书为如何找到用户的公钥并知道它是否有效这一问题提供了解决方案。
```

## openssl中有如下后缀名的文件
```
.key格式：私有的密钥

.csr格式：证书签名请求（证书请求文件），含有公钥信息，certificate signing request的缩写

.crt格式：证书文件，certificate的缩写

.crl格式：证书吊销列表，Certificate Revocation List的缩写

.pem格式：用于导出，导入证书时候的证书的格式，有证书开头，结尾的格式
```

## CA根证书的生成步骤
生成CA私钥（.key）-->生成CA证书请求（.csr）-->自签名得到根证书（.crt）（CA给自已颁发的证书）。

```
# Generate CA private key 
openssl genrsa -out ca.key 2048 
# Generate CSR 
openssl req -new -key ca.key -out ca.csr
# Generate Self Signed certificate（CA 根证书）
openssl x509 -req -days 365 -in ca.csr -signkey ca.key -out ca.crt
```
在实际的软件开发工作中，往往服务器就采用这种自签名的方式，因为毕竟找第三方签名机构是要给钱的，也是需要花时间的。

* 用户证书的生成步骤

生成私钥（.key）-->生成证书请求（.csr）-->用CA根证书签名得到证书（.crt）

* 服务器端用户证书：
```
# private key
$openssl genrsa -des3 -out server.key 1024 
# generate csr
$openssl req -new -key server.key -out server.csr
# generate certificate
$openssl ca -in server.csr -out server.crt -cert ca.crt -keyfile ca.key 
```

* 客户端用户证书：
```
$openssl genrsa -des3 -out client.key 1024 
$openssl req -new -key client.key -out client.csr
$openssl ca -in client.csr -out client.crt -cert ca.crt -keyfile ca.key
```

## PME格式转换
```
openssl rsa -in server.key -text > private.pem
openssl x509 -inform PEM -in server.crt > public.pem
```

```
openssl genrsa -out key.pem 1024
openssl req -new -out req.csr -key key.pem
openssl x509 -req -in server-req.csr -out server-cert.pem -signkey server-key.pem -CA ../cert.pem -CAkey ../key.pem -CAcreateserial -days 3650
openssl pkcs12 -export -clcerts -in server-cert.pem -inkey server-key.pem -out server.p12
```