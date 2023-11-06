package com.example.spring_assignment.entity;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@Data
public class Message {

    private int statusCode;
    private String message;
    private Object data;

    public Message() {
        this.statusCode = HttpStatus.OK.value();
        this.message = null;
        this.data = null;
    }

    // message 빌더 메서드
    public void build(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public void build(int statusCode, String message) {
        this.statusCode =statusCode;
        this.message = message;
    }
}

