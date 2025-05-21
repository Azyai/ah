package com.itay.utils;

public class SecurityConstants {
    public static final String[] WHITE_LIST = {
            "/api/auth/login",
            "/api/auth/valid-register-email",
            "/api/auth/valid-reset-email",
            "/api/auth/start-reset",
            "/api/auth/do-reset",
            "/api/auth/valid-email",
            "/api/auth/fx",
            "/api/auth/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/**",
            "/v3/**",
            "/webjars/**",
            "/error",
            "/static/**",
            "/user/GetUserId",
            // 放行图标
            "/favicon.ico",
    };
}