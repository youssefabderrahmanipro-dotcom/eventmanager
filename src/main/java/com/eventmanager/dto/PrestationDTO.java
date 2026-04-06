package com.eventmanager.dto;

import lombok.Data;

@Data
public class PrestationDTO {

    private String id;

    private String nom;

    private String description;

    private Double prix;

    private String categorie;

    private String statut;

    private String proprietaireId;
}