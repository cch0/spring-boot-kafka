package com.example.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ConsumerFactory and ListenerContainerFactory configuration for "simple" topic case which expects message to be a plain string
 */
@EnableKafka
@Configuration
public class KafkaSimpleConsumerConfig {
    private static Random random = new Random(System.currentTimeMillis());

    @Autowired
    KafkaClusterConfiguration clusterConfiguration;

    @Autowired
    ConsumerConfiguration consumerConfiguration;

    @Bean(name = "consumerFactoryForString")
    public ConsumerFactory<String, String> consumerFactoryForString() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfiguration.getGroupIdPrefix() + random.nextInt());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "kafkaListenerContainerFactoryForString")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryForString() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForString());
        return factory;
    }
}
