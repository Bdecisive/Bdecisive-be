package edu.ilstu.bdecisive.services.impl;

import edu.ilstu.bdecisive.models.Follower;
import edu.ilstu.bdecisive.repositories.FollowerRepository;
import edu.ilstu.bdecisive.repositories.RoleRepository;
import edu.ilstu.bdecisive.services.FollowerService;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Follower createFollower(Follower follower) {
        if (follower.getPassword() != null) {
            follower.setPassword(passwordEncoder.encode(follower.getPassword()));
        }

        follower.setAccountNonLocked(true);
        follower.setAccountNonExpired(true);
        follower.setCredentialsNonExpired(true);
        follower.setEnabled(true);
        follower.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        follower.setAccountExpiryDate(LocalDate.now().plusYears(1));
        follower.setSignUpMethod("email");

        Role role = roleRepository.findByRoleName(AppRole.ROLE_FOLLOWER)
                .orElseThrow(() -> new RuntimeException("Role Follower not found"));

        follower.setRole(role);

        return followerRepository.save(follower);
    }

    @Override
    public String addComment(Long followerId, String comment) {
        Follower follower = (Follower) followerRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        return follower.addComment(comment);
    }
}
