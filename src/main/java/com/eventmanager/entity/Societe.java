package com.eventmanager.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "societe")
    private List<Utilisateur> utilisateurs;
}