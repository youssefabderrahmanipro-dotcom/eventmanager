package com.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String motDePasse;

    private String telephone;

    private String role;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "societe_prestataires",
            joinColumns = @JoinColumn(name = "societe_id"),
            inverseJoinColumns  = @JoinColumn(name = "prestataire_id"))
    private List<Utilisateur> prestataires = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "prestataires")
    private List<Utilisateur> societes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "societe_id")
    private Societe societe;
}