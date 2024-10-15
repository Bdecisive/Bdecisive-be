package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor/")
public class VendorController {

    @Autowired
    VendorService vendorService;

    @PostMapping("create")
    public ResponseEntity<?> createVendorRequest(@Valid @RequestBody VendorRequestDTO requestDTO) throws ServiceException {
        vendorService.create(requestDTO);
        return ResponseEntity.ok("Vendor request creted successfully");
    }
}
