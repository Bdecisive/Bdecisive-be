package edu.ilstu.bdecisive.repositories;


import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByUser(User user);

    List<Category> findByApproved(boolean approved);
}

