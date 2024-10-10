package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VendorRequestDTO;

public interface VendorService {
    void create(VendorRequestDTO requestDTO);
    boolean approveVendorAccount(Long vendorId);
}
