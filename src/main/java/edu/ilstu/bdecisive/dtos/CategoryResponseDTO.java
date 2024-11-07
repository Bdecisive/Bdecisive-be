package edu.ilstu.bdecisive.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private boolean approved;
}
