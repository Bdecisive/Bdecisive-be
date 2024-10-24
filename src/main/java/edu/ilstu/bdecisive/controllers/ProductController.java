package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("create")
    public ResponseEntity<?>  createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) throws ServiceException {
        productService.create(requestDTO);
        return ResponseEntity.ok("Product created successfully");
    }
}
