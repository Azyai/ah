package com.itay.controller;

import com.itay.resp.ResultData;
import com.itay.securityservice.AuthorizeService;
import com.itay.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name="用户系统",description = "用户接口管理模块")
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

    //
    @Operation(description = "发送注册邮件")
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


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(description = "登录账号")
    @PostMapping("/login")
    public ResultData<String> login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtUtils.generateToken(authentication.getName(), authentication.getAuthorities());
            System.out.println(token);

            return ResultData.success("登录成功");
        } catch (AuthenticationException e) {
            return ResultData.fail("401", "用户名或密码错误");
        }
    }



    @Autowired
    StringRedisTemplate redisTemplate;


    @Operation(description = "退出登录，需要携带token")
    @PostMapping("/logout")
    @Parameters({@Parameter(name = "Authorization", description = "Bearer token", required = true)})
    public ResultData<String> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            return ResultData.fail("401", "未提供有效 Token");
        }

        String token = header.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return ResultData.fail("401", "Token 无效");
        }

        try {
            String username = jwtUtils.parseUsername(token);
            jwtUtils.addToBlacklist(token);

            // 👇 新增：删除 Redis 中的权限缓存
            redisTemplate.delete("user:" + username + ":authorities");

            return ResultData.success("退出成功");
        } catch (Exception e) {
            return ResultData.fail("500", "退出失败，请稍后再试");
        }
    }






    @Operation(description = "注册账号,需要验证邮件编码")
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

    @Operation(description = "重置密码-发送验证码")
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


    @Operation(description = "重置密码-开始重置-主要是验证 验证码是否正确")
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

    @Operation(description = "重置密码-重置密码接口")
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