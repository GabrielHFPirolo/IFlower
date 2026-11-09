package com.iflower.controller;

import com.iflower.dto.AuthDTO;
import com.iflower.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthDTO.LoginResponse login(@Valid @RequestBody AuthDTO.LoginRequest req) {
        return authService.login(req);
    }
}
