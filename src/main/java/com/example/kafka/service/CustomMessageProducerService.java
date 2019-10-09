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
    @Qualifier(value = "kafkaTemplate")
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public void postMessage(final CustomMessage message) {

        this.kafkaTemplate.executeInTransaction(kafkaTemplate -> {
            ListenableFuture<SendResult<Object, Object>> listenableFuture =
                kafkaTemplate.send("custom", null, message);

            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    log.info("message sent, partition={}, offset={}", result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable throwable) {
                    log.warn("failed to send, message={}", message, throwable);
                }
            });

            return null;
        });
    }
}
