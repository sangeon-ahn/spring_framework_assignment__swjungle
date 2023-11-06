package com.example.spring_assignment.service;

import com.example.spring_assignment.dto.PostRequestDto;
import com.example.spring_assignment.dto.PostResponseDto;
import com.example.spring_assignment.entity.Post;
import com.example.spring_assignment.entity.User;
import com.example.spring_assignment.entity.UserRoleEnum;
import com.example.spring_assignment.security.JwtUtil;
import com.example.spring_assignment.repository.PostRepository;
import com.example.spring_assignment.repository.UserRepository;
import com.example.spring_assignment.security.PwEncoder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    Claims claims;

    @Transactional
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            postDtos.add(new PostResponseDto(post));
        }
        return postDtos;
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

        String encodedPassword = PwEncoder.encoder.encode(requestDto.getPassword());
        Post post = new Post(requestDto, encodedPassword, user.getUsername());
        post.setUser(user);

        postRepository.saveAndFlush(post);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 포스트입니다.")
        );

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 존재하지 않습니다.")
        );

        // role이 user면 검사
        if (user.getRole().equals(UserRoleEnum.USER)) {
            // 해당 포스트가 유저의 포스트가 아니면 예외처리
            if (!post.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }

            // 포스트 비밀번호와 전달받은 비밀번호가 다르면 예외처리
            if (!PwEncoder.encoder.matches(requestDto.getPassword(), post.getPassword())) {
                throw new IllegalArgumentException("입력한 포스트 비밀번호가 일치하지 않습니다.");
            }
        }

        // 포스트 업데이트
        // (@Transactional이 붙었기 때문에, 함수 종료시 자동으로 commit되면서 db에 sql 보냄)
        post.update(requestDto);

        return new PostResponseDto(post);
    }

    @Transactional
    public String deletePost(Long id, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 포스트입니다.")
        );

        // 권한이 user면 검사
        if (user.getRole().equals(UserRoleEnum.USER)) {
            // 해당 유저가 작성한 글인지 파악
            if (!user.getId().equals(post.getUser().getId())) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }
        }

        postRepository.delete(post);
        user.getPosts().remove(post);

        return "deletePost success";
    }
}
