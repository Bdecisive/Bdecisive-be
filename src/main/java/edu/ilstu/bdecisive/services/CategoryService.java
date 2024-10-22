package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.Optional;

public interface CategoryService {

    void create(CategoryRequestDTO requestDTO) throws ServiceException;
    Optional<Category> findByCategoryName(String categoryName);
}


