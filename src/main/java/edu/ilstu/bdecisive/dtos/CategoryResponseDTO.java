package edu.ilstu.bdecisive.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String vendorName;
    private String companyName;
    private String description;
    private boolean approved;
    private String approvedDate;
    private String createdAt;
}
