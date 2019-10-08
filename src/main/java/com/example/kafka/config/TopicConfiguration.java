package com.example.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
@Data
public class TopicConfiguration {
    String name;
    int numberOfPartitions;
    int numberOfReplicas;
}
