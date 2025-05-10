package com.itay.controller;

import com.itay.entity.User;
import com.itay.resp.ResultData;
import com.itay.resp.UserInfo;
import com.itay.service.UserProfileService;
import com.itay.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class TestGateWayController {

    @PreAuthorize("hasAuthority('2099')") // 只有拥有 '2099' 权限的用户才能访问
    @GetMapping("/test")
    public ResultData<String> test() {
        return ResultData.success("test");
    }


    @GetMapping("/test2")
    public ResultData<String> test2() {
        return ResultData.success("test2");
    }


    @Autowired
    UserProfileService userProfileService;

    @Resource
    JwtUtils jwtUtils;


    @GetMapping("/getUserInfo")
    public ResultData<UserInfo> getUserInfo(HttpServletRequest request) {
        String token = extractToken(request);
        String username = jwtUtils.parseUsername(token);
        UserInfo userInfo = userProfileService.findUserProfileByUserByUserNameOreMail(username);
        return ResultData.success(userInfo);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
