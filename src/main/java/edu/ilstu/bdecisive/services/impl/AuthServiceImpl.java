package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.security.jwt.JwtUtils;
import edu.ilstu.bdecisive.security.request.LoginRequest;
import edu.ilstu.bdecisive.security.response.LoginResponse;
import edu.ilstu.bdecisive.security.services.UserDetailsImpl;
import edu.ilstu.bdecisive.services.AuthService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) throws ServiceException {
        validateUserVerification(loginRequest);

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            throw new ServiceException("Bad credentials", HttpStatus.NOT_FOUND);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        return new LoginResponse(jwtToken);
    }

    @Override
    public void verifyUser(VerifyUserDTO request) throws ServiceException {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.isEnabled()) {
                throw new ServiceException("Account is already verified", HttpStatus.ALREADY_REPORTED);
            }

            if (user.getVerificationCodeExpiresAt() == null ||
                    user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new ServiceException("Verification code has expired", HttpStatus.BAD_GATEWAY);
            }

            if (user.getVerificationCode().equals(request.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userService.save(user);
            } else {
                throw new ServiceException("Invalid verification code", HttpStatus.BAD_GATEWAY);
            }
        } else {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void resendVerificationCode(String email) throws ServiceException {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new ServiceException("Account is already verified", HttpStatus.ALREADY_REPORTED);
            }

            user.setVerificationCode(emailService.generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            emailService.sendVerificationEmail(user);
            userService.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void forgotPassword(String email) throws ServiceException {
        try {
            userService.generatePasswordResetToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) throws ServiceException {
        try {
            userService.resetPassword(token, newPassword);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateUserVerification(LoginRequest request) throws ServiceException {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new ServiceException("User account doesn't exists.", HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();

        if (!user.isEnabled()) {
            throw new ServiceException("Account not verified. Please verify your account.", HttpStatus.FORBIDDEN);
        }
    }
}
