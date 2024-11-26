package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void deleteByProduct(Product product) {
        List<Review> reviewList = reviewRepository.findByProduct(product);
        reviewRepository.deleteAll(reviewList);
    }

}
