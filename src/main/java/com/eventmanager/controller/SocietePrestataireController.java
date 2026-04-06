package com.eventmanager.controller;

import com.eventmanager.entity.Utilisateur;
import com.eventmanager.service.SocietePrestataireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/societe-prestataires")
public class SocietePrestataireController {

    private final SocietePrestataireService service;

    public SocietePrestataireController(SocietePrestataireService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> ajouterPrestataire(@RequestBody Map<String, Long> body) {
        Long societeId = body.get("societeId");
        Long prestataireId = body.get("prestataireId");

        try {
            Utilisateur societe = service.ajouterPrestataire(societeId, prestataireId);
            return ResponseEntity.ok(societe.getPrestataires());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{societeId}")
    public ResponseEntity<List<Utilisateur>> getPrestataires(@PathVariable Long societeId) {
        return ResponseEntity.ok(service.getPrestataires(societeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Utilisateur>> getTousPrestataires() {
        return ResponseEntity.ok(service.getTousPrestataires());
    }
}