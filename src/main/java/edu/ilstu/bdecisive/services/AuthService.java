package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.security.request.LoginRequest;
import edu.ilstu.bdecisive.security.response.LoginResponse;
import edu.ilstu.bdecisive.utils.ServiceException;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest) throws ServiceException;
    void verifyUser(VerifyUserDTO input) throws ServiceException;
    void resendVerificationCode(String email) throws ServiceException;
    void forgotPassword(String email) throws ServiceException;
    void resetPassword(String token, String newPassword) throws ServiceException;
}
