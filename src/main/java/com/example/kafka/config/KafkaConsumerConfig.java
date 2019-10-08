package com.example.kafka.config;

import com.example.kafka.model.CustomMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Autowired
    KafkaClusterConfiguration clusterConfiguration;

    @Autowired
    ConsumerConfiguration consumerConfiguration;

    private static Random random = new Random(System.currentTimeMillis());

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

    @Bean(name = "consumerFactoryForCustomMessage")
    public ConsumerFactory<String, CustomMessage> consumerFactoryForCustomMessage() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfiguration.getGroupIdPrefix() + random.nextInt());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
            new JsonDeserializer<>(CustomMessage.class));
    }

    @Bean(name = "kafkaListenerContainerFactoryForCustom")
    public ConcurrentKafkaListenerContainerFactory<String, CustomMessage> kafkaListenerContainerFactoryForCustom() {
        ConcurrentKafkaListenerContainerFactory<String, CustomMessage> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForCustomMessage());
        return factory;
    }

    @Bean(name = "consumerFactory")
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfiguration.getGroupIdPrefix() + random.nextInt());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
