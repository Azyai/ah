package com.itay.controller;

import com.itay.resp.ResultData;
import com.itay.resp.UserInfo;
import com.itay.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserProfileService userProfileService;

    @PreAuthorize("hasAnyAuthority('2099')")
    @GetMapping("/getUserInfo")
    public ResultData<UserInfo> getUserInfoById(int id){
        UserInfo u = userProfileService.findUserProfileByUserByUserId(id);
        return ResultData.success(u);
    }

    @PreAuthorize("hasAnyAuthority('20100')")
    @GetMapping("/getUserInfo2")
    public ResultData<UserInfo> getUserInfoById2(int id){
        UserInfo u = userProfileService.findUserProfileByUserByUserId(id);
        return ResultData.success(u);
    }

}
