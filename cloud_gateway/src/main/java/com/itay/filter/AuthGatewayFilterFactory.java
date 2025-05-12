package com.itay.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itay.resp.ResultData;
import com.itay.utils.JwtUtilsGateWay;
import com.itay.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
/**
 * 自定义网关过滤器工厂类，用于在 Spring Cloud Gateway 中实现 JWT 鉴权逻辑。
 * 该类继承 AbstractGatewayFilterFactory 并注册为 Spring Bean。
 */
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    /**
     * 注入 JwtUtilsGateWay 工具类，用于 JWT 的解析、验证及黑名单校验。
     */
    @Autowired
    private JwtUtilsGateWay jwtUtilsGateWay;

    /**
     * 注入 StringRedisTemplate，用于从 Redis 查询用户权限码。
     */
    @Autowired
    private StringRedisTemplate redisTemplate;


    public AuthGatewayFilterFactory() {
        super(AuthGatewayFilterFactory.Config.class);
    }


    /**
     * 根据配置创建并返回一个 GatewayFilter 实例。
     * 该 filter 将会在请求进入网关时执行鉴权逻辑。
     * 参数说明：
     * exchange: 当前请求的上下文，包含请求和响应对象。
     * chain: 过滤器链，用于控制是否继续执行后续过滤器或目标服务。
     * ServerHttpRequest: 代表当前 HTTP 请求，从中提取路径、Header 等信息
     *
     * @param config 过滤器配置对象（此处未使用）
     * @return 返回一个 GatewayFilter 实例
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. 白名单路径放行
            if (isWhiteList(request.getPath().value())) {
                return chain.filter(exchange);
            }

            // 2. 提取 Token
            String authHeader = request.getHeaders().getFirst("Authorization");
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            System.out.println(token + "   ------");

            if (token == null) {
                return unauthorized(exchange, "Missing token");
            }

            // 3. 验证 Token
            if (!jwtUtilsGateWay.validateToken(token)) {
                return unauthorized(exchange, "Invalid or expired token");
            }

            // 4. 获取用户名
            String username = jwtUtilsGateWay.parseUsername(token);
            System.out.println(username+" -----");
            if (username == null) {
                return unauthorized(exchange, "Invalid token");
            }

            // 5. 查询 Redis 用户权限码 List<String>
            String authorityStr = redisTemplate.opsForValue().get("user:" + username + ":authorities");

            if (authorityStr == null || authorityStr.isEmpty()) {
                return forbidden(exchange, "No authorities found for user");
            }

            List<String> authorities = Arrays.asList(authorityStr.split(","));
            System.out.println(authorities + " -----");

            if (authorities == null || authorities.isEmpty()) {
                return forbidden(exchange, "No authorities found for user");
            }

            // 6. 判断是否有访问权限
            String path = request.getPath().value();
            System.out.println(path + " -----");
            if (!hasAuthorities(path, authorities)) {
                return forbidden(exchange, "Access denied to this resource");
            }

            // 7. 放行
            return chain.filter(exchange);

        };
    }
    private boolean isWhiteList(String path) {
        return Arrays.stream(SecurityConstants.WHITE_LIST).anyMatch(path::startsWith);
    }

    private boolean hasAuthorities(String path, Collection<String> authorities) {
        String permissionCode = resolveAuthoritiesCode(path);
        return permissionCode != null && authorities.contains(permissionCode);
    }

    /**
     * 后续会改成使用MySQL数据库表实现，这里只是为了演示,所有的权限码都可以访问
     * @param path
     * @return
     */
    private String resolveAuthoritiesCode(String path) {
      return "1010";
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ResultData<?> resultData = ResultData.fail("401", message);
        return writeResponse(exchange, HttpStatus.UNAUTHORIZED, resultData);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        ResultData<?> resultData = ResultData.fail("403", message);
        return writeResponse(exchange, HttpStatus.FORBIDDEN, resultData);
    }




    private Mono<Void> writeResponse(ServerWebExchange exchange, HttpStatus status, ResultData<?> resultData) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(resultData);
        } catch (JsonProcessingException e) {
            // 出现异常时返回默认错误信息
            bytes = "{\"code\":\"500\",\"message\":\"Internal server error\"}".getBytes();
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }


    public static class Config {
        // 可以用于配置参数，例如指定某些路径需要特定角色等
    }

}
