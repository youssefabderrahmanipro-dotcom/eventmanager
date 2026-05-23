package com.eventmanager.service;

import com.eventmanager.dto.*;
import com.eventmanager.entity.Prestataire;
import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    private final UtilisateurRepository uRepo;
    private final EvenementRepository eRepo;
    private final PrestationRepository sRepo;
    private final CommandeRepository oRepo;
    private final MapperService mapper;
    private final PrestataireRepository prestataireRepository;

    public UtilisateurService(UtilisateurRepository u, EvenementRepository e, PrestationRepository s, CommandeRepository o, MapperService m, PrestataireRepository prestataireRepository) {
        uRepo = u;
        eRepo = e;
        sRepo = s;
        oRepo = o;
        mapper = m;
        this.prestataireRepository = prestataireRepository;
    }

    public Utilisateur findByEmail(String email) {
        return uRepo.findByEmail(email).orElseThrow();
    }

    public List<PrestataireDTO> getProviders(String email) {
        Utilisateur u = findByEmail(email);
        return u.getPrestataires().stream().map(p -> mapper.toPrestataire(p, String.valueOf(u.getId()))).collect(Collectors.toList());
    }

    public PrestataireDTO addProvider(String email, Long providerId) {
        Utilisateur company = findByEmail(email);

        // ✅ récupérer prestataire existant
        Utilisateur provider = uRepo.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Prestataire non trouvé"));

        // ✅ sécurité : vérifier role
        if (!"provider".equals(provider.getRole())) {
            throw new RuntimeException("Cet utilisateur n'est pas un prestataire");
        }

        // ✅ éviter doublons
        if (!company.getPrestataires().contains(provider)) {
            company.getPrestataires().add(provider);
            uRepo.save(company);
        }

        return mapper.toPrestataire(provider, String.valueOf(company.getId()));
    }

    public void removeProvider(String email, Long providerId) {
        Utilisateur company = findByEmail(email);
        company.getPrestataires().removeIf(p -> p.getId().equals(providerId));
        uRepo.save(company);
    }

    public StatistiquesDTO getStats(String email) {
        Utilisateur u = findByEmail(email);
        StatistiquesDTO s = new StatistiquesDTO();
        s.setTotalEvenements(eRepo.findByProprietaireId(u.getId()).size());
        s.setTotalCommandes(oRepo.findByProprietaireId(u.getId()).size());
        s.setRevenu(oRepo.findByProprietaireId(u.getId()).stream().filter(o -> o.getPrixTotal() != null).mapToDouble(o -> o.getPrixTotal()).sum());
        if ("company".equals(u.getRole())) s.setTotalPrestataires(u.getPrestataires().size());
        if ("provider".equals(u.getRole())) s.setTotalPrestations(sRepo.findByProprietaireId(u.getId()).size());
        return s;
    }

    public List<PrestataireDTO> getAllProviders() {
        return uRepo.findByRole("provider")
                .stream()
                .map(u -> mapper.toPrestataire(u, null))
                .collect(Collectors.toList());
    }

    public Utilisateur updateUtilisateur(String email, UpdateUtilisateurDTO dto) {
        Utilisateur u = findByEmail(email);
        if (dto.getNom() != null && !dto.getNom().isBlank())
            u.setNom(dto.getNom());
        if (dto.getEmail() != null && !dto.getEmail().isBlank())
            u.setEmail(dto.getEmail());
        if (dto.getTelephone() != null && !dto.getTelephone().isBlank())
            u.setTelephone(dto.getTelephone());
        return uRepo .save(u); // ← vérifiez que ce champ existe dans votre service
    }

    public PrestataireDTO getProviderById(Long id) {
        Utilisateur u = uRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));
        return mapper.toPrestataire(u, null);
    }
}
