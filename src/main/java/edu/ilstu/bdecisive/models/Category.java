package edu.ilstu.bdecisive.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long categoryID;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;
    @JsonManagedReference
    @ToString.Exclude
    private boolean isApproved;

    public Category( String categoryName, String categoryDescription)
    {
        this.categoryDescription = categoryDescription;
        this.categoryName = categoryName;
    }
}
