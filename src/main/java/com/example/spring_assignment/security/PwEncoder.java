package com.example.spring_assignment.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public class PwEncoder {
    public static PasswordEncoder encoder = new BCryptPasswordEncoder();
}
