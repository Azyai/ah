# 系统模块介绍
1. cloud_user：用户认证模块，可以单体使用，也可以在微服务使用，使用的是jwt实现的
2. cloud_gateway：网关模块，统一校验权限
3. cloud_api_commons：公共模块，存放一些公共的类
4. cloud_price_provider：抽奖模块提供者
5. cloud_price_consumer：抽奖模块消费者，主要是为了使用openfeign
6. SQL文件项目已经给出：ah2025.sql，需要自己去配置数据库的连接信息
7. 另外还需要自己配置nacos注册中心的连接信息

### 1.首页
在首页可以查看到活动信息，然后右上角有登录/注册链接
![img.png](image/img.png)

#### 1.1注册功能
首先我们注册一下，如果该邮箱已经被注册了，则无法注册
![img_2.png](image/img_2.png)

如果该邮箱没有被注册，则会正常发送邮件
![img_3.png](image/img_3.png)

然后我们将邮件中的验证码和个人信息填写好后注册
![img_4.png](image/img_4.png)

如果验证码不一致,则不允许注册
![img_5.png](image/img_5.png)

#### 1.2登录功能
登录后可以查看自己的个人信息
![img_1.png](image/img_1.png)

### 2.活动相关

#### 2.1活动列表
![img.png](image/img_6.png)

#### 2.2活动详情
![img_1.png](image/img_7.png)

#### 2.3抽奖页面
![img_2.png](image/img_8.png)

### 3.用户信息
所有信息均可以导出成excel表
#### 3.1参与列表
![img_4.png](image/img_10.png)
#### 3.2中奖列表
![img_5.png](image/img_11.png)

### 4.其他
#### 4.1联系我们
![img_3.png](image/img_9.png)