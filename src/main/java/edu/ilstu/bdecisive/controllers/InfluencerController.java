package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.InfluencerDTO;
import edu.ilstu.bdecisive.services.InfluencerService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/influencers/")
public class InfluencerController {

   @Autowired
    InfluencerService influencerService;

    @PostMapping("create")
    public ResponseEntity<?> createInfluencer(@Valid @RequestBody InfluencerDTO influencerDTO) throws ServiceException {
        influencerService.create(influencerDTO);
        return ResponseEntity.ok("Influencer account created successfully");
    }
}
