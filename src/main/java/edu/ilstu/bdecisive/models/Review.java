package edu.ilstu.bdecisive.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private double rating;

    @Column(name = "details", length = 10000)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonBackReference
    private List<ReviewLike> likes = new ArrayList<>();

    public boolean isLikedByUser(Long userId) {
        return likes.stream()
                .anyMatch(like -> like.getUser().getUserId().equals(userId));
    }

    public int getLikeCount() {
        return likes.size();
    }
}