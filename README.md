# 系统模块介绍
1. cloud_user：用户认证模块，可以单体使用，也可以在微服务使用，使用的是jwt实现的
2. cloud_gateway：网关模块，统一校验权限
3. cloud_api_commons：公共模块，存放一些公共的类
4. cloud_price_provider：抽奖模块提供者
5. cloud_price_consumer：抽奖模块消费者，主要是为了使用openfeign
6. SQL文件项目已经给出：ah2025.sql，需要自己去配置数据库的连接信息
7. 另外还需要自己配置nacos注册中心的连接信息