package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.models.FavoriteProduct;
import edu.ilstu.bdecisive.services.FavoriteProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteProductController {

    @Autowired
    private FavoriteProductService favoriteProductService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            favoriteProductService.addFavorite(userId, productId);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            favoriteProductService.removeFavorite(userId, productId);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<FavoriteProduct>> getFavorites(@RequestParam Long userId) {
        try {
            List<FavoriteProduct> favorites = favoriteProductService.getFavoriteProducts(userId);
            return ResponseEntity.ok(favorites);
        } catch (ServiceException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }
}
