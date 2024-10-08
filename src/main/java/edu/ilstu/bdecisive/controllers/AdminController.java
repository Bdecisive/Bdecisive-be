package edu.ilstu.bdecisive.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String home() {
        return "Admin home";
    }
}
