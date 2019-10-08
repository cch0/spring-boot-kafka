package com.example.kafka.service;

import com.example.kafka.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class CustomMessageProducerService {
    @Autowired
    @Qualifier(value = "kafkaTemplateForPOJO")
    private KafkaTemplate kafkaTemplate;

    public void postMessage(final CustomMessage message) {
        ListenableFuture<SendResult<String, CustomMessage>> listenableFuture =
                kafkaTemplate.send("custom", null, message);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, CustomMessage>>() {

            @Override
            public void onSuccess(SendResult<String, CustomMessage> result) {
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
