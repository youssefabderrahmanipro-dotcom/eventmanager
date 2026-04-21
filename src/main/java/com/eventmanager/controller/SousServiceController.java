package com.eventmanager.controller;

import com.eventmanager.dto.SousServiceDTO;
import com.eventmanager.service.SousServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-services")
@RequiredArgsConstructor
public class SousServiceController {

    private final SousServiceService service;

    @PostMapping
    public SousServiceDTO create(@RequestBody SousServiceDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<SousServiceDTO> getByPrestation(@PathVariable Long id) {
        return service.getByPrestation(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}