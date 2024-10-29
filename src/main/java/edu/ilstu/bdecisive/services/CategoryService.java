package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void create(CategoryRequestDTO requestDTO) throws ServiceException;
    boolean approveOrRejectCategory(Long categoryId, boolean isApproved) throws ServiceException;
    Optional<Category> findByCategoryName(String categoryName);

    List<CategoryResponseDTO> list(Optional<String> name, Optional<String> description, Optional<Boolean> approved);
}


