package com.itay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = "com.itay.cloud.apis") 如果扫描不到用这条
@EnableFeignClients//启用feign客户端,定义服务+绑定接口，以声明式的方法优雅而简单的实现服务调用 一定要加这个注解否则不能将Feign接口识别成Bean
public class Main8083 {
    public static void main(String[] args)
    {
        SpringApplication.run(Main8083.class,args);
    }
}