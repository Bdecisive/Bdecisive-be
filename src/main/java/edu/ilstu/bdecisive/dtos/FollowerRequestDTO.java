package edu.ilstu.bdecisive.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FollowerRequestDTO {
    private int id;

    private String firstName;

    private String lastName;

    @NotBlank
    @Size(max = 70)
    @Email
    private String email;

    @NotBlank
    @Size(max = 40)
    private String username;

    private String password;
}
