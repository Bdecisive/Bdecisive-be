package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.models.FavoriteProduct;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.FavoriteProductRepository;
import edu.ilstu.bdecisive.repositories.ProductRepository;
import edu.ilstu.bdecisive.repositories.UserRepository;
import edu.ilstu.bdecisive.services.FavoriteProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addFavorite(Long userId, Long productId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));

        // Check if the product is already in favorites
        if (favoriteProductRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new ServiceException("Product is already in favorites", HttpStatus.BAD_REQUEST);
        }

        else {
            // Add product to favorites
            FavoriteProduct favoriteProduct = new FavoriteProduct();
            favoriteProduct.setUser(user);
            favoriteProduct.setProduct(product);
            favoriteProductRepository.save(favoriteProduct);
        }
    }

    @Override
    public void removeFavorite(Long userId, Long productId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException("Product not found", HttpStatus.NOT_FOUND));

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ServiceException("Favorite not found", HttpStatus.NOT_FOUND));

        favoriteProductRepository.delete(favoriteProduct);
    }

    @Override
    public List<FavoriteProduct> getFavoriteProducts(Long userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        return favoriteProductRepository.findAllByUser(user);
    }
}
