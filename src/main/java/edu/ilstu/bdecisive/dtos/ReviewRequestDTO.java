package edu.ilstu.bdecisive.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Long id;

    private Long productId;

    private Long categoryId;

    private double rating;

    private String details;
}
