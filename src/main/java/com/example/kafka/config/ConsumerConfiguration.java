package com.example.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.consumer")
@Data
public class ConsumerConfiguration {
    private String groupIdPrefix;
    private int maxFailure;
}
