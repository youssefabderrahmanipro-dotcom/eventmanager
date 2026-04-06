package com.eventmanager.service;

import com.eventmanager.dto.*;
import com.eventmanager.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapperService {

    // --------------------- AUTH ---------------------
    public ReponseAuthentification toAuth(String token, Utilisateur u) {
        return new ReponseAuthentification(
                token,
                String.valueOf(u.getId()),
                u.getNom(),
                u.getEmail(),
                u.getTelephone(),
                u.getRole()
        );
    }

    // --------------------- EVENEMENT ---------------------
    public EvenementDTO toDto(Evenement e) {
        EvenementDTO d = new EvenementDTO();
        d.setId(String.valueOf(e.getId()));
        d.setTitre(e.getTitre());
        d.setType(e.getType());
        d.setDate(e.getDate());
        d.setLieu(e.getLieu());
        d.setDescription(e.getDescription());
        d.setProprietaireId(e.getProprietaire() != null ? String.valueOf(e.getProprietaire().getId()) : "");
        d.setStatut(e.getStatut());
        d.setNomClient(e.getNomClient());
        d.setPrenom(e.getPrenom());
        d.setEmail(e.getEmail());
        d.setTelephone(e.getTelephone());
        d.setAdresse(e.getAdresse());
        d.setVille(e.getVille());
        d.setCodePostale(e.getCodePostale());
        d.setBudget(e.getBudget());
        d.setPrestationIds(new ArrayList<>());
        return d;
    }

    public Evenement fromDto(EvenementDTO d, Utilisateur proprietaire) {
        Evenement e = new Evenement();

        e.setTitre(d.getTitre());
        e.setType(d.getType());
        e.setDate(d.getDate());
        e.setLieu(d.getLieu());
        e.setDescription(d.getDescription());
        e.setStatut(d.getStatut() != null ? d.getStatut() : "à venir");

        e.setNomClient(d.getNomClient());
        e.setPrenom(d.getPrenom());
        e.setEmail(d.getEmail());
        e.setTelephone(d.getTelephone());
        e.setAdresse(d.getAdresse());
        e.setVille(d.getVille());
        e.setCodePostale(d.getCodePostale());

        e.setBudget(d.getBudget());
        e.setProprietaire(proprietaire);

        return e;
    }

    // --------------------- PRESTATION ---------------------
    public PrestationDTO toDto(Prestation s) {
        PrestationDTO d = new PrestationDTO();
        d.setId(String.valueOf(s.getId()));
        d.setNom(s.getNom());
        d.setDescription(s.getDescription());
        d.setPrix(s.getPrix());
        d.setCategorie(s.getCategorie());
        d.setStatut(s.getStatut());
        d.setProprietaireId(s.getProprietaire() != null ? String.valueOf(s.getProprietaire().getId()) : "");
        return d;
    }

    public Prestation fromDto(PrestationDTO d, Utilisateur proprietaire) {
        Prestation s = new Prestation();
        s.setNom(d.getNom());
        s.setDescription(d.getDescription());
        s.setPrix(d.getPrix());
        s.setCategorie(d.getCategorie());
        s.setStatut(d.getStatut() != null ? d.getStatut() : "actif");
        s.setProprietaire(proprietaire);
        return s;
    }

    // --------------------- PACK ---------------------
    public PackDTO toDto(Pack p) {
        PackDTO d = new PackDTO();
        d.setId(String.valueOf(p.getId()));
        d.setNom(p.getNom());
        d.setDescription(p.getDescription());
        d.setPrix(p.getPrix());
        d.setReduction(p.getReduction());
        d.setProprietaireId(p.getProprietaire() != null ? String.valueOf(p.getProprietaire().getId()) : "");
        d.setPrestationIds(
                p.getPrestations() != null ?
                        p.getPrestations().stream().map(s -> String.valueOf(s.getId())).collect(Collectors.toList()) :
                        new ArrayList<>()
        );
        return d;
    }

    public Pack fromDto(PackDTO d, Utilisateur proprietaire, List<Prestation> prestations) {
        Pack p = new Pack();
        p.setNom(d.getNom());
        p.setDescription(d.getDescription());
        p.setPrix(d.getPrix());
        p.setReduction(d.getReduction());
        p.setProprietaire(proprietaire);
        p.setPrestations(prestations != null ? prestations : new ArrayList<>());
        return p;
    }

    // --------------------- COMMANDE ---------------------
    public CommandeDTO toDto(Commande o) {
        CommandeDTO d = new CommandeDTO();
        d.setId(String.valueOf(o.getId()));
        d.setTitre(o.getTitre());
        d.setNomClient(o.getNomClient());
        d.setStatut(o.getStatut());
        d.setPrixTotal(o.getPrixTotal());
        d.setNotes(o.getNotes());
        d.setDateCreation(o.getDateCreation());
        d.setProprietaireId(o.getProprietaire() != null ? String.valueOf(o.getProprietaire().getId()) : "");
        d.setEvenementId(o.getEvenement() != null ? String.valueOf(o.getEvenement().getId()) : null);
        d.setPrestationIds(
                o.getPrestations() != null ?
                        o.getPrestations().stream().map(s -> String.valueOf(s.getId())).collect(Collectors.toList()) :
                        new ArrayList<>()
        );
        d.setPackIds(
                o.getPacks() != null ?
                        o.getPacks().stream().map(p -> String.valueOf(p.getId())).collect(Collectors.toList()) :
                        new ArrayList<>()
        );
        return d;
    }

    public Commande fromDto(
            CommandeDTO d,
            Utilisateur proprietaire,
            List<Prestation> prestations,
            List<Pack> packs,
            Evenement evenement
    ) {
        Commande c = new Commande();
        c.setTitre(d.getTitre());
        c.setNomClient(d.getNomClient());
        c.setStatut(d.getStatut() != null ? d.getStatut() : "devis");
        c.setPrixTotal(d.getPrixTotal());
        c.setNotes(d.getNotes());
        c.setDateCreation(d.getDateCreation() != null ? d.getDateCreation() : java.time.LocalDate.now().toString());
        c.setProprietaire(proprietaire);
        c.setEvenement(evenement);
        c.setPrestations(prestations != null ? prestations : new ArrayList<>());
        c.setPacks(packs != null ? packs : new ArrayList<>());
        return c;
    }

    // --------------------- PRESTATAIRE ---------------------
    public PrestataireDTO toPrestataire(Utilisateur u, String proprietaireId) {
        PrestataireDTO d = new PrestataireDTO();
        d.setId(String.valueOf(u.getId()));
        d.setNom(u.getNom());
        d.setType("prestataire");
        d.setTelephone(u.getTelephone());
        d.setEmail(u.getEmail());
        d.setMotDePasse(null);
        d.setProprietaireId(proprietaireId);
        return d;
    }
}