package com.eventmanager.controller;

import com.eventmanager.dto.CategorieDTO;
import com.eventmanager.service.CategorieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @PostMapping
    public CategorieDTO create(@RequestBody CategorieDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<CategorieDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CategorieDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/proprietaire/{id}")
    public List<CategorieDTO> getByProprietaire(@PathVariable Long id) {
        return service.getByProprietaire(id);
    }

    @PutMapping("/{id}")
    public CategorieDTO update(@PathVariable Long id, @RequestBody CategorieDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}