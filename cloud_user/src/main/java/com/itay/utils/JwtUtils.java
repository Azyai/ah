package com.itay.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private  String SECRET_KEY;
    private  long EXPIRATION = 86400000; // 24h

    @Autowired
    private StringRedisTemplate redisTemplate;

//    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
//        List<String> roles = authorities.stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//
//        // 修改后（处理ROLE_前缀）
////        String authorityStr = roles.stream()
////                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_"+role)
////                .collect(Collectors.joining(","));
//
//        String authorityStr = String.join(",", roles); // 👈 使用逗号拼接
//        System.out.println(authorityStr);
//        redisTemplate.opsForValue().set("user:" + username + ":authorities", authorityStr, EXPIRATION, TimeUnit.MILLISECONDS);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
//                .compact();
//    }


    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        // 缓存到 Redis
        redisTemplate.opsForValue().set("user:" + username + ":authorities", roles.toString(), EXPIRATION, TimeUnit.MILLISECONDS);

        return token;
    }

    public String parseUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // 解析 Token
            var claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            System.out.println("Token Subject: " + claims.getSubject());
            System.out.println("Token Expire: " + claims.getExpiration());
            System.out.println("Now Time: " + new Date(System.currentTimeMillis()));

            // 检查是否过期
            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
                return false; // Token 已过期
            }

            // 检查是否在黑名单中
            return !isInBlacklist(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public void addToBlacklist(String token) {
        String jti = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId(); // 如果有 JTI 字段可识别唯一性

        if (jti == null) {
            jti = UUID.randomUUID().toString();
        }

        long expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime() - System.currentTimeMillis();

        redisTemplate.opsForValue().set("blacklist:token:" + jti, "true", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isInBlacklist(String token) {
        String jti = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();

        if (jti == null) return false;

        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:token:" + jti));
    }
}
