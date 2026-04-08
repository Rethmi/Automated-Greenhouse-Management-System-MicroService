package com.agms.identity_service.controller;

import com.agms.identity_service.entity.UserCredential;
import com.agms.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

    @PostMapping("/login")
    public String getToken(@RequestBody UserCredential user) {
        return service.generateToken(user.getName());
    }
}