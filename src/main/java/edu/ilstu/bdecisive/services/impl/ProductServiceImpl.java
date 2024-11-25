package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.ProductRequestDTO;

import edu.ilstu.bdecisive.dtos.ProductResponseDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.models.Product;

import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.repositories.ProductRepository;
import edu.ilstu.bdecisive.repositories.productReviewRepository;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    productReviewRepository productReviewRepository;
    public void create(ProductRequestDTO requestDTO) throws ServiceException {

        Optional<Product> productId = productRepository.findByProductId(requestDTO.getProductId());
        List<Review> review=new ArrayList<>();

        if (productId.isPresent()) {
            throw new ServiceException("Product ID is already taken", HttpStatus.BAD_REQUEST);
        }
        Product product=new Product(requestDTO.getProductId(),requestDTO.getProductName(),requestDTO.getProductDescription(), requestDTO.getProductPrice(), review,requestDTO.getC());
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
            return new ProductRequestDTO(p.getProductId(),p.getProductName(),p.getProductDescription(),p.getProductPrice(),p.getCategory());
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


    public void productReview(ProductReviewDTO requestDTO)throws ServiceException{
        Long productId = requestDTO.getID();
//        String productName = requestDTO.getProductName();
//        System.out.println(productId);
//        System.out.println(productName);// This will get the productId directly
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));
        Review review=new Review(requestDTO.getReviewid(),requestDTO.getProductName(),requestDTO.getPros(),requestDTO.getCons(), requestDTO.getPersonalExperince(), requestDTO.getRating(),product);
        product.getReviews().add(review);
        productRepository.save(product);
        productReviewRepository.save(review);
    }

    public void productUpdate(ProductRequestDTO requestDTO)throws ServiceException{
        Long productId = requestDTO.getProductId();
        System.out.println(productId);
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));
        if(requestDTO.getProductName()!=null){
            product.setProductName(requestDTO.getProductName());
        }
        if(requestDTO.getProductDescription()!=null){
            System.out.println(requestDTO.getProductDescription());
            product.setProductDescription(requestDTO.getProductDescription());
            System.out.println(product.getProductDescription());
        }
        if(requestDTO.getProductPrice()!=null){
            product.setProductPrice(requestDTO.getProductPrice());
        }
        productRepository.save(product);
        System.out.println(product.getProductDescription());
    }

}
