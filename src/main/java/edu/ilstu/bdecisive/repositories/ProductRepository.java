package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(Long productId);
    Optional<Product> findByProductName(String productName);
}
