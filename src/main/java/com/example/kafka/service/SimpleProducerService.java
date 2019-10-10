package com.example.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Producer to sending message to "simple" topic.
 */
@Component
@Slf4j
public class SimpleProducerService {
    @Autowired
    @Qualifier(value = "kafkaTemplateForString")
    private KafkaTemplate kafkaTemplate;

    public void postMessage(final String message) {
        // compared to the other producer, no transaction is setup for sending the message
        ListenableFuture<SendResult<String, String>> listenableFuture =
                kafkaTemplate.send("simple", null, message);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("message sent, partition={}, offset={}", result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.warn("failed to send, message={}", message, throwable);
            }
        });

    }
}
