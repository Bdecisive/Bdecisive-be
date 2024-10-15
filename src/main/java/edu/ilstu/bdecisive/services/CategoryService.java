package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.models.Category;

import java.util.Optional;

public interface CategoryService {

    void create(CategoryRequestDTO requestDTO);

    Optional<Category> findByCategoryName(String categoryName);
}


