package com.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "evenements")
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String type;
    private String date;
    private String dateFin;
    private String lieu;
    private String description;

    private String statut;
    private String nomClient;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostale;
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @JsonIgnore
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL)
    private List<Commande> commandes;
}