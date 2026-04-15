package com.eventmanager.service;

import com.eventmanager.dto.EvenementDTO;
import com.eventmanager.entity.*;
import com.eventmanager.repository.*;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvennementService {
    private final EvenementRepository repo;
    private final UtilisateurRepository uRepo;
    private final MapperService mapper;

    public EvennementService(EvenementRepository r, UtilisateurRepository u, MapperService m) {
        repo = r;
        uRepo = u;
        mapper = m;
    }

    public List<EvenementDTO> findAll(String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();
        return repo.findByProprietaireId(u.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EvenementDTO create(EvenementDTO dto, String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();
        return mapper.toDto(repo.save(mapper.fromDto(dto, u)));
    }

    public EvenementDTO update(Long id, EvenementDTO dto) {
        Evenement e = repo.findById(id).orElseThrow();
        e.setTitre(dto.getTitre());
        e.setType(dto.getType());
        e.setDate(dto.getDate());
        e.setLieu(dto.getLieu());
        e.setDescription(dto.getDescription());
        e.setStatut(dto.getStatut());
        e.setNomClient(dto.getNomClient());
        e.setPrenom(dto.getPrenom());
        e.setEmail(dto.getEmail());
        e.setTelephone(dto.getTelephone());
        e.setAdresse(dto.getAdresse());
        e.setVille(dto.getVille());
        e.setCodePostale(dto.getCodePostale());
        return mapper.toDto(repo.save(e));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
