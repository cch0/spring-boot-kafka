package com.example.kafka.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class CustomMessage {
    private String message;
    private Date date = new Date();
}
