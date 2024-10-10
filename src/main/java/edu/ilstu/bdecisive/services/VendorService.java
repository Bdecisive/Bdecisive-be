package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.utils.ServiceException;

public interface VendorService {
    void create(VendorRequestDTO requestDTO) throws ServiceException;
}
