package edu.ilstu.bdecisive.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="productreviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = "reviewid")
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reviewid")
    private int reviewid;
    private String productName;
    private String pros;
    private String cons;
    private String personalExperince;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private Product product;

}
