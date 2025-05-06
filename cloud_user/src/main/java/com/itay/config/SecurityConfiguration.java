package com.itay.config;

import jakarta.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // 只是编写了配置文件，还没有写登录成功的重定向302
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .anyRequest().authenticated()
        ).formLogin(form -> {
            form.loginProcessingUrl("api/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password");
//                    .successHandler(this::onAuthenticationSuccess);
        }).logout(logout -> {
            logout.logoutUrl("api/auth/logout");
        }).csrf(csrf -> csrf.disable());

        return http.build();
    }

//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        response.setCharacterEncoding("utf-8");
//        response.getWriter().write("登录成功!");
//    }



}