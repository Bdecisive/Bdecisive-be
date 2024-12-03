package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendors/")
public class VendorController {

    @Autowired
    VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VendorDTO> list() {
        return vendorService.list();
    }

    @PostMapping("create")
    public ResponseEntity<?> createVendorRequest(@Valid @RequestBody VendorRequestDTO requestDTO) throws ServiceException {
        vendorService.create(requestDTO);
        return ResponseEntity.ok("Vendor request created successfully");
    }

    @PatchMapping("/{vendorId}/approve")
    public ResponseEntity<String> approveVendorAccount(@PathVariable Long vendorId) throws ServiceException {
        boolean isApproved = vendorService.approveVendorAccount(vendorId);
        if (isApproved) {
            return ResponseEntity.ok("Vendor account approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendor approval failed");
        }
    }

    @PatchMapping("/{vendorId}/reject")
    public ResponseEntity<String> rejectVendorAccount(@PathVariable Long vendorId) throws ServiceException {
        boolean isApproved = vendorService.rejectVendorAccount(vendorId);
        if (isApproved) {
            return ResponseEntity.ok("Vendor account approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendor approval failed");
        }
    }
}
