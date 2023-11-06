package com.example.spring_assignment.dto;

import com.example.spring_assignment.entity.Comment;
import com.example.spring_assignment.entity.Post;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;

    private String title;

    private String author;

    private String contents;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();

        for (Comment comment : post.getComments()) {
            comments.add(new CommentResponseDto(comment));
        }
    }
}
