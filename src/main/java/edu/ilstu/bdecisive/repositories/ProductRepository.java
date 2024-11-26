package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    Optional<Product> findByName(String name);
    List<Product> findByUser(User user);
}
