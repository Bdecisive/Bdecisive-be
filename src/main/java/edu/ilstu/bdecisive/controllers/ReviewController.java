package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.services.ReviewService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("create")
    public ResponseEntity<String> create(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO) throws ServiceException {
        reviewService.create(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review created successfully!");
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDTO> geReview(@PathVariable Long reviewId) throws ServiceException {
        ReviewDTO review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId) throws ServiceException {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCategory(@PathVariable Long categoryId) throws ServiceException {
        List<ReviewDTO> reviews = reviewService.getReviewsByCategoryId(categoryId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable Long userId) throws ServiceException {
        List<ReviewDTO> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("{reviewId}/update")
    public ResponseEntity<String> update(@PathVariable Long reviewId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.update(reviewId, reviewRequestDTO);
        return ResponseEntity.ok("Review updated successfully!");
    }

    @DeleteMapping("{reviewId}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long reviewId) throws ServiceException {
        try {
            reviewService.delete(reviewId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("{reviewId}/like")
    public ResponseEntity<ReviewDTO> likeReview(@PathVariable Long reviewId) throws ServiceException {
        ReviewDTO review = reviewService.likeReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping("{reviewId}/unlike")
    public ResponseEntity<ReviewDTO> unlikeReview(@PathVariable Long reviewId) throws ServiceException {
        ReviewDTO review = reviewService.unlikeReview(reviewId);
        return ResponseEntity.ok(review);
    }
}
