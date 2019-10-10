package com.example.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumer for receiving message from "simple" topic which expects message to be plain string.
 */
@Component
@Slf4j
public class SimpleConsumerService {

    @KafkaListener(topics = {"simple"}, containerFactory = "kafkaListenerContainerFactoryForString")
    public void listenWithHeaders(@Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.OFFSET) int offset) {
        log.info("message received, partition={}, offset={}, message={}", partition, offset, message);
    }
}
