package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.FollowerRequestDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Follower;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.FollowerRepository;
import edu.ilstu.bdecisive.services.FollowerService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserService userService;

    @Override
    public Follower createFollower(FollowerRequestDTO requestDTO) throws ServiceException {
        validateUser(requestDTO);
        // Create new user's account
        User user = new User(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword());
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());

        user = userService.registerUser(user, AppRole.ROLE_FOLLOWER, false);

        // Create follower account
        Follower follower = new Follower();
        follower.setUser(user);

        return followerRepository.save(follower);
    }

    private void validateUser(FollowerRequestDTO requestDTO) throws ServiceException {
        Optional<User> userByUsername = userService.findByUsername(requestDTO.getUsername());
        if (userByUsername.isPresent()) {
            throw new ServiceException("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        Optional<User> userByEmail =  userService.findByEmail(requestDTO.getEmail());
        if (userByEmail.isPresent()) {
            throw new ServiceException("Email is already in use!", HttpStatus.BAD_REQUEST);
        }
    }
}
