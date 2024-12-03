package edu.ilstu.bdecisive.dtos;

import edu.ilstu.bdecisive.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO {

    private int reviewid;
    private String productName;
    private String pros;
    private String cons;
    private String personalExperince;
    private int rating;
    private Long ID;
}
