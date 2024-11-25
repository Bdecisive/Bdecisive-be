package edu.ilstu.bdecisive.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    private String role;

    public UserResponseDTO(Long id, String username, String email, boolean enabled, LocalDate credentialsExpiryDate,
                           LocalDate accountExpiryDate, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.role = role;
    }
}
