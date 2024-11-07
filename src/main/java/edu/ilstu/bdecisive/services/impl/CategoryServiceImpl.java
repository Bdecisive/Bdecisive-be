package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.CategoryRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.services.UserService;
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

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<CategoryResponseDTO> list(Optional<String> name, Optional<String> description, Optional<Boolean> approved) {
        Category categoryExample = new Category();
        name.ifPresent(categoryExample::setCategoryName);
        description.ifPresent(categoryExample::setCategoryDescription);
        approved.ifPresent(categoryExample::setApproved);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Ensures partial matching
                .withIgnoreCase() // Applies ignore case by default
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Category> example = Example.of(categoryExample, matcher);

        List<Category> categories = categoryRepository.findAll(example);
        return categories.stream()
                .map(category -> new CategoryResponseDTO(category.getId(), category.getCategoryName(),
                        category.getCategoryDescription(), category.isApproved()))
                .collect(Collectors.toList());
    }

    @Override
    public void create(CategoryRequestDTO requestDTO)  throws ServiceException{

        Optional<Category> categoryByName = findByCategoryName(requestDTO.getCategoryName());

        //validate that the Category does not already exist
        if (categoryByName.isPresent()) {
            throw new ServiceException("Error: Category already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();

        //create new category
        Category category = new Category(requestDTO.getCategoryName(), requestDTO.getCategoryDescription());
        category.setApproved(false);
        category.setUser(user);
        categoryRepository.save(category);
    }

    @Override
    public boolean approveOrRejectCategory(Long categoryId, boolean isApproved) throws ServiceException {
        //make sure category request exists
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        //if it exists approve the category
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setApproved(isApproved);
            categoryRepository.save(category);
            User user = category.getUser();
            emailService.sendCategoryConfirmationEmail(user, category.getCategoryName(), isApproved);
            return true;
        }
        //if the category does not exist throw exception
        else {
            throw new ServiceException(
                    String.format("Category doesn't exist for id: %d", categoryId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Category updateCategory(Long categoryId, Category updatedCategory) throws ServiceException {
        return categoryRepository.findById(categoryId).map(category -> {
                    category.setCategoryName(updatedCategory.getCategoryName());
                    category.setCategoryDescription(updatedCategory.getCategoryDescription());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new ServiceException(
                        String.format("Category doesn't exist for id: %d", categoryId),
                        HttpStatus.NOT_FOUND));
    }
    }




