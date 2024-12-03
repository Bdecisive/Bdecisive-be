package edu.ilstu.bdecisive.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorite_products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id"}) // Ensures a product is favorited only once per user
})
public class FavoriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // References the User model (Influencer, Vendor, Follower)

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // References the Product model

}
