package com.example.spring_assignment.controller;


import com.example.spring_assignment.dto.CommentRequestDto;
import com.example.spring_assignment.dto.CommentResponseDto;
import com.example.spring_assignment.entity.Message;
import com.example.spring_assignment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성 API
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Message> createComment(@PathVariable Long postId, @RequestBody @Valid CommentRequestDto requestDto, HttpServletRequest request) {
        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, request);

        Message message = new Message();
        message.build(HttpStatus.OK.value(), "댓글 생성 성공", responseDto);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    // 2. 댓글 수정 API
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Message> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody @Valid CommentRequestDto requestDto, HttpServletRequest request) {
        CommentResponseDto responseDto = commentService.updateComment(postId, id, requestDto, request);

        Message message = new Message();
        message.build(HttpStatus.OK.value(), "댓글 수정 성공", responseDto);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }


    // 3. 댓글 삭제 API
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Message> deleteComment(@PathVariable Long postId, @PathVariable Long id, HttpServletRequest request) {
        String result = commentService.deleteComment(postId, id, request);

        Message message = new Message();
        message.build(HttpStatus.OK.value(), result);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 4. 댓글 전체 조회 API
    @GetMapping("/comments")
    public ResponseEntity<Message> getComments() {
        Message message = new Message();
        message.build(HttpStatus.OK.value(), "get comments success", commentService.getComments());

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
