package edu.ilstu.bdecisive.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRequestDTO {

    private Long id;

    private String companyName;

    private String firstName;

    private String lastName;

    private String address;

    private String description;

    private String phone;

    @NotBlank
    @Size(max = 70)
    @Email
    private String email;

    @NotBlank
    @Size(max = 40)
    private String username;

    private String password;
}
