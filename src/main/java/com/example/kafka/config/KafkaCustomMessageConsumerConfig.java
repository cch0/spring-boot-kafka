package com.example.kafka.config;

import com.example.kafka.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ConsumerFactory and ListenerContainerFactory configuration for "custom" topic case which expects message value can
 * be converted into POJO.
 */
@EnableKafka
@Configuration
@Slf4j
public class KafkaCustomMessageConsumerConfig {
    private static Random random = new Random(System.currentTimeMillis());

    @Autowired
    KafkaClusterConfiguration clusterConfiguration;

    @Autowired
    ConsumerConfiguration consumerConfiguration;

    @Bean(name = "consumerFactory")
    public ConsumerFactory<Object, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterConfiguration.getBootstrapServer());

        // for illustration purpose, using a random group id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfiguration.getGroupIdPrefix() + random.nextInt());

        // set the isolation level
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
            new ErrorHandlingDeserializer2(new StringDeserializer()));
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
        ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
        @Qualifier(value = "kafkaTemplate") KafkaTemplate<Object, Object> template) {

        ConsumerFactory<Object, Object> consumerFactory = consumerFactory();

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

        configurer.configure(factory, consumerFactory);

        factory.setConsumerFactory(consumerFactory);

        // error handling
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template),
            consumerConfiguration.getMaxFailure()));

        return factory;
    }

    @Bean
    public RecordMessageConverter converter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        // set the trusted package
        typeMapper.addTrustedPackages("com.example.kafka.model");

        Map<String, Class<?>> mappings = new HashMap<>();
        // "register" POJO class
        mappings.put("custom", CustomMessage.class);

        typeMapper.setIdClassMapping(mappings);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}
