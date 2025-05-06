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

    @PostMapping("/valid-reset-email")
    public ResultData<String> validateResetEmail(@Pattern (regexp = EMAIL_REGEXP)@RequestParam("email") String email,
                                                 HttpServletRequest request){
        HttpSession session = request.getSession();
        // 修改密码时必须要有这个账户才可以
        String s = authorizeService.sendValidateEmail(email, session.getId(),true);
        if (s == null) {
            return ResultData.success("邮件已发送，请注意查收");
        }else {
            return ResultData.fail("400",s);
        }
    }


    @PostMapping("/start-reset")
    public ResultData<String> startRest(  @Pattern (regexp = EMAIL_REGEXP)  @RequestParam("email")String email,
                                        @Length(min = 6,max = 6) @RequestParam("code")String code,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        String s = authorizeService.validateOnly(email,code,session.getId());
        if( s == null){  // 如果验证成功,就将需要重置密码的邮件地址传入
            session.setAttribute("reset-password", email);
            return ResultData.success("校验成功，请重置密码");
        }
        return ResultData.fail("400",s);
    }

    @PostMapping("/do-reset")
    public ResultData<String> resetPassword(  @Length(min = 6,max = 18)@RequestParam("password")String password,
                                            HttpServletRequest request){
        HttpSession session = request.getSession();
        // 如果我们刚刚验证码通过就会存放一条session数据，如果此时浏览器带过来了，那么就允许修改密码
        // 如果没有携带，说明就没有通过校验！直接就调用我们这个修改密码的接口了
        String email = (String) session.getAttribute("reset-password");
        if( email == null){
            return ResultData.fail("401","请先完成邮箱验证");
        }else if(authorizeService.resetPasswordByEmail(password,email)){
            session.removeAttribute("reset-password");
            return ResultData.success("密码重置成功");
        }else {
            return ResultData.fail("500","内部错误，请联系管理员");
        }
    }


    @GetMapping("/fx")
    public String testFX(){
      return "测试放行接口";
    }

}