package edu.ilstu.bdecisive.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorDTO {
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String address;
    private String description;
    private String phone;
    private String email;
    private String username;
    private boolean isApproved;
    private String approvedDate;
    private String createdAt;
}
