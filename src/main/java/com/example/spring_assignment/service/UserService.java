package com.example.spring_assignment.service;

import com.example.spring_assignment.dto.LoginRequestDto;
import com.example.spring_assignment.dto.SignupRequestDto;
import com.example.spring_assignment.entity.User;
import com.example.spring_assignment.entity.UserRoleEnum;
import com.example.spring_assignment.security.JwtUtil;
import com.example.spring_assignment.repository.UserRepository;

import com.example.spring_assignment.security.PwEncoder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    public final UserRepository userRepository;
    public final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequestDto requestDto) {
        // 받은 username으로 이미 아이디가 존재하는지 확인
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String encodedPassword = PwEncoder.encoder.encode(password);

        UserRoleEnum role = requestDto.getRole();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        userRepository.save(new User(username, encodedPassword, role));

        return "signup success";
    }

    @Transactional
    public String login(LoginRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();


        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );

        if (!PwEncoder.encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // jwtToken 생성해서 헤더에 넣어주기
        String jwtToken = jwtUtil.createToken(username, user.getRole());
        response.addHeader("Authorization", jwtToken);

        return "login success";
    }
}
