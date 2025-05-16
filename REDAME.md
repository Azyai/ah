# 系统模块
1. cloud_user：用户认证中心，使用jwt作为校验方式，可以单体使用也可以微服务服用（但并没有使用oauth2协议）
2. cloud_gateway：网关，统一鉴权
3. cloud_api_commons：通用模块
4. cloud_price_provider：抽奖模块提供者，主要是为了使用openfeign和rocketMQ
5. cloud_price_consumer：抽奖模块消费者
