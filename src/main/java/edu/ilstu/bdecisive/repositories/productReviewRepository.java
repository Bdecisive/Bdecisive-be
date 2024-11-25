package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productReviewRepository  extends JpaRepository<Review, Long>{


}