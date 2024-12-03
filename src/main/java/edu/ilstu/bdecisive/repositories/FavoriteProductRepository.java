package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.FavoriteProduct;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    Optional<FavoriteProduct> findByUserAndProduct(User user, Product product);

    List<FavoriteProduct> findAllByUser(User user);
}