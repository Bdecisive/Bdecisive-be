package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.UserDTO;
import edu.ilstu.bdecisive.dtos.UserResponseDTO;
import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.PasswordResetToken;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.PasswordResetTokenRepository;
import edu.ilstu.bdecisive.repositories.RoleRepository;
import edu.ilstu.bdecisive.repositories.UserRepository;
import edu.ilstu.bdecisive.security.services.UserDetailsImpl;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${base.url}")
    String frontendUrl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailService emailService;

    @Override
    public void updateUserRole(Long userId, String roleName) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new ServiceException("Role not found", HttpStatus.NOT_FOUND));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    private UserDTO convertToDto(User user) {
        UserDTO result = new UserDTO();
        result.setUserId(user.getUserId());
        result.setEmail(user.getEmail());
        result.setUserName(user.getUsername());
        result.setEnabled(user.isEnabled());
        result.setRole(user.getRole());
        result.setCreatedDate(user.getCreatedDate());
        result.setUpdatedDate(user.getUpdatedDate());
        return result;

    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            userOpt = findByEmail(username);
        }
        return userOpt;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateAccountLockStatus(Long userId, boolean lock) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }


    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expire) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabled) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expire) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
    }


    @Override
    public void updatePassword(Long userId, String password) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ServiceException("User not found", HttpStatus.NOT_FOUND));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to update password", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void generatePasswordResetToken(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
        passwordResetTokenRepository.save(resetToken);

//        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        // Send email to user
//        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }


    @Override
    public void resetPassword(String token, String newPassword) throws ServiceException {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ServiceException("Invalid password reset token", HttpStatus.NOT_FOUND));

        if (resetToken.isUsed())
            throw new ServiceException("Password reset token has already been used", HttpStatus.CONFLICT);

        if (resetToken.getExpiryDate().isBefore(Instant.now()))
            throw new ServiceException("Password reset token has expired", HttpStatus.BAD_REQUEST);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public User registerUser(User user, AppRole roleName, boolean isEnabled){
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(isEnabled);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setSignUpMethod("email");
        user.setVerificationCode(emailService.generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        // Check if role already exists in the database
        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        user.setRole(role);

        emailService.sendVerificationEmail(user);
        return userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findById(userDetails.getId());
            return user.orElseThrow(() -> new RuntimeException("User not found"));
        }
        return null;
    }

    @Override
    public UserResponseDTO getUserProfile() throws ServiceException {
        User user = getCurrentUser();
        if (user == null) {
            throw new ServiceException("Cannot find the user.", HttpStatus.NOT_FOUND);
        }
        Role role = user.getRole();
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                role.getRoleName().name()
        );
    }

    @Override
    public void updateUserProfile(Long userId, UserDTO userProfileDTO) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ServiceException("Cannot Find the user.", HttpStatus.NOT_FOUND));

            if (userProfileDTO.getUserName() != null) {
                user.setFirstName(userProfileDTO.getUserName());
            }
            if (userProfileDTO.getEmail() != null){
                user.setEmail(userProfileDTO.getEmail());
            }
//            if (userProfileDTO.getPhoneNumber() != null) {
//                user.setPhoneNumber(userProfileDTO.getPhoneNumber());
//            }
//            if (userProfileDTO.getPassword() != null) {
//                user.setPassword(passwordEncoder.encode(userProfileDTO.getPassword()));
//            }
//            if (userProfileDTO.getProfilePictureUrl() != null) {
//                user.setProfilePictureUrl(userProfileDTO.getProfilePictureUrl());
//            }

            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Fail to update user profile.", HttpStatus.BAD_REQUEST);
        }
    }


}