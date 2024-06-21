package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    // 用于生成 JWT 令牌的密钥，通常是一个加密字符串
    private String adminSecretKey;
    // 表示 JWT 令牌的过期时间，以毫秒为单位，即令牌的有效期限
    private long adminTtl;
    // 指定 JWT 令牌的名称，即在 HTTP 请求或响应中使用的令牌参数的名称
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
