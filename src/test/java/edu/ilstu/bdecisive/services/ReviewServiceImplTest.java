package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.models.*;
import edu.ilstu.bdecisive.repositories.ReviewLikeRepository;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.impl.ReviewServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private Product product;
    private Category category;
    private User user;
    private ReviewRequestDTO reviewRequestDTO;
    private ReviewLike reviewLike;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new User("testUser", "test@example.com");
        user.setUserId(1L);

        category = new Category("Test Category", "Test Description");
        category.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        product.setCategory(category);
        product.setUser(user);

        review = new Review();
        review.setId(1L);
        review.setRating(4.5);
        review.setDetails("Great product!");
        review.setProduct(product);
        review.setCategory(category);
        review.setUser(user);

        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setId(1L);
        reviewRequestDTO.setProductId(1L);
        reviewRequestDTO.setCategoryId(1L);
        reviewRequestDTO.setRating(4.5);
        reviewRequestDTO.setDetails("Great product!");

        reviewLike = new ReviewLike();
        reviewLike.setId(1L);
        reviewLike.setReview(review);
        reviewLike.setUser(user);
    }

    @Test
    void create_ShouldCreateNewReview() throws ServiceException {
        // Arrange
        when(productService.findById(1L)).thenReturn(product);
        when(categoryService.findById(1L)).thenReturn(category);
        when(userService.getCurrentUser()).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        reviewService.create(reviewRequestDTO);

        // Assert
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(productService, times(1)).findById(1L);
        verify(categoryService, times(1)).findById(1L);
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    void delete_ShouldDeleteReview() throws ServiceException {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // Act
        reviewService.delete(1L);

        // Assert
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void delete_WhenReviewNotFound_ShouldThrowException() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> reviewService.delete(1L));
        assertEquals("Review not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getAllReviews_ShouldReturnAllReviews() {
        // Arrange
        List<Review> reviews = Arrays.asList(review);
        when(reviewRepository.findAll()).thenReturn(reviews);
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        List<ReviewDTO> result = reviewService.getAllReviews();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(review.getId(), result.get(0).getId());
        assertEquals(review.getRating(), result.get(0).getRating());
    }

    @Test
    void getReview_ShouldReturnReview() throws ServiceException {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        ReviewDTO result = reviewService.getReview(1L);

        // Assert
        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
        assertEquals(review.getRating(), result.getRating());
    }

    @Test
    void getReviewsByProductId_ShouldReturnReviews() throws ServiceException {
        // Arrange
        List<Review> reviews = Arrays.asList(review);
        when(productService.findById(1L)).thenReturn(product);
        when(reviewRepository.findByProduct(product)).thenReturn(reviews);
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        List<ReviewDTO> result = reviewService.getReviewsByProductId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(review.getId(), result.get(0).getId());
    }

    @Test
    void likeReview_ShouldAddLike() throws ServiceException {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByReviewAndUser(review, user))
                .thenReturn(Optional.empty());
        when(reviewLikeRepository.save(any(ReviewLike.class))).thenReturn(reviewLike);

        // Act
        ReviewDTO result = reviewService.likeReview(1L);

        // Assert
        assertNotNull(result);
        verify(reviewLikeRepository, times(1)).save(any(ReviewLike.class));
    }

    @Test
    void likeReview_WhenAlreadyLiked_ShouldThrowException() {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByReviewAndUser(review, user))
                .thenReturn(Optional.of(reviewLike));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> reviewService.likeReview(1L));
        assertEquals("Review already liked by user", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void unlikeReview_ShouldRemoveLike() throws ServiceException {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review))
                .thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByReviewAndUser(review, user))
                .thenReturn(Optional.of(reviewLike));

        // Act
        ReviewDTO result = reviewService.unlikeReview(1L);

        // Assert
        assertNotNull(result);
        verify(reviewLikeRepository, times(1)).delete(reviewLike);
        verify(reviewLikeRepository, times(1)).flush();
        verify(entityManager, times(1)).detach(review);
    }

    @Test
    void unlikeReview_WhenLikeNotFound_ShouldThrowException() {
        // Arrange
        when(userService.getCurrentUser()).thenReturn(user);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByReviewAndUser(review, user))
                .thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> reviewService.unlikeReview(1L));
        assertEquals("Like not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void update_ShouldUpdateReview() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        reviewService.update(1L, reviewRequestDTO);

        // Assert
        verify(reviewRepository, times(1)).save(review);
        assertEquals(reviewRequestDTO.getRating(), review.getRating());
        assertEquals(reviewRequestDTO.getDetails(), review.getDetails());
    }

    @Test
    void update_WhenReviewNotFound_ShouldThrowException() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reviewService.update(1L, reviewRequestDTO));
        assertEquals("Review not found with ID: 1", exception.getMessage());
    }
}
