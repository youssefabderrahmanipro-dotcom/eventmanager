package com.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private String nomClient;

    private String statut;

    private Double prixTotal;

    private String notes;

    private String dateCreation;

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;


    @ManyToMany
    @JoinTable(
            name = "commande_prestations",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    private List<Prestation> prestations;

    @ManyToMany
    @JoinTable(
            name = "commande_packs",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "pack_id")
    )
    private List<Pack> packs;
}