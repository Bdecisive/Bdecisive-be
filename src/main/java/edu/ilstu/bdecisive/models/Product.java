package edu.ilstu.bdecisive.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Column(name="ProductReview")
    private List<Review> reviews = new ArrayList<>();

     @ManyToOne
     @JoinColumn(name = "category_id", nullable = false)
     private Category category;




}
