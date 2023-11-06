package com.example.spring_assignment.controller;

import com.example.spring_assignment.dto.PostRequestDto;
import com.example.spring_assignment.entity.Message;
import com.example.spring_assignment.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    // 1. 전체 게시글 목록 조회 API
    @GetMapping("/posts")
    public ResponseEntity<Message> getPosts() {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), "get posts success", postService.getPosts());

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    // 2. 게시글 작성 API
    @PostMapping("/posts")
    public ResponseEntity<Message> createPost(@RequestBody @Valid PostRequestDto requestDto, HttpServletRequest request) {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), "createPost success", postService.createPost(requestDto, request));

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    // 3. 선택한 게시글 조회 API
    @GetMapping("posts/{id}")
    public ResponseEntity<Message> getPost(@PathVariable Long id) {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), "getPost success", postService.getPost(id));

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    // 4. 선택한 게시글 수정 API
    @PutMapping("posts/{id}")
    public ResponseEntity<Message> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequestDto requestDto, HttpServletRequest request) {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), "updatePost success", postService.updatePost(id, requestDto, request));

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    // 5. 선택한 게시글 삭제 API
    @DeleteMapping("posts/{id}")
    public ResponseEntity<Message> deletePost(@PathVariable Long id, HttpServletRequest request) {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), postService.deletePost(id, request));

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
