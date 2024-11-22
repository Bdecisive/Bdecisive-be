package edu.ilstu.bdecisive.dtos;

import edu.ilstu.bdecisive.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private boolean enabled;
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    private String signUpMethod;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String phoneNumber;
    private String password;
    private String profilePictureUrl;

    public UserDTO(Long userId, String username, String email, boolean enabled, LocalDate credentialsExpiryDate, LocalDate accountExpiryDate, String signUpMethod, Role role, LocalDateTime createdDate, LocalDateTime updatedDate) {
    }
}
