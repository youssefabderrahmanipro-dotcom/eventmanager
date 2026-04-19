package com.eventmanager.service;

import com.eventmanager.dto.CategorieDTO;
import com.eventmanager.entity.Categorie;
import com.eventmanager.repository.CategorieRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    private final CategorieRepository repository;

    public CategorieService(CategorieRepository repository) {
        this.repository = repository;
    }


    public CategorieDTO create(CategorieDTO dto) {
        Categorie categorie = mapToEntity(dto);
        return mapToDTO(repository.save(categorie));
    }


    public List<CategorieDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public CategorieDTO getById(Long id) {
        Categorie categorie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));
        return mapToDTO(categorie);
    }


    public List<CategorieDTO> getByProprietaire(Long proprietaireId) {
        return repository.findByProprietaireId(proprietaireId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public CategorieDTO update(Long id, CategorieDTO dto) {
        Categorie categorie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));

        categorie.setName(dto.getName());
        categorie.setDescription(dto.getDescription());
       // categorie.setProprietaireId(dto.getProprietaireId());

        return mapToDTO(repository.save(categorie));
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 🔁 Mapper
    private CategorieDTO mapToDTO(Categorie categorie) {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(categorie.getId());
        dto.setName(categorie.getName());
        dto.setDescription(categorie.getDescription());
        dto.setProprietaireId(categorie.getProprietaireId());
        return dto;
    }

    private Categorie mapToEntity(CategorieDTO dto) {
        Categorie categorie = new Categorie();
        categorie.setId(dto.getId());
        categorie.setName(dto.getName());
        categorie.setDescription(dto.getDescription());
        categorie.setProprietaireId(dto.getProprietaireId());
        return categorie;
    }
}