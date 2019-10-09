package com.example.kafka.controller;

import com.example.kafka.model.CustomMessage;
import com.example.kafka.service.CustomMessageProducerService;
import com.example.kafka.service.SimpleProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private SimpleProducerService simpleProducerService;

    @Autowired
    private CustomMessageProducerService customMessageProducerService;

    @PostMapping(value = "/message")
    public void postMessage(@RequestBody String message) {
        simpleProducerService.postMessage(message);
    }

    @PostMapping(value = "/custom-message", consumes = "application/json")
    public void postCustomMessage(@RequestBody CustomMessage message) {
        customMessageProducerService.postMessage(message);
    }
}
