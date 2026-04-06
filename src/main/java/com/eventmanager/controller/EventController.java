package com.eventmanager.controller;

import com.eventmanager.dto.EvenementDTO;
import com.eventmanager.service.EvennementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EvennementService s;

    public EventController(EvennementService s) {
        this.s = s;
    }

    @GetMapping
    public List<EvenementDTO> getAll(Authentication a) {
        return s.findAll(a.getName());
    }

    @PostMapping
    public EvenementDTO create(@RequestBody EvenementDTO d, Authentication a) {
        System.out.println(d.getAdresse());
        return s.create(d, a.getName());
    }

    @PutMapping("/{id}")
    public EvenementDTO update(@PathVariable Long id, @RequestBody EvenementDTO d) {
        return s.update(id, d);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        s.delete(id);
        return ResponseEntity.noContent().build();
    }
}
