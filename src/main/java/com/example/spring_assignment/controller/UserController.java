package com.example.spring_assignment.controller;

import com.example.spring_assignment.dto.LoginRequestDto;
import com.example.spring_assignment.dto.SignupRequestDto;
import com.example.spring_assignment.entity.Message;
import com.example.spring_assignment.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // 1. 회원 가입 페이지 API
    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    // 2. 로그인 페이지 API
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    // 3. 회원 가입 API
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        String result = userService.signup(signupRequestDto);

        Message message = new Message();
        message.build(HttpStatus.OK.value(), result);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 4. 로그인 API
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String result = userService.login(loginRequestDto, response);

        Message message = new Message();
        message.build(HttpStatus.OK.value(), result);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }



}
