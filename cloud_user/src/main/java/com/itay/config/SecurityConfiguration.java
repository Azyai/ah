package com.itay.config;


import com.itay.resp.ResultData;
import com.itay.securityservice.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//     在 Spring Security 5.7 及以上版本中，AuthenticationManagerBuilder 的 and() 方法已被标记为弃用，并计划在未来版本中移除
//     这是因为 Spring Security 团队正在推动更清晰的 API 设计，避免链式调用可能导致的歧义
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(authorizeService);
        return authBuilder.build();

    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 只是编写了配置文件，还没有写登录成功的重定向302
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .anyRequest().authenticated()
        ).formLogin(form -> {
            form.loginProcessingUrl("/api/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(this::onAuthenticationSuccess);
        }).logout(logout -> {
            logout.logoutUrl("/api/auth/logout");
        }).csrf(csrf -> csrf.disable());

        return http.build();
    }


    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        ResultData<String> resultData = ResultData.success("登录成功");
        response.getWriter().write(String.valueOf(resultData));
    }

    // 手动配置用户信息
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withUsername("user")
                .password("{noop}user") // {noop}表示不加密
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();
        //可以继续追加其它用户...
        UserDetails anonymous = User.withUsername("anonymous")
                .password("{noop}anonymous")
                .roles("ANONYMOUS")
                .build();

        return new InMemoryUserDetailsManager(user, admin, anonymous);
    }


}