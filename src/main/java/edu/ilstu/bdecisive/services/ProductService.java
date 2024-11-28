package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;

public interface ProductService {
    void create(ProductRequestDTO requestDTO)  throws ServiceException;
    void productReview(ProductReviewDTO productReviewDTO)throws ServiceException;
    void productUpdate(Long productId, ProductRequestDTO requestDTO)throws ServiceException;
    void delete(Long productId) throws ServiceException;
    List<ProductDTO> getProductsByVendor(long vendorId);
    List<ProductDTO> getProductsByCategory(long categoryId);
}

