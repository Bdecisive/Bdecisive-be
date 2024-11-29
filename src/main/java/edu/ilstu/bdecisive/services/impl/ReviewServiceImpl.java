package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryDTO;
import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.services.ReviewService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CategoryService categoryService;

    @Lazy
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public void deleteByProduct(Product product) {
        List<Review> reviewList = reviewRepository.findByProduct(product);
        reviewRepository.deleteAll(reviewList);
    }

    @Override
    public void create(ReviewRequestDTO requestDTO) throws ServiceException {
        Product product = productService.findById(requestDTO.getProductId());
        Category category = categoryService.findById(requestDTO.getCategoryId());

        User user = userService.getCurrentUser();

        Review review = new Review();
        review.setRating(requestDTO.getRating());
        review.setDetails(requestDTO.getDetails());
        review.setProduct(product);
        review.setCategory(category);
        review.setUser(user);

        reviewRepository.save(review);
    }

    @Override
    public void delete(Long reviewId) throws ServiceException {
        Review product = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));
        reviewRepository.delete(product);
    }

    @Override
    public void deleteByProductId(Long productId) throws ServiceException {
        Product product = productService.findById(productId);
        List<Review> reviews = reviewRepository.findByProduct(product);
        reviewRepository.deleteAll(reviews);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) throws ServiceException {
        Product product = productService.findById(productId);
        List<Review> reviews = reviewRepository.findByProduct(product);
        ProductDTO productDto = convertToProductDto(product);
        CategoryDTO categoryDto = convetToCategoryDto(product.getCategory());
        return reviews.stream().map(review ->
                convertToReviewDto(review, productDto, categoryDto)).toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByCategoryId(Long categoryId) throws ServiceException {
        Category category = categoryService.findById(categoryId);
        List<Review> reviews = reviewRepository.findByCategory(category);
        CategoryDTO categoryDto = convetToCategoryDto(category);

        return reviews.stream().map(review ->
                convertToReviewDto(review, convertToProductDto(review.getProduct()), categoryDto)).toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Long userId) throws ServiceException {
        User user = userService.findUserById(userId);
        List<Review> reviews = reviewRepository.findByUser(user);

        return reviews.stream().map(review ->
                convertToReviewDto(review,
                        convertToProductDto(review.getProduct()),
                        convetToCategoryDto(review.getCategory())))
                .toList();
    }

    @Override
    public void update(Long reviewId, ReviewRequestDTO requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        // Optional: Update only the fields provided
        review.setRating(requestDTO.getRating());
        review.setDetails(requestDTO.getDetails());

        reviewRepository.save(review);
    }

    private ReviewDTO convertToReviewDto(Review review, ProductDTO productDto, CategoryDTO categoryDto) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProduct(productDto);
        dto.setCategory(categoryDto);
        dto.setRating(review.getRating());
        dto.setDetails(review.getDetails());
        dto.setCreatedAt(String.valueOf(review.getCreatedAt()));
        return dto;
    }

    private ProductDTO convertToProductDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        productDTO.setCategory(categoryDTO);
        productDTO.setCreatedAt(String.valueOf(product.getCreatedAt()));
        productDTO.setUpdatedAt(String.valueOf(product.getUpdatedAt()));
        return productDTO;
    }

    private CategoryDTO convetToCategoryDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
