package com.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Prestataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String email;

    private String telephone;

    @OneToMany(mappedBy = "prestataire")
    private List<Utilisateur> utilisateurs;
}
