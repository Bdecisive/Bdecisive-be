package edu.ilstu.bdecisive.dtos;

import edu.ilstu.bdecisive.models.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long categoryId;

    private Long userId;
}
