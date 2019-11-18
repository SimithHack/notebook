# oauth2常见的安全问题
+ 输入需要验证
    + CSRF令牌劫持 使用带state参数的CSRF token，确保OAuth flow的一致性
    + 重定向时泄露授权码或者令牌 明确注册重定向URIs，并确保URI验证
    + 通过切换客户劫持token 将同意客户和授权方式/token请求进行绑定

微服务的安全架构 令牌的校验
安全架构需要监控的指标
+ 登录次数，授权次数，办法访问令牌个数，活跃令牌个数，吊销次数 注册client数目
+ 接口条用性能指标
    + authroirze token instrospect revoke
    水平扩容，缓存使用

企业级开源产品
apereo CAS https://www.aperea.org/projects/cas
openid-connect =-java-s

配置的实现
    静态配置 动态配置
