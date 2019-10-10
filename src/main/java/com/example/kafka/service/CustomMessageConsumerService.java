package com.example.kafka.service;

import com.example.kafka.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumer for receiving message from "custom" topic which expects message to be converted into CustomMessage class.
 */
@Component
@Slf4j
public class CustomMessageConsumerService {

    @KafkaListener(topics = { "custom" }, containerFactory = "kafkaListenerContainerFactory")
    public void listenWithHeaders(@Payload CustomMessage message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.OFFSET) int offset) {

        log.info("message received, partition={}, offset={}, message={}", partition, offset, message);
    }

    /**
     * Called when number of failure exceeds configured maxFailure number.
     * Note that we are using String type to receive the message instead of POJO. This can be useful when
     * the failure is due to deserialization which Spring won't be able to construct POJO at all.
     * @param message
     */
    @KafkaListener(topics = "custom.DLT", containerFactory = "kafkaListenerContainerFactory")
    public void dltListen(final String message) {
        log.info("received from DeadLetterTopic, failed to consume message, message={}", message);
    }
}
