package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.GlobalCategoryDTO;
import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.Review;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.ProductRepository;
import edu.ilstu.bdecisive.repositories.ReviewRepository;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.services.ReviewService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRepository ReviewRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    public void create(ProductRequestDTO requestDTO) throws ServiceException {
        User user = userService.findUserById(requestDTO.getUserId());
        Category category = categoryService.findCategoryById(requestDTO.getCategoryId());
        Product product = new Product(requestDTO.getId(),
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getPrice(), category, user);
        productRepository.save(product);
    }

    public void productReview(ProductReviewDTO requestDTO)throws ServiceException{
        Long productId = requestDTO.getID();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));
        Review review=new Review(requestDTO.getReviewid(),requestDTO.getProductName(),requestDTO.getPros(),requestDTO.getCons(), requestDTO.getPersonalExperince(), requestDTO.getRating(),product);
        productRepository.save(product);
        ReviewRepository.save(review);
    }

    public void productUpdate(Long productId, ProductRequestDTO requestDTO)throws ServiceException{
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));
        if(requestDTO.getName()!=null){
            product.setName(requestDTO.getName());
        }
        if(requestDTO.getDescription()!=null){
            System.out.println(requestDTO.getDescription());
            product.setDescription(requestDTO.getDescription());
            System.out.println(product.getDescription());
        }
        if(requestDTO.getPrice()!=null){
            product.setPrice(requestDTO.getPrice());
        }
        productRepository.save(product);
    }

    @Override
    public void delete(Long productId) throws ServiceException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));
        reviewService.deleteByProduct(product);
        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getProductsByVendor(long userId) {
        User user = userService.findUserById(userId);
        List<Product> products = productRepository.findByUser(user);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = convertToProductDto(product);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    private static ProductDTO convertToProductDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());

        GlobalCategoryDTO categoryDTO = new GlobalCategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        productDTO.setCategory(categoryDTO);
        productDTO.setCreatedAt(String.valueOf(product.getCreatedAt()));
        productDTO.setUpdatedAt(String.valueOf(product.getUpdatedAt()));
        return productDTO;
    }

    @Override
    public List<ProductDTO> getProductsByCategory(long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        List<Product> products = productRepository.findByCategory(category);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = convertToProductDto(product);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

}
