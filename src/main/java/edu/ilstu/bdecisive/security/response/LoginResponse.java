package edu.ilstu.bdecisive.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private String token;
//    private String username;
//    private List<String> roles;

    public LoginResponse(String token) {
//        this.username = username;
//        this.roles = roles;
        this.token = token;
    }

}
