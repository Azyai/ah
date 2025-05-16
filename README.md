# 系统模块介绍
1. cloud_user：用户认证模块，可以单体使用，也可以在微服务使用，使用的是jwt实现的（没有实现oauth2，因为我不会！）
2. cloud_gateway：网关模块，统一校验权限
3. cloud_api_commons：公共模块，存放一些公共的类
4. cloud_price_provider：抽奖模块提供者
5. cloud_price_consumer：抽奖模块消费者，主要是为了使用openfeign和rocketMQ