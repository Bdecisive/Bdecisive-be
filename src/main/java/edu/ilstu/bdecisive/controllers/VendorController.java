package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/vendor/")
public class VendorController {

    @Autowired
    VendorService vendorService;

    @PostMapping("create")
    public ResponseEntity<?> createVendorRequest(@Valid @RequestBody VendorRequestDTO requestDTO) throws ServiceException {
        vendorService.create(requestDTO);
        return ResponseEntity.ok("Vendor request created successfully");
    }

    @PostMapping("approve")
    public ResponseEntity<String> approveVendorAccount(@RequestParam Long vendorId) throws ServiceException {
        boolean isApproved = vendorService.approveVendorAccount(vendorId);
        if (isApproved) {
            return ResponseEntity.ok("Vendor account approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendor approval failed");
        }
    }

}
