package edu.ilstu.bdecisive.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private String userName;
    private Long reviewId;
    private LocalDateTime createdAt;
}
