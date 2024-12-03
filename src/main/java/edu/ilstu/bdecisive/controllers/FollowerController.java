package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.FollowerRequestDTO;
import edu.ilstu.bdecisive.models.Follower;
import edu.ilstu.bdecisive.services.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/followers/")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @PostMapping("create")
    public ResponseEntity<?> signUpAsFollower(@RequestBody FollowerRequestDTO requestDTO) {
        try {
            Follower registeredFollower = followerService.createFollower(requestDTO);
            // Logic to send confirmation email can go here
            return new ResponseEntity<>(registeredFollower, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
