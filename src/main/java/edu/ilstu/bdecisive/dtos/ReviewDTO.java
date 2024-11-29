package edu.ilstu.bdecisive.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;

    private ProductDTO product;

    private CategoryDTO category;

    private double rating;

    private String details;

    private String createdAt;
}
