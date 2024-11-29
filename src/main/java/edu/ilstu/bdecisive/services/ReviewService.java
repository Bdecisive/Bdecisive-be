package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ReviewDTO;
import edu.ilstu.bdecisive.dtos.ReviewRequestDTO;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;

public interface ReviewService {
    void deleteByProduct(Product product);

    void deleteByProductId(Long productId) throws ServiceException;

    List<ReviewDTO> getReviewsByProductId(Long productId) throws ServiceException;

    List<ReviewDTO> getReviewsByCategoryId(Long categoryId) throws ServiceException;

    List<ReviewDTO> getReviewsByUser(Long userId) throws ServiceException;

    void update(Long reviewId, ReviewRequestDTO requestDTO);

    void create(ReviewRequestDTO requestDTO) throws ServiceException;

    void delete(Long reviewId) throws ServiceException;
}
