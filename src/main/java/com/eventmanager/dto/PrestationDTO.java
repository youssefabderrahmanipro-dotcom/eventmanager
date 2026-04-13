package com.eventmanager.dto;

import lombok.Data;

@Data
public class PrestationDTO {
    private String id;
    private String nom;
    private String description;
    private double prix;
    private String statut;

    private Long categorieId;
    private String categorieNom;

    private String proprietaireId;
}