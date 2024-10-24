package edu.ilstu.bdecisive.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long productId;

    @Column(name="productName")
    private String productName;

    @Column(name="productDes")
    private String productDescription;

    @Column(name="productPrice")
    private double productPrice;


}
