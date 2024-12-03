package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void create(ProductRequestDTO requestDTO)  throws ServiceException;
    void productUpdate(Long productId, ProductRequestDTO requestDTO)throws ServiceException;
    void delete(Long productId) throws ServiceException;
    List<ProductDTO> getProductsByVendor(long vendorId) throws ServiceException;
    List<ProductDTO> getProductsByCategory(long categoryId) throws ServiceException;
    Product findById(Long productId) throws ServiceException;
}

