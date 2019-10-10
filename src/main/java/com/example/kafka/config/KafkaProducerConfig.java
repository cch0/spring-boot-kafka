package com.example.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * ProducerFactory and KafkaTemplate configuration for both "simple" and "custom" cases.
 */
@Configuration
public class KafkaProducerConfig {
    @Autowired
    KafkaClusterConfiguration clusterConfiguration;

    @Bean(name = "producerFactoryForString")
    public ProducerFactory<String, String> producerFactoryForString() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, 1);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "kafkaTemplateForString")
    public KafkaTemplate<String, String> kafkaTemplateForString() {
        return new KafkaTemplate<>(producerFactoryForString());
    }

    @Bean(name = "producerFactory")
    public ProducerFactory<Object, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory<>(configProps);

        // enable transaction
        factory.setTransactionIdPrefix("tx-");

        return factory;
    }

    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
