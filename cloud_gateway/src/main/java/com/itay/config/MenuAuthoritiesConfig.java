package com.itay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "menu-authorities")
public class MenuAuthoritiesConfig {

    private List<MenuAuthority> authorities;

    @Data
    public static class MenuAuthority {
        private Integer id;
        private String icon;
        private String name;
        private Integer state;
        private String url;
        private Integer pId;
        private String aclValue;
        private Integer grade;
    }
}
