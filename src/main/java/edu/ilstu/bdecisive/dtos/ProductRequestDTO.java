package edu.ilstu.bdecisive.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private Long productId;

    private String productName;

    private String productDescription;

    private Double productPrice;
}
