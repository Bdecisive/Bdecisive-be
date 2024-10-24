package edu.ilstu.bdecisive.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRequestDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String description;




    @NotBlank
    @Size(max = 70)
    @Email
    private String email;

    @NotBlank
    @Size(max = 40)
    private String username;

    private String password;
}
