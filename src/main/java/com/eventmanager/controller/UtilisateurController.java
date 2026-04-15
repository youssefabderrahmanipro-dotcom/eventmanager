package com.eventmanager.controller;

import com.eventmanager.dto.*;
import com.eventmanager.entity.Utilisateur;
import com.eventmanager.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UtilisateurController {
    private final UtilisateurService s;

    public UtilisateurController(UtilisateurService s) {
        this.s = s;
    }

    @GetMapping("/me")
    public ResponseEntity<ReponseAuthentification> me(Authentication a) {
        Utilisateur u = s.findByEmail(a.getName());
        return ResponseEntity.ok(
                new ReponseAuthentification(
                        null, // pas besoin de token ici
                        String.valueOf(u.getId()),
                        u.getNom(),
                        u.getEmail(),
                        u.getTelephone(),
                        u.getRole()
                )
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<StatistiquesDTO> stats(Authentication a) {
        return ResponseEntity.ok(s.getStats(a.getName()));
    }

    @GetMapping("/providers")
    public ResponseEntity<List<PrestataireDTO>> providers(Authentication a) {
        return ResponseEntity.ok(s.getProviders(a.getName()));
    }

    @PostMapping("/providers/{providerId}")
    public ResponseEntity<PrestataireDTO> addProvider(
            @PathVariable Long providerId,
            Authentication a) {

        PrestataireDTO saved = s.addProvider(a.getName(), providerId);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/providers/{id}")
    public ResponseEntity<Void> removeProvider(@PathVariable Long id, Authentication a) {
        s.removeProvider(a.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-providers")
    public ResponseEntity<List<PrestataireDTO>> getAllProviders() {
        return ResponseEntity.ok(s.getAllProviders());
    }
}
