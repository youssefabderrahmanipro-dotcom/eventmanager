package com.eventmanager.controller;

import com.eventmanager.dto.*;
import com.eventmanager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService s;

    public AuthController(AuthService s) {
        this.s = s;
    }

    @PostMapping("/login")
    public ResponseEntity<ReponseAuthentification> login(@RequestBody AuthentificationRequete r) {
        return ResponseEntity.ok(s.login(r));
    }

    @PostMapping("/register")
    public ResponseEntity<ReponseAuthentification> register(@RequestBody AuthentificationRequete r) {
        return ResponseEntity.ok(s.register(r));
    }
}
