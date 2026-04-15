package com.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SousPrestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Double prix;

    @ManyToOne
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;
}