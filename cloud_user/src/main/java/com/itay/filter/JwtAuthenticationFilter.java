package com.itay.filter;

import com.itay.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token == null) {
            // 无 Token
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未提供 Token");
            return;
        }

        if (!jwtUtils.validateToken(token)) {
            // Token 非法或已过期
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 无效，请重新登录");
            return;
        }

        if (jwtUtils.isInBlacklist(token)) {
            // Token 已注销
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 已失效，请重新登录");
            return;
        }

        String username = jwtUtils.parseUsername(token);
        String authorityStr = redisTemplate.opsForValue().get("user:" + username + ":authorities");

        if (authorityStr == null || authorityStr.isEmpty()) {
            // 权限缓存为空（可能已退出）
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "权限信息不存在，请重新登录");
            return;
        }

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityStr);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
