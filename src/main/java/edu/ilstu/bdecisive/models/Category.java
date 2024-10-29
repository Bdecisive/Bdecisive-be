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
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category(String categoryName, String categoryDescription)
    {
        this.categoryDescription = categoryDescription;
        this.categoryName = categoryName;
    }
}
