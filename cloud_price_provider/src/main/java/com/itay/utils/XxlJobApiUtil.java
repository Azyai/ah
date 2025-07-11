package com.itay.utils;

import com.itay.pojo.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class XxlJobApiUtil {

    private static final String ADMIN_URL = "http://192.168.117.134:8088/xxl-job-admin/jobinfo";
    private static final String LOGIN_URL = "http://192.168.117.134:8088/xxl-job-admin/toLogin";

    // 使用access_token需要修改源码，这里没有使用，所以使用登录吧。。。
    private static final String ACCESS_TOKEN = "default_token";


    public String loginAndGetCookie(String username, String password) {
        String loginUrl = "http://192.168.117.134:8088/xxl-job-admin/login";

        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userName", username);
        formData.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // 改成接收 ReturnT<String>
        ResponseEntity<ReturnT<String>> responseEntity = restTemplate.exchange(
                loginUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ReturnT<String>>() {});

        ReturnT<String> returnT = responseEntity.getBody();

        if (returnT != null && returnT.getCode() == 200) {
            List<String> cookies = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (cookies != null && !cookies.isEmpty()) {
                return cookies.get(0); // 返回 JSESSIONID=xxx
            }
        } else {
            System.err.println("登录失败：" + (returnT != null ? returnT.getMsg() : "未知错误"));
        }

        return null;
    }



    @Autowired
    private RestTemplate restTemplate;

    /**
     * 注册定时任务
     */
    public ReturnT<String> registerJob(String jobDesc, String cron, String executorHandler,
                                       Integer jobGroup, String executorParam) {
        Map<String, Object> params = buildCommonParams(jobDesc, cron, executorHandler,
                jobGroup, executorParam,"XXL");

        ReturnT<String> stringReturnT = sendPostRequest(ADMIN_URL + "/add", params);
        if(stringReturnT.getCode() == 200){
            // 获取刚刚创建的任务ID（需要从返回值中解析）
             int jobId = Integer.parseInt(stringReturnT.getContent());

            System.out.println("jobId: " + jobId);
            //立即启动任务
            return startJob(jobId);
        }
        return null;
    }

    /**
     * 启动定时任务
     */
    public ReturnT<String> startJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/start", params);
    }

    /**
     * 更新定时任务
     */
    public ReturnT<String> updateJob(int jobId, String jobDesc, String cron, String executorHandler,
                                     Integer jobGroup, String executorParam) {
        Map<String, Object> params = buildCommonParams(jobDesc, cron, executorHandler,
                jobGroup, executorParam,"XXL");
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/update", params);
    }

    /**
     * 删除定时任务
     */
    public ReturnT<String> removeJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/delete", params);
    }

    /**
     * 暂停定时任务
     */
    public ReturnT<String> pauseJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/stop", params);
    }

    /**
     * 恢复定时任务
     */
    public ReturnT<String> resumeJob(int jobId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(ADMIN_URL + "/start", params);
    }

    /**
     * 手动触发任务执行
     */
    public ReturnT<String> triggerJob(int jobId) {
        String triggerUrl = "http://192.168.117.134:8088/xxl-job-admin/api/joblog/trigger";
        Map<String, Object> params = new HashMap<>();
        params.put("id", jobId);
        return sendPostRequest(triggerUrl, params);
    }

    private Map<String, Object> buildCommonParams(String jobDesc, String cron, String executorHandler,
                                                  Integer jobGroup, String executorParam, String author) {
        Map<String, Object> params = new HashMap<>();

        // 必填字段
        params.put("jobGroup", jobGroup); // 执行器ID
        params.put("jobDesc", jobDesc); // 任务描述
        params.put("author", author); // 作者
        params.put("scheduleType", "CRON"); // 调度类型
        params.put("scheduleConf", cron); // CRON表达式
        params.put("executorHandler", executorHandler); // JobHandler名称
        params.put("executorParam", executorParam); // 参数
        params.put("misfireStrategy", "DO_NOTHING"); // 过期策略
        params.put("glueType", "BEAN"); // Glue类型
        params.put("createdBy", "system");

        // 可选字段（建议补上默认值）
        params.put("alarmEmail", ""); // 报警邮件
        params.put("schedule_conf_CRON", cron); // CRON调度配置
        params.put("schedule_conf_FIX_RATE", "");
        params.put("schedule_conf_FIX_DELAY", "");

        params.put("executorRouteStrategy", "FIRST"); // 路由策略
        params.put("executorBlockStrategy", "SERIAL_EXECUTION"); // 阻塞策略
        params.put("executorTimeout", "60"); // 超时时间
        params.put("executorFailRetryCount", "3"); // 失败重试次数
        params.put("glueRemark", "GLUE代码初始化");
        params.put("glueSource", "");
        params.put("childJobId", ""); // 子任务ID，默认空

        return params;
    }


    String cookieValue = null;

    // 发送POST请求
    private ReturnT<String> sendPostRequest(String url, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if (cookieValue == null) {
            cookieValue = loginAndGetCookie("admin", "Ay234411");
        }
        headers.set("Cookie", cookieValue);

        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            formData.add(entry.getKey(), entry.getValue().toString());
        }

        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // 改用 ReturnT 接收响应
            ResponseEntity<ReturnT<String>> response = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ReturnT<String>>() {});

            System.out.println(response.getBody());
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}