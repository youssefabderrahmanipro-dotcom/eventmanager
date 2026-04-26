package com.eventmanager.controller;

import com.eventmanager.dto.PrestationDTO;
import com.eventmanager.service.PrestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final PrestationService s;

    public ServiceController(PrestationService s) {
        this.s = s;
    }

    @GetMapping
    public List<PrestationDTO> getAll(Authentication a) {
        return s.findAll(a.getName());
    }

    @GetMapping("/all")
    public List<PrestationDTO> getAllPublic() {
        return s.findAllPublic();
    }

    @PostMapping
    public ResponseEntity<PrestationDTO> create(@RequestBody PrestationDTO d, Authentication a) {
        PrestationDTO dto = s.create(d, a.getName());
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestationDTO> update(@PathVariable Long id, @RequestBody PrestationDTO d) {
        PrestationDTO dto = s.update(id, d);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        s.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<PrestationDTO>> getByProvider(
            @PathVariable Long providerId,
            Authentication a) {
        return ResponseEntity.ok(s.findByProvider(providerId, a.getName()));
    }
}