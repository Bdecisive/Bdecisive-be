package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.GlobalCategoryDTO;
import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.CategoryRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    VendorService vendorService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<CategoryResponseDTO> list(Optional<String> name, Optional<String> description, Optional<Boolean> approved) throws ServiceException {
        Category categoryExample = new Category();

        // Populate example fields conditionally
        categoryExample.setApproved(false);
        name.ifPresent(categoryExample::setName);
        description.ifPresent(categoryExample::setDescription);
        approved.ifPresent(categoryExample::setApproved); // Set only if relevant

        // Configure matcher
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("approved");

        // Create example
        Example<Category> example = Example.of(categoryExample, matcher);

        // Fetch matching categories
        List<Category> categories = categoryRepository.findAll(example);

        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole().getRoleName() == AppRole.ROLE_VENDOR) {
            categories = categories.stream()
                    .filter(category -> category.isApproved() || category.getUser() == currentUser)
                    .toList();
        }

        return getCategoryResponseDTOS(categories);
    }

    @Override
    public List<CategoryResponseDTO> vendorCategoryList() {
        User currentUser = userService.getCurrentUser();
        List<Category> categories = categoryRepository.findByUser(currentUser);
        return getCategoryResponseDTOS(categories);
    }

    @Override
    public List<GlobalCategoryDTO> listGlobalCategory() {
        // Fetch matching categories
        List<Category> categories = categoryRepository.findByApproved(true);

        return categories.stream().map(category ->
                new GlobalCategoryDTO(category.getId(), category.getName())).collect(Collectors.toList());
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    private List<CategoryResponseDTO> getCategoryResponseDTOS(List<Category> categories) {
        List<CategoryResponseDTO> list = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
            categoryResponseDTO.setId(category.getId());
            categoryResponseDTO.setName(category.getName());
            categoryResponseDTO.setDescription(category.getDescription());
            categoryResponseDTO.setApproved(category.isApproved());
            categoryResponseDTO.setApprovedDate(String.valueOf(category.getApprovedDate()));
            categoryResponseDTO.setCreatedAt(String.valueOf(category.getCreatedAt()));

            User user = category.getUser();
            categoryResponseDTO.setVendorName(user.getFirstName() + " " + user.getLastName());
            if (user.getRole().getRoleName() == AppRole.ROLE_VENDOR) {
                VendorDTO vendor = vendorService.getVendorByUserId(user.getUserId());
                categoryResponseDTO.setCompanyName(vendor.getCompanyName());
            }
            list.add(categoryResponseDTO);
        }
        return list;
    }

    @Override
    public void create(CategoryRequestDTO requestDTO) throws ServiceException {

        Optional<Category> categoryByName = findByCategoryName(requestDTO.getName());

        //validate that the Category does not already exist
        if (categoryByName.isPresent()) {
            throw new ServiceException("Category name already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();

        //create new category
        Category category = new Category(requestDTO.getName(), requestDTO.getDescription());
        if (user.getRole().getRoleName() == AppRole.ROLE_ADMIN) {
            category.setApproved(true);
        } else {
            category.setApproved(false);
        }
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
            category.setApprovedDate(LocalDateTime.now());
            categoryRepository.save(category);
            User user = category.getUser();
            emailService.sendCategoryConfirmationEmail(user, category.getName(), isApproved);
            return true;
        }
        //if the category does not exist throw exception
        else {
            throw new ServiceException(String.format("Category doesn't exist for id: %d", categoryId), HttpStatus.NOT_FOUND);
        }
    }
}
