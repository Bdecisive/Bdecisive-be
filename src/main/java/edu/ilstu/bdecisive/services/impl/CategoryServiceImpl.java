package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.repositories.CategoryRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<CategoryResponseDTO> list(Optional<String> name, Optional<String> description) {
        Category categoryExample = new Category();
        name.ifPresent(categoryExample::setCategoryName);
        description.ifPresent(categoryExample::setCategoryDescription);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Ensures partial matching
                .withIgnoreCase() // Applies ignore case by default
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Category> example = Example.of(categoryExample, matcher);

        List<Category> categories = categoryRepository.findAll(example);
        return categories.stream()
                .map(category -> new CategoryResponseDTO(category.getCategoryID(),
                        category.getCategoryName(), category.getCategoryDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public void create(CategoryRequestDTO requestDTO)  throws ServiceException{

        Optional<Category> categoryByName = findByCategoryName(requestDTO.getCategoryName());

        //validate that the Category does not already exist
        if (categoryByName.isPresent()) {
            throw new ServiceException("Error: Category already exists", HttpStatus.BAD_REQUEST);
        }

        //create new category
        Category category = new Category(requestDTO.getCategoryName(), requestDTO.getCategoryDescription());
        category.setApproved(false);
        categoryRepository.save(category);
    }

    @Override
    public boolean approveCategory(Long categoryId) throws ServiceException {

        //make sure category request exists
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        //if it exists approve the category
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setApproved(true);
            categoryRepository.save(category);
            return true;
        }
        //if the category does not exist throw exception
        else {
            throw new ServiceException(
                    String.format("Category doesn't exist for id: %d", categoryId),
                    HttpStatus.NOT_FOUND);
        }
    }
}
