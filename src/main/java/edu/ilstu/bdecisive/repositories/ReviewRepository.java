package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByProduct(Product product);
}