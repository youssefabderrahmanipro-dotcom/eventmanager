package com.eventmanager.controller;

import com.eventmanager.dto.PackDTO;
import com.eventmanager.service.PackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController {

    private final PackService s;

    public PackController(PackService s) {
        this.s = s;
    }

    @GetMapping
    public List<PackDTO> getAll(Authentication a) {
        return s.findAll(a.getName());
    }

    @PostMapping
    public ResponseEntity<PackDTO> create(@RequestBody PackDTO d, Authentication a) {
        PackDTO dto = s.create(d, a.getName());
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackDTO> update(@PathVariable Long id, @RequestBody PackDTO d) {
        PackDTO dto = s.update(id, d);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        s.delete(id);
        return ResponseEntity.noContent().build();
    }
}