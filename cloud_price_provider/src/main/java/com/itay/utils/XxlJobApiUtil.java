package com.itay.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class XxlJobApiUtil {

    private static final String ADMIN_URL = "http://192.168.117.134:8088/xxl-job-admin/api/jobinfo";
    private static final String LOGIN_URL = "http://192.168.117.134:8088/xxl-job-admin/toLogin";
    private static final String ACCESS_TOKEN = "default_token";

    @Resource
    private RestTemplate restTemplate;

    /**
     * 注册定时任务
     */
    public boolean registerJob(String jobDesc, String cron, String executorHandler,
                               Integer jobGroup, String executorParam) {
        Map<String, Object> params = buildCommonParams(jobDesc, cron, executorHandler,
                jobGroup, executorParam);
        return sendPostRequest(ADMIN_URL, params);
    }

    /**
     * 更新定时任务
     */
    public boolean updateJob(int jobId, String jobDesc, String cron, String executorHandler,
                             Integer jobGroup, String executorParam) {
        Map<String, Object> params = buildCommonParams(jobDesc, cron, executorHandler,
                jobGroup, executorParam);
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/update", params);
    }

    /**
     * 删除定时任务
     */
    public boolean removeJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/delete", params);
    }

    /**
     * 暂停定时任务
     */
    public boolean pauseJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/stop", params);
    }

    /**
     * 恢复定时任务
     */
    public boolean resumeJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/start", params);
    }

    /**
     * 手动触发任务执行
     */
    public boolean triggerJob(int jobId) {
        String triggerUrl = "http://192.168.117.134:8088/xxl-job-admin/api/joblog/trigger";
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(triggerUrl, params);
    }

    // 构建通用请求参数
    private Map<String, Object> buildCommonParams(String jobDesc, String cron, String executorHandler,
                                                  Integer jobGroup, String executorParam) {
        Map<String, Object> params = new HashMap<>();
        params.put("jobGroup", jobGroup); // 执行器ID
        params.put("jobDesc", jobDesc); // 任务描述
        params.put("scheduleType", "CRON");
        params.put("scheduleConf", cron); // CRON表达式
        params.put("executorHandler", executorHandler); // JobHandler名称
        params.put("executorParam", executorParam); // 参数
        params.put("misfireStrategy", "DO_NOTHING");
        params.put("glueType", "BEAN");
        params.put("createdBy", "system");
        return params;
    }

    // 发送POST请求
    private boolean sendPostRequest(String url, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("XXL_JOB_ACCESS_TOKEN", ACCESS_TOKEN);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}