package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.ProductRequestDTO;

import edu.ilstu.bdecisive.models.Product;

import edu.ilstu.bdecisive.repositories.ProductRepository;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    public void create(ProductRequestDTO requestDTO) throws ServiceException {
        Optional<Product> productId = productRepository.findByProductId(requestDTO.getProductId());
        if (productId.isPresent()) {
            throw new ServiceException("Product ID is already taken", HttpStatus.BAD_REQUEST);
        }
        Product product=new Product(requestDTO.getProductId(),requestDTO.getProductName(),requestDTO.getProductDescription(),requestDTO.getProductPrice());
        productRepository.save(product);
    }
//    public List<Product> getAllProducts(){
//        return productRepository.findAll();
//    }

    @Override
    public ProductRequestDTO getProductById(Long id) {
        Optional<Product> product = productRepository.findByProductId(id);

        if(product.isPresent()) {
            Product p = product.get();
            return new ProductRequestDTO(p.getProductId(),p.getProductName(),p.getProductDescription(),p.getProductPrice());
        }
        else{
            return null;
        }
    }
    public Optional<Product> findByProductName(String productName){
        return productRepository.findByProductName(productName);
    }
    public Optional<Product> findByProductId(Long id){
        return productRepository.findByProductId(id);
    }


}
