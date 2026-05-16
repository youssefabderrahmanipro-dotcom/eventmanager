package com.eventmanager.controller;

import com.eventmanager.dto.*;
import com.eventmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService s;

    public AuthController(AuthService s)
    {
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

    /*@PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody MotDePasseOublieRequete r) {
        s.forgotPassword(r.getEmail());
        return ResponseEntity.ok(
                Map.of("message", "Si cet email existe, un lien a été envoyé.")
        );
    }*/
}
