package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import edu.ilstu.bdecisive.repositories.VendorRepository;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void create(VendorRequestDTO requestDTO) throws ServiceException {
        validateUser(requestDTO);

        // Create new user's account
        User user = new User(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword());
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user = userService.registerUser(user, AppRole.ROLE_VENDOR, false);

        // Create vendor account
        Vendor vendor = new Vendor();
        vendor.setCompanyName(requestDTO.getCompanyName());
        vendor.setAddress(requestDTO.getAddress());
        vendor.setUser(user);
        vendor.setApproved(false);
        vendor.setDescription(requestDTO.getDescription());
        vendor.setPhone(requestDTO.getPhone());
        vendorRepository.save(vendor);
    }

    private void validateUser(VendorRequestDTO requestDTO) throws ServiceException {
        Optional<User> userByUsername = userService.findByUsername(requestDTO.getUsername());
        if (userByUsername.isPresent()) {
            throw new ServiceException("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        Optional<User> userByEmail = userService.findByEmail(requestDTO.getEmail());
        if (userByEmail.isPresent()) {
            throw new ServiceException("Email is already in use!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean approveVendorAccount(Long vendorId) throws ServiceException {
        return updateVendorApproval(vendorId, true);
    }

    @Override
    public boolean rejectVendorAccount(Long vendorId) throws ServiceException {
        return updateVendorApproval(vendorId, false);
    }

    private boolean updateVendorApproval(Long vendorId, boolean approved) throws ServiceException {
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isPresent()) {
            Vendor vendor = optionalVendor.get();
            vendor.setApproved(approved);
            vendor.setApprovedDate(LocalDateTime.now());
            vendorRepository.save(vendor);

            User user = vendor.getUser();
            emailService.sendVendorStatusEmail(user, vendor.getCompanyName(), approved);
            return true;
        } else {
            throw new ServiceException(
                    String.format("Vendor account doesn't exist for id: %d", vendorId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean isApproved(User user) {
        Optional<Vendor> vendorOpt = vendorRepository.findByUser(user);
        if (vendorOpt.isPresent()) {
            Vendor vendor = vendorOpt.get();
            return vendor.isApproved();
        }
        return false;
    }

    @Override
    public List<VendorDTO> list() {
        return vendorRepository.findAll().stream()
                .map(vendor -> {
                    VendorDTO vendorDto = new VendorDTO();
                    vendorDto.setId(vendor.getId());
                    vendorDto.setCompanyName(vendor.getCompanyName());
                    vendorDto.setAddress(vendor.getAddress());
                    vendorDto.setDescription(vendor.getDescription());
                    vendorDto.setApproved(vendor.isApproved());
                    if (vendor.getApprovedDate() != null) {
                        vendorDto.setApprovedDate(vendor.getApprovedDate().toString());
                    }
                    vendorDto.setCreatedAt(vendor.getCreatedAt().toString());
                    vendorDto.setPhone(vendor.getPhone());

                    User user = vendor.getUser();
                    vendorDto.setFirstName(user.getFirstName());
                    vendorDto.setLastName(user.getLastName());
                    vendorDto.setEmail(user.getEmail());
                    vendorDto.setUsername(user.getUsername());
                    return vendorDto;
                })
                .collect(Collectors.toList());
    }
}
