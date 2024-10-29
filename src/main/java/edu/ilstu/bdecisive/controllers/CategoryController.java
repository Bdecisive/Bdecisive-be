package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.services.CategoryService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories/")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> list(@RequestParam Optional<String> name,
                                     @RequestParam Optional<String> description) {
        return ResponseEntity.ok(categoryService.list(name, description));
    }

    @PostMapping("create")
    public ResponseEntity<?> createCategoryRequest(@Valid @RequestBody CategoryRequestDTO requestDTO) throws ServiceException {
        categoryService.create(requestDTO);
        return ResponseEntity.ok("Category request created successfully");
    }

    @PostMapping("approve")
    public ResponseEntity<String> approveCategory(@RequestParam Long categoryId) throws ServiceException {
        boolean isApproved = categoryService.approveCategory(categoryId);
        if (isApproved) {
            return ResponseEntity.ok("Category approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category approval failed");
        }
    }
}
