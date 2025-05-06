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

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;


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

    @Resource
    DataSource dataSource;

    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次true自动创建一个表，我们也可以手动创建(首次需要，其他就不需要了，再次运行就要改为false)
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    // 只是编写了配置文件，还没有写登录成功的重定向302
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository tokenRepository) throws Exception {
        http.authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                ).formLogin(form -> {
                    form.loginProcessingUrl("/api/auth/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .successHandler(this::onAuthenticationSuccess);
                }).logout(logout -> {
                    logout.logoutUrl("/api/auth/logout");
                }).csrf(AbstractHttpConfigurer::disable)
                .rememberMe(remember -> remember.rememberMeParameter("remember")
                        .tokenRepository(tokenRepository) //这里要将其注入才能自动创建表
                        .tokenValiditySeconds(3600 * 24 * 7 )) //以秒计算，7天内免登录
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                });

        return http.build();
    }


    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许我们携带cookie，因为前端我们是设置了的发送cookie
        corsConfiguration.setAllowCredentials(true);
        // 设置允许的请求头
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        // 设置允许请求的方法
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));

        // corsConfiguration.addAllowedOriginPattern("*");
        // 设置能允许请求的路径 这两个都可以设置单个或全部，只不过下面这个可以设置多个允许地址

        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173/"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有的请求都走我们这个策略
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        ResultData<String> resultData = ResultData.success("登录成功");
        response.getWriter().write(String.valueOf(resultData));
    }

}