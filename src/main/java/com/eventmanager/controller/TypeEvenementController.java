package com.eventmanager.controller;

import com.eventmanager.entity.TypeEvenement;
import com.eventmanager.repository.TypeEvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-evenements")
public class TypeEvenementController {

    @Autowired
    private TypeEvenementRepository repo;

    @GetMapping
    public List<TypeEvenement> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public TypeEvenement create(@RequestBody TypeEvenement type) {
        return repo.save(type);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}