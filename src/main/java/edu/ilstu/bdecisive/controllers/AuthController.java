package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.security.request.LoginRequest;
import edu.ilstu.bdecisive.security.response.LoginResponse;
import edu.ilstu.bdecisive.security.response.MessageResponse;
import edu.ilstu.bdecisive.services.AuthService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws ServiceException {
        LoginResponse response = authService.authenticateUser(loginRequest);

        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDto) throws ServiceException {
        try {
            authService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) throws ServiceException {
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws ServiceException {
        // TODO: Not functional. Need to fix service method
        authService.forgotPassword(email);
        return ResponseEntity.ok(new MessageResponse("Password reset email sent!"));
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                           @RequestParam String newPassword) throws ServiceException {
        // TODO: Not functional. Need to fix service method
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(new MessageResponse("Password reset successful"));
    }
}
