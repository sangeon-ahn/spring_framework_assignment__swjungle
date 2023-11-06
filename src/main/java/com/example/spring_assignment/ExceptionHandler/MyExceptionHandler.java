package com.example.spring_assignment.ExceptionHandler;

import com.example.spring_assignment.entity.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@ControllerAdvice
@RestController
public class MyExceptionHandler {

    // @Valid 를 제외한 모든 예외 처리 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> handleIllegalArgumentException(IllegalArgumentException ex) {
        Message message = new Message();
        message.build(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // @Valid 예외처리 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Message> handleValidationException(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패 예외 처리 로직
        Message message = new Message();
        message.build(HttpStatus.BAD_REQUEST.value(), ex.getFieldError().getDefaultMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
