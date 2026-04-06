package com.eventmanager.service;

import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocietePrestataireService {

    private final UtilisateurRepository utilisateurRepository;

    public SocietePrestataireService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur ajouterPrestataire(Long societeId, Long prestataireId) {
        Utilisateur societe = utilisateurRepository.findById(societeId)
                .orElseThrow(() -> new RuntimeException("Société non trouvée"));
        Utilisateur prestataire = utilisateurRepository.findById(prestataireId)
                .orElseThrow(() -> new RuntimeException("Prestataire non trouvé"));

        if (!prestataire.getRole().equals("PROVIDER")) {
            throw new RuntimeException("L'utilisateur n'est pas un prestataire");
        }

        if (!societe.getPrestataires().contains(prestataire)) {
            societe.getPrestataires().add(prestataire);
            utilisateurRepository.save(societe);
        }

        return societe;
    }

    public List<Utilisateur> getPrestataires(Long societeId) {
        Utilisateur societe = utilisateurRepository.findById(societeId)
                .orElseThrow(() -> new RuntimeException("Société non trouvée"));
        return societe.getPrestataires();
    }

    public List<Utilisateur> getTousPrestataires() {
        return utilisateurRepository.findByRole("PROVIDER");
    }
}