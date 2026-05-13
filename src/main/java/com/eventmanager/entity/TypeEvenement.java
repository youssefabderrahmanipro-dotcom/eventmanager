package com.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "type_evenement")
public class TypeEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    // ✅ Simple champ comme Categorie
    private Long proprietaireId;

    @JsonIgnore
    @OneToMany(mappedBy = "typeEvenement", cascade = CascadeType.ALL)
    private List<Evenement> evenements = new ArrayList<>();


    @JsonIgnore // ✅ casser la référence circulaire
    @OneToMany(mappedBy = "typeEvenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categorie> categories = new ArrayList<>();
}