package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.models.FavoriteProduct;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;

public interface FavoriteProductService {
    void addFavorite(Long userId, Long productId) throws ServiceException;

    void removeFavorite(Long userId, Long productId) throws ServiceException;

    List<FavoriteProduct> getFavoriteProducts(Long userId) throws ServiceException;
}
