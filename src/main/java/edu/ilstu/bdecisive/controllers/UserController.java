package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.UserDTO;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.security.response.UserInfoResponse;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getUserService() {
        return "Admin Page";
    }

    @GetMapping("user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user = userOpt.get();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                roles
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }

    @PutMapping("/profile/update")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO userProfileDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
            User user = userOpt.orElseThrow(() -> new RuntimeException("Cannot find the user."));

            userService.updateUserProfile(user.getUserId(), userProfileDTO);
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (ServiceException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
