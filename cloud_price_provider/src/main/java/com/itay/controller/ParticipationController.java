package com.itay.controller;

import com.itay.resp.ResultData;
import com.itay.service.ParticipationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/draw/participation")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @PostMapping("/participate")
    public ResultData<String> participate(Integer activityId, @RequestHeader("X-Device-Fingerprint") String deviceFingerprint, HttpServletRequest request) {

        // 从网关传递的认证信息中获取用户ID（假设网关已经添加了X-User-Id请求头）
        Long userId = Long.parseLong(request.getHeader("X-User-Id"));
        String ip = request.getRemoteAddr();
        boolean success = participationService.addParticipate(userId, activityId, ip, deviceFingerprint);
        return success ? ResultData.success("参与成功") : ResultData.fail("参与失败，请稍后再试！");
    }

}
