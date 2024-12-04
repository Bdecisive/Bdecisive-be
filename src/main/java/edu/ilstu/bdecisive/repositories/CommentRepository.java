package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Comment;
import edu.ilstu.bdecisive.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReview(Review review);
}
