package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import edu.ilstu.bdecisive.utils.ServiceException;

import java.util.List;

public interface VendorService {
    void create(VendorRequestDTO requestDTO) throws ServiceException;
    boolean isApproved(User user);
    List<VendorDTO> list();
    boolean approveVendorAccount(Long vendorId) throws ServiceException;
    boolean rejectVendorAccount(Long vendorId) throws ServiceException;
    VendorDTO getVendorByUserId(Long userId) throws ServiceException;
}
