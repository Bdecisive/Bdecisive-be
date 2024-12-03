package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.services.ReviewService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO reviewDTO;
    private ReviewRequestDTO reviewRequestDTO;
    private List<ReviewDTO> reviewDTOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(4.5);
        reviewDTO.setDetails("Great product!");
        reviewDTO.setLikeCount(5);
        reviewDTO.setLikedByUser(true);

        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setId(1L);
        reviewRequestDTO.setProductId(1L);
        reviewRequestDTO.setCategoryId(1L);
        reviewRequestDTO.setRating(4.5);
        reviewRequestDTO.setDetails("Great product!");

        reviewDTOList = Arrays.asList(reviewDTO);
    }

    @Test
    void getAllReviews_ShouldReturnListOfReviews() {
        when(reviewService.getAllReviews()).thenReturn(reviewDTOList);
        
        ResponseEntity<List<ReviewDTO>> response = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTOList, response.getBody());
        verify(reviewService, times(1)).getAllReviews();
    }

    @Test
    void createReview_ShouldReturnSuccessMessage() throws ServiceException {
        doNothing().when(reviewService).create(any(ReviewRequestDTO.class));

        ResponseEntity<String> response = reviewController.create(reviewRequestDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Review created successfully!", response.getBody());
        verify(reviewService, times(1)).create(reviewRequestDTO);
    }

    @Test
    void getReview_ShouldReturnReview() throws ServiceException {
        when(reviewService.getReview(1L)).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.geReview(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
        verify(reviewService, times(1)).getReview(1L);
    }

    @Test
    void getReviewsByProduct_ShouldReturnListOfReviews() throws ServiceException {
        when(reviewService.getReviewsByProductId(1L)).thenReturn(reviewDTOList);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTOList, response.getBody());
        verify(reviewService, times(1)).getReviewsByProductId(1L);
    }

    @Test
    void getReviewsByCategory_ShouldReturnListOfReviews() throws ServiceException {
        when(reviewService.getReviewsByCategoryId(1L)).thenReturn(reviewDTOList);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTOList, response.getBody());
        verify(reviewService, times(1)).getReviewsByCategoryId(1L);
    }

    @Test
    void getReviewsByUser_ShouldReturnListOfReviews() throws ServiceException {
        when(reviewService.getReviewsByUser(1L)).thenReturn(reviewDTOList);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getReviewsByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTOList, response.getBody());
        verify(reviewService, times(1)).getReviewsByUser(1L);
    }

    @Test
    void updateReview_ShouldReturnSuccessMessage() {
        doNothing().when(reviewService).update(eq(1L), any(ReviewRequestDTO.class));

        ResponseEntity<String> response = reviewController.update(1L, reviewRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Review updated successfully!", response.getBody());
        verify(reviewService, times(1)).update(1L, reviewRequestDTO);
    }

    @Test
    void deleteReview_ShouldReturnSuccessMessage() throws ServiceException {
        doNothing().when(reviewService).delete(1L);

        ResponseEntity<?> response = reviewController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Review deleted successfully", response.getBody());
        verify(reviewService, times(1)).delete(1L);
    }

    @Test
    void deleteReview_WhenExceptionOccurs_ShouldReturnBadRequest() throws ServiceException {
        doThrow(new RuntimeException("Delete failed")).when(reviewService).delete(1L);

        ResponseEntity<?> response = reviewController.deleteProduct(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Delete failed", response.getBody());
        verify(reviewService, times(1)).delete(1L);
    }

    @Test
    void likeReview_ShouldReturnUpdatedReview() throws ServiceException {
        when(reviewService.likeReview(1L)).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.likeReview(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
        verify(reviewService, times(1)).likeReview(1L);
    }

    @Test
    void unlikeReview_ShouldReturnUpdatedReview() throws ServiceException {
        when(reviewService.unlikeReview(1L)).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.unlikeReview(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
        verify(reviewService, times(1)).unlikeReview(1L);
    }
}
