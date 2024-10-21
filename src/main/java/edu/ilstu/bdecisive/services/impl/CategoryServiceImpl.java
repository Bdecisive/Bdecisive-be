package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.repositories.CategoryRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public void create(CategoryRequestDTO requestDTO)  throws ServiceException{

        Optional<Category> categoryByName = findByCategoryName(requestDTO.getCategoryName());

        if (categoryByName.isPresent()) {
            throw new ServiceException("Error: Category already exists", HttpStatus.BAD_REQUEST);
        }

        //create new category
        Category category = new Category(requestDTO.getCategoryName(), requestDTO.getCategoryDescription());
        categoryRepository.save(category);
    }
}
