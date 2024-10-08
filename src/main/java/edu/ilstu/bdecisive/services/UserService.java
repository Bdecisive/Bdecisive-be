package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.UserDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    Optional<User> findByUsername(String username);

    void updateAccountLockStatus(Long userId, boolean lock);

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(Long userId, boolean expire);

    void updateAccountEnabledStatus(Long userId, boolean enabled);

    void updateCredentialsExpiryStatus(Long userId, boolean expire);

    void updatePassword(Long userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    Optional<User> findByEmail(String email);

    User registerUser(User user, AppRole roleName, boolean isEnabled);
}
