package com.itay.resp;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReturnCodeEnum {
    // 1.举值
    /**操作失败**/
    RC999("999","操作XXX失败"),
    /**操作成功**/
    RC200("200","success"),
    /**服务降级**/
    RC201("201","服务开启降级保护,请稍后再试!"),
    /**热点参数限流**/
    RC202("202","热点参数限流,请稍后再试!"),
    /**系统规则不满足**/
    RC203("203","系统规则不满足要求,请稍后再试!"),
    /**授权规则不通过**/
    RC204("204","授权规则不通过,请稍后再试!"),
    /**access_denied**/
    RC403("403","无访问权限,请联系管理员授予权限"),
    /**access_denied**/
    RC401("401","匿名用户访问无权限资源时的异常"),
    RC404("404","404页面找不到的异常"),
    /**服务异常**/
    RC500("500","系统异常，请稍后重试"),
    RC375("375","数学运算异常，请稍后重试"),

    INVALID_TOKEN("2001","访问令牌不合法"),
    ACCESS_DENIED("2003","没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED("1001","客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR("1002","用户名或密码错误"),
    BUSINESS_ERROR("1004","业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE("1003", "不支持的认证模式");

    // 2.构造
    /**自定义状态码**/
    private final String code;
    /**自定义描述**/
    private final String message;

    ReturnCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    // 3.遍历
    // 3.1 传统版
    public static ReturnCodeEnum getReturnCodeEnumV1(String code){
        for(ReturnCodeEnum returnCodeEnum:ReturnCodeEnum.values()){
            if(returnCodeEnum.getCode().equalsIgnoreCase(code)){
                return returnCodeEnum;
            }
        }
        return null;
    }
    // 3.2Stream流式计算版
    public static ReturnCodeEnum getReturnCodeEnumV2(String code){
        return Arrays.stream(ReturnCodeEnum.values())
                .filter(returnCodeEnum -> returnCodeEnum.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public static void main(String[] args) {
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV1("200"));
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV1("200").getCode());
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV1("200").getMessage());
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV2("404"));
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV2("404").getCode());
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV2("404").getMessage());
        System.out.println(ReturnCodeEnum.getReturnCodeEnumV2("500").getMessage());
    }

}