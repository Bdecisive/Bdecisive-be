package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    public void create(ProductRequestDTO requestDTO)  throws ServiceException;
    public ProductRequestDTO getProductById(Long productId);
    public Optional<Product> findByProductId(Long productId);
    public Optional<Product> findByProductName(String productName);
    public void productReview(ProductReviewDTO productReviewDTO)throws ServiceException;
    public void productUpdate(ProductRequestDTO requestDTO)throws ServiceException;
}

