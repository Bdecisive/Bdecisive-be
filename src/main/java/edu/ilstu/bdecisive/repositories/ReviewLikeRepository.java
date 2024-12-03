package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.models.ReviewLike;
import edu.ilstu.bdecisive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewAndUser(Review review, User user);
    void deleteByReviewAndUser(Review review, User user);
    int countByReview(Review review);
}
