package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.dtos.InfluencerDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Influencer;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.InfluencerRepository;
import edu.ilstu.bdecisive.services.InfluencerService;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class InfluencerServiceImpl implements InfluencerService {

    @Autowired
    private UserService userService;

    @Autowired
    private InfluencerRepository influencerRepository;

    @Override
    public void create(InfluencerDTO influencerDTO) throws ServiceException {
        Optional<User> userByUsername = userService.findByUsername(influencerDTO.getUsername());
        if (userByUsername.isPresent()) {
            throw new ServiceException("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        Optional<User> userByEmail = userService.findByEmail(influencerDTO.getEmail());
        if (userByEmail.isPresent()) {
            throw new ServiceException("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        User user = new User(influencerDTO.getUsername(), influencerDTO.getEmail(), influencerDTO.getPassword());
        user = userService.registerUser(user, AppRole.ROLE_INFLUENCER, false);

        // Create influencer account
        Influencer influencer = new Influencer(user, influencerDTO.getAddress(), influencerDTO.getDescription());
        influencerRepository.save(influencer);
    }
}
