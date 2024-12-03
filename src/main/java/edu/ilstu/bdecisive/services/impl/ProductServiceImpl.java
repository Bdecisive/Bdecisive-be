package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.CategoryDTO;
import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.Product;
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
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    public void create(ProductRequestDTO requestDTO) throws ServiceException {
        User user = userService.findUserById(requestDTO.getUserId());
        Category category = categoryService.findById(requestDTO.getCategoryId());

        Product product = new Product(requestDTO.getId(),
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.getPrice(), category, user);
        productRepository.save(product);
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
    public List<ProductDTO> getProductsByVendor(long userId) throws ServiceException {
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

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(product.getCategory().getId());
        categoryDTO.setName(product.getCategory().getName());
        productDTO.setCategory(categoryDTO);
        productDTO.setCreatedAt(String.valueOf(product.getCreatedAt()));
        productDTO.setUpdatedAt(String.valueOf(product.getUpdatedAt()));
        return productDTO;
    }

    @Override
    public List<ProductDTO> getProductsByCategory(long categoryId) throws ServiceException {
        Category category = categoryService.findById(categoryId);
        List<Product> products = productRepository.findByCategory(category);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = convertToProductDto(product);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    @Override
    public Product findById(Long productId) throws ServiceException {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ServiceException("Product not found", HttpStatus.NOT_FOUND);
        }
        return productOpt.get();
    }

}
