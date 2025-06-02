package com.itay.job;

import com.alibaba.fastjson2.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FudaiDrawJobHandler {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @XxlJob("fudaiDrawJobHandler")
    public void fudaiDrawJobHandler() throws Exception {
        // 从上下文中获取activityId  假设有executorParam传递
        String param = XxlJobHelper.getJobParam();
        JSONObject paramJson = JSONObject.parseObject(param);
        Integer activityId = paramJson.getInteger("activityId");

        System.out.println("自动执行定时任务进行开奖，activityId是：" + activityId);

        // 发送抽奖消息到mq，执行开奖操作
        rocketMQTemplate.convertAndSend("fd-draw-topic", activityId);

    }
}
