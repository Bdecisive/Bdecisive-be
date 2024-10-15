package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import edu.ilstu.bdecisive.repositories.UserRepository;
import edu.ilstu.bdecisive.repositories.VendorRepository;
import edu.ilstu.bdecisive.security.response.MessageResponse;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public void create(VendorRequestDTO requestDTO) throws ServiceException {
        Optional<User> userByUsername = userService.findByUsername(requestDTO.getUsername());
        if (userByUsername.isPresent()) {
            throw new ServiceException("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        Optional<User> userByEmail =  userService.findByEmail(requestDTO.getEmail());
        if (userByEmail.isPresent()) {
            throw new ServiceException("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        User user = new User(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword());
        user = userService.registerUser(user, AppRole.ROLE_VENDOR, false);

        // Create vendor account
        Vendor vendor = new Vendor();
        vendor.setAddress(requestDTO.getAddress());
        vendor.setUser(user);
        vendor.setApproved(false);
        vendor.setDescription(requestDTO.getDescription());
        vendorRepository.save(vendor);
    }

    @Override
    public boolean approveVendorAccount(Long vendorId) {
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isPresent()) {
            Vendor vendor = optionalVendor.get();
            vendor.setIsApproved(true);
            vendorRepository.save(vendor);
            return true;
        } else {
            throw new ServiceException(
                    String.format("Vendor account doesn't exist for id: %d", vendorId),
                    HttpStatus.NOT_FOUND);
        }
    }

}
