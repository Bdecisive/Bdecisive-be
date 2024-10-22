package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.models.Follower;
import edu.ilstu.bdecisive.services.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpAsFollower(@RequestBody Follower follower) {
        try {
            Follower registeredFollower = followerService.createFollower(follower);
            // Logic to send confirmation email can go here
            return new ResponseEntity<>(registeredFollower, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        try {
            String result = followerService.addComment(id, comment);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
