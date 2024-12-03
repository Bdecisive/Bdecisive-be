package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryDTO;
import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.models.*;
import edu.ilstu.bdecisive.repositories.ReviewLikeRepository;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.services.ReviewService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CategoryService categoryService;

    @Lazy
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private EntityManager entityManager;

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
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        User currentUser = userService.getCurrentUser();
        return reviews.stream().map(review -> mapToDTO(review, currentUser)).toList();
    }

    @Transactional
    @Override
    public ReviewDTO getReview(Long reviewId) throws ServiceException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        User currentUser = userService.getCurrentUser();
        return mapToDTO(review, currentUser);
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
        User currentUser = userService.getCurrentUser();
        return reviews.stream().map(review ->
                convertToReviewDto(review, productDto, categoryDto, currentUser.getUserId())).toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByCategoryId(Long categoryId) throws ServiceException {
        Category category = categoryService.findById(categoryId);
        List<Review> reviews = reviewRepository.findByCategory(category);
        CategoryDTO categoryDto = convetToCategoryDto(category);
        User currentUser = userService.getCurrentUser();
        return reviews.stream().map(review ->
                convertToReviewDto(review, convertToProductDto(review.getProduct()),
                        categoryDto, currentUser.getUserId()))
                .toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Long userId) throws ServiceException {
        User user = userService.findUserById(userId);
        List<Review> reviews = reviewRepository.findByUser(user);

        User currentUser = userService.getCurrentUser();

        return reviews.stream().map(review ->
                mapToDTO(review, currentUser))
                .toList();
    }

    @Override
    public void update(Long reviewId, ReviewRequestDTO requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        review.setRating(requestDTO.getRating());
        review.setDetails(requestDTO.getDetails());

        reviewRepository.save(review);
    }

    @Override
    public ReviewDTO likeReview(Long reviewId) throws ServiceException {
        User currentUser = userService.getCurrentUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        Optional<ReviewLike> existingLike = reviewLikeRepository
                .findByReviewAndUser(review, currentUser);

        if (existingLike.isPresent()) {
            throw new ServiceException("Review already liked by user", HttpStatus.BAD_REQUEST);
        }

        ReviewLike like = new ReviewLike();
        like.setReview(review);
        like.setUser(currentUser);
        reviewLikeRepository.save(like);

        return mapToDTO(review, currentUser);
    }

    @Override
    @Transactional
    public ReviewDTO unlikeReview(Long reviewId) throws ServiceException {
        User currentUser = userService.getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        ReviewLike like = reviewLikeRepository
                .findByReviewAndUser(review, currentUser)
                .orElseThrow(() -> new ServiceException("Like not found", HttpStatus.NOT_FOUND));

        // Detach the review to avoid cascading issues
        entityManager.detach(review);

        // Delete the like
        reviewLikeRepository.delete(like);
        reviewLikeRepository.flush();  // Force the delete to be executed

        // Refresh the review to get updated like count
        review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        return mapToDTO(review, currentUser);
    }

    private ReviewDTO mapToDTO(Review review, User currentUser) {
        Long currentUserId = null;
        if (currentUser != null) {
            currentUserId = currentUser.getUserId();
        }
        return convertToReviewDto(review,
                convertToProductDto(review.getProduct()),
                convetToCategoryDto(review.getCategory()), currentUserId);
    }

    private ReviewDTO convertToReviewDto(
            Review review, ProductDTO productDto, CategoryDTO categoryDto, Long currentUserId) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProduct(productDto);
        dto.setCategory(categoryDto);
        dto.setRating(review.getRating());
        dto.setDetails(review.getDetails());
        dto.setCreatedAt(String.valueOf(review.getCreatedAt()));
        dto.setLikeCount(review.getLikeCount());

        if (currentUserId != null) {
            dto.setLikedByUser(review.isLikedByUser(currentUserId));
        }
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
