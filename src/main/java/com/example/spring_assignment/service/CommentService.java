package com.example.spring_assignment.service;

import com.example.spring_assignment.dto.CommentRequestDto;
import com.example.spring_assignment.dto.CommentResponseDto;
import com.example.spring_assignment.entity.Comment;
import com.example.spring_assignment.entity.Post;
import com.example.spring_assignment.entity.User;
import com.example.spring_assignment.entity.UserRoleEnum;
import com.example.spring_assignment.security.JwtUtil;
import com.example.spring_assignment.repository.CommentRepository;
import com.example.spring_assignment.repository.PostRepository;
import com.example.spring_assignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    Claims claims;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 없습니다.")
        );

        Comment comment = new Comment(requestDto, user);
        comment.setPost(post);

        comment = commentRepository.saveAndFlush(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 없습니다.")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        // 해당 댓글이 해당 포스트에 없으면 예외 처리
        if (!post.getId().equals(comment.getPost().getId())) {
            throw new IllegalArgumentException("해당 댓글은 해당 포스트에 없습니다.");
        }

        if (user.getRole().equals(UserRoleEnum.USER)) {
            // 해당 댓글이 유저의 댓글이 아니면 예외 처리
            if (!comment.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }
        }

        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public String deleteComment(Long postId, Long id, HttpServletRequest request) {
        claims = jwtUtil.getClaims(request);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 없습니다.")
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        // 해당 댓글이 해당 포스트에 없으면 예외 처리
        if (!post.getId().equals(comment.getPost().getId())) {
            throw new IllegalArgumentException("해당 댓글은 해당 포스트에 없습니다.");
        }

        if (user.getRole().equals(UserRoleEnum.USER)) {
            // 해당 댓글이 유저의 댓글이 아니면 예외 처리
            if (!comment.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }
        }

        commentRepository.delete(comment);
        post.getComments().remove(comment);

        return "deleteComment success";
    }

    @Transactional
    public List<CommentResponseDto> getComments() {
        List<CommentResponseDto> list = new ArrayList<>();

        for (Comment comment : commentRepository.findAll()) {
            list.add(new CommentResponseDto(comment));
        }

        return list;
    }
}
