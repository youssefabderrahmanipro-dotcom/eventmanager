package com.eventmanager.service;

import com.eventmanager.dto.PrestationDTO;
import com.eventmanager.entity.Prestation;
import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.PrestationRepository;
import com.eventmanager.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestationService {

    private final PrestationRepository repo;
    private final UtilisateurRepository uRepo;
    private final MapperService mapper;

    public PrestationService(PrestationRepository r, UtilisateurRepository u, MapperService m) {
        repo = r;
        uRepo = u;
        mapper = m;
    }

    public List<PrestationDTO> findAll(String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();
        return repo.findByProprietaireId(u.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PrestationDTO> findAllPublic() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PrestationDTO create(PrestationDTO dto, String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();
        Prestation p = mapper.fromDto(dto, u); // mapper doit créer la prestation avec propriétaire
        return mapper.toDto(repo.save(p));
    }

    public PrestationDTO update(Long id, PrestationDTO dto) {
        Prestation p = repo.findById(id).orElseThrow();
        p.setNom(dto.getNom());
        p.setDescription(dto.getDescription());
        p.setPrix(dto.getPrix());
        p.setCategorie(dto.getCategorie());
        p.setStatut(dto.getStatut());
        return mapper.toDto(repo.save(p));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}