package edu.ilstu.bdecisive.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewDTO {
    private Long id;

    private ProductDTO product;

    private CategoryDTO category;

    private String name;

    private double rating;

    private String details;

    private int likeCount;

    private boolean isLikedByUser;

    private String createdAt;

    private List<CommentDTO> comments;
}
