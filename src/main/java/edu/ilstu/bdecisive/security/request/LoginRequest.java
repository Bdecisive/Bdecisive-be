package edu.ilstu.bdecisive.security.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank
    @Size(max = 70)
    private String username;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;
}
