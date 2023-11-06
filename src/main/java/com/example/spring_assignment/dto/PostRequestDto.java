package com.example.spring_assignment.dto;


import com.example.spring_assignment.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "게시글 제목 비어있음")
    private String title;

    @NotEmpty(message = "게시글 내용 비어있음")
    private String contents;

    @NotEmpty(message = "게시글 패스워드 비어있음")
    private String password;
}
