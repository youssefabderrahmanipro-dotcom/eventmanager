package com.eventmanager.controller;

import com.eventmanager.dto.CommandeDTO;
import com.eventmanager.service.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class CommandeController {

    private final CommandeService s;

    public CommandeController(CommandeService s) {
        this.s = s;
    }

    @GetMapping
    public List<CommandeDTO> getAll(Authentication a) {
        return s.findAll(a.getName());
    }

    @PostMapping
    public ResponseEntity<CommandeDTO> create(@RequestBody CommandeDTO d, Authentication a) {
        CommandeDTO dto = s.create(d, a.getName());
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDTO> update(@PathVariable Long id, @RequestBody CommandeDTO d) {
        CommandeDTO dto = s.update(id, d);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CommandeDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!List.of("devis", "confirmé", "en_cours", "terminé", "annulé").contains(status)) {
            throw new IllegalArgumentException("Statut invalide");
        }
        CommandeDTO dto = s.updateStatus(id, status);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        s.delete(id);
        return ResponseEntity.noContent().build();
    }
}