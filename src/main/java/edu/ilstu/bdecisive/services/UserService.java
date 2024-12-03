package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.UserDTO;
import edu.ilstu.bdecisive.dtos.UserResponseDTO;
import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void updateUserRole(Long userId, String roleName) throws ServiceException;

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    User findUserById(Long id) throws ServiceException;

    Optional<User> findByUsername(String username);

    void updateAccountLockStatus(Long userId, boolean lock) throws ServiceException;

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(Long userId, boolean expire) throws ServiceException;

    void updateAccountEnabledStatus(Long userId, boolean enabled) throws ServiceException;

    void updateCredentialsExpiryStatus(Long userId, boolean expire) throws ServiceException;

    void updatePassword(Long userId, String password) throws ServiceException;

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword) throws ServiceException;

    Optional<User> findByEmail(String email);

    User registerUser(User user, AppRole roleName, boolean isEnabled);

    void updateUserProfile(Long userId, UserDTO userProfileDTO) throws ServiceException;

    void save(User user);

    User getCurrentUser();

    UserResponseDTO getUserProfile() throws ServiceException;
}
