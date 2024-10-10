package edu.ilstu.bdecisive.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    private Long categoryID;

    private String categoryName;

    private String categoryDescription;

    private boolean isApproved;
}
