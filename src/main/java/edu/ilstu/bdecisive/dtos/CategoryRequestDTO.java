package edu.ilstu.bdecisive.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    private Long categoryID;

    @NotEmpty
    private String categoryName;

    @NotEmpty
    private String categoryDescription;
}
