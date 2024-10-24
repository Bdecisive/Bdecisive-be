package edu.ilstu.bdecisive.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyUserDTO {
    @NotBlank
    @Size(max = 70)
    private String username;

    @NotBlank
    private String verificationCode;
}
