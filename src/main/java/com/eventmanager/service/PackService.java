package com.eventmanager.service;

import com.eventmanager.dto.PackDTO;
import com.eventmanager.entity.*;
import com.eventmanager.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackService {

    private final PackRepository repo;
    private final UtilisateurRepository uRepo;
    private final PrestationRepository prestationRepo;
    private final MapperService mapper;

    public PackService(PackRepository r, UtilisateurRepository u, PrestationRepository p, MapperService m) {
        repo = r;
        uRepo = u;
        prestationRepo = p;
        mapper = m;
    }

    public List<PackDTO> findAll(String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();

        return repo.findByProprietaireId(u.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PackDTO create(PackDTO dto, String email) {

        Utilisateur u = uRepo.findByEmail(email).orElseThrow();

        Pack p = new Pack();
        p.setNom(dto.getNom());
        p.setDescription(dto.getDescription());
        p.setPrix(dto.getPrix());
        p.setReduction(dto.getReduction());
        p.setProprietaire(u);

        if (dto.getPrestationIds() != null && !dto.getPrestationIds().isEmpty()) {

            List<Prestation> prestations = prestationRepo.findAllById(
                    dto.getPrestationIds()
                            .stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );

            p.setPrestations(prestations);
        }

        return mapper.toDto(repo.save(p));
    }

    public PackDTO update(Long id, PackDTO dto) {

        Pack p = repo.findById(id).orElseThrow();

        p.setNom(dto.getNom());
        p.setDescription(dto.getDescription());
        p.setPrix(dto.getPrix());
        p.setReduction(dto.getReduction());

        if (dto.getPrestationIds() != null) {

            List<Prestation> prestations = prestationRepo.findAllById(
                    dto.getPrestationIds()
                            .stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );

            p.setPrestations(prestations);
        }

        return mapper.toDto(repo.save(p));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}