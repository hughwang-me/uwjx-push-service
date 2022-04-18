package com.uwjx.push.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunAppConfig {

    private Long appKey;
    private String accessKeyId;
    private String accessSecret;

}
