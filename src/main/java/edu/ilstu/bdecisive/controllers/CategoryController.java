package edu.ilstu.bdecisive.controllers;


import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category/")

public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("create")
    public ResponseEntity<?> createCategoryRequest(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        categoryService.create(requestDTO);
        return ResponseEntity.ok("Category request created successfully");
    }
}
