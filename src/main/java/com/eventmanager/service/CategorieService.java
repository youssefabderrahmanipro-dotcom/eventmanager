package com.eventmanager.service;

import com.eventmanager.dto.CategorieDTO;
import com.eventmanager.entity.Categorie;
import com.eventmanager.entity.TypeEvenement;
import com.eventmanager.repository.CategorieRepository;

import com.eventmanager.repository.TypeEvenementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    private final CategorieRepository repository;
    private final TypeEvenementRepository typeEvenementRepository; // ✅ Ajouter

    public CategorieService(CategorieRepository repository, TypeEvenementRepository typeEvenementRepository) {
        this.repository = repository;
        this.typeEvenementRepository = typeEvenementRepository;
    }


    public CategorieDTO create(CategorieDTO dto) {
        Categorie categorie = mapToEntity(dto);

        // ✅ Récupérer et associer le TypeEvenement si présent
        if (dto.getTypeEvenementId() != null) {
            TypeEvenement typeEvenement = typeEvenementRepository.findById(dto.getTypeEvenementId())
                    .orElseThrow(() -> new RuntimeException("Type d'événement non trouvé"));
            categorie.setTypeEvenement(typeEvenement);
        }

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

    // ✅ Modifier update
    public CategorieDTO update(Long id, CategorieDTO dto) {
        Categorie categorie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));

        categorie.setName(dto.getName());
        categorie.setDescription(dto.getDescription());

        // ✅ Mettre à jour le TypeEvenement si nécessaire
        if (dto.getTypeEvenementId() != null) {
            TypeEvenement typeEvenement = typeEvenementRepository.findById(dto.getTypeEvenementId())
                    .orElseThrow(() -> new RuntimeException("Type d'événement non trouvé"));
            categorie.setTypeEvenement(typeEvenement);
        } else {
            categorie.setTypeEvenement(null);
        }

        return mapToDTO(repository.save(categorie));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ✅ Modifier mapToDTO pour inclure TypeEvenement
    private CategorieDTO mapToDTO(Categorie categorie) {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(categorie.getId());
        dto.setName(categorie.getName());
        dto.setDescription(categorie.getDescription());
        dto.setProprietaireId(categorie.getProprietaireId());

        // ✅ Ajouter les infos du TypeEvenement
        if (categorie.getTypeEvenement() != null) {
            dto.setTypeEvenementId(categorie.getTypeEvenement().getId());
            dto.setTypeEvenementLibelle(categorie.getTypeEvenement().getLibelle());
        }

        return dto;
    }

    // ✅ Modifier mapToEntity (sans associer TypeEvenement ici)
    private Categorie mapToEntity(CategorieDTO dto) {
        Categorie categorie = new Categorie();
        categorie.setId(dto.getId());
        categorie.setName(dto.getName());
        categorie.setDescription(dto.getDescription());
        categorie.setProprietaireId(dto.getProprietaireId());
        // Le TypeEvenement sera associé dans la méthode create
        return categorie;
    }
}