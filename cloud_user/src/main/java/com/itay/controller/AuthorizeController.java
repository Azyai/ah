package com.itay.controller;

import com.itay.resp.ResultData;
import com.itay.securityservice.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated  //开启参数验证
@RestController
@RequestMapping(value = "/api/auth" )
public class AuthorizeController {
    // 邮箱的校验规则
    private final String EMAIL_REGEXP = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";

    // 用户名的校验规则，与前端保持一致即可
    private final String UNAME_REGEXP = "^[a-zA-Z0-9_-]{3,18}$";
    @Resource
    AuthorizeService authorizeService;

    @PostMapping("/valid-register-email")
    public ResultData<String> validateRegisterEmail(@Pattern (regexp = EMAIL_REGEXP)@RequestParam("email") String email,
                                                  jakarta.servlet.http.HttpServletRequest request){
        jakarta.servlet.http.HttpSession session = request.getSession();

        String s = authorizeService.sendValidateEmail(email, session.getId(),false);
        if (s == null) {
            return ResultData.success("邮件已发送，请注意查收");
        }else {
            return ResultData.fail("400",s);
        }
    }

    @PostMapping("/register")
    public ResultData<String> registerUser(@Pattern(regexp = UNAME_REGEXP) @Length(min = 3,max = 18) @RequestParam("username") String username,
                                         @Length(min = 6,max = 18)@RequestParam("password")String password,
                                         @Pattern (regexp = EMAIL_REGEXP)  @RequestParam("email")String email,
                                         @Length(min = 6,max = 6) @RequestParam("code")String code,
                                         HttpServletRequest request){
        HttpSession session = request.getSession();
        String s = authorizeService.validateAndRegister(username, password, email, code, session.getId());
        if(s == null) {
            return ResultData.success("注册成功");
        } else {
            return ResultData.fail("400",s);
        }
    }


    @GetMapping("/fx")
    public String testFX(){
      return "测试放行接口";
    }





}

