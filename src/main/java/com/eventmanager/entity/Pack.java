package com.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "packs")
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Double prix;
    private Double reduction;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @ManyToMany
    @JoinTable(
            name = "pack_prestations",
            joinColumns = @JoinColumn(name = "pack_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    private List<Prestation> prestations;

    // ✅ NEW
    @JsonIgnore
    @ManyToMany(mappedBy = "packs")
    private List<Commande> commandes;
}