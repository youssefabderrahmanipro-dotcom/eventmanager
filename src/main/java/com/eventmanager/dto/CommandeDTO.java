package com.eventmanager.dto;



import lombok.Data;

import java.util.List;

@Data
public class CommandeDTO {

    private String id;

    private String titre;

    private String nomClient;

    private List<String> prestationIds;

    private List<String> packIds;

    private String evenementId;

    private String statut;

    private Double prixTotal;

    private String proprietaireId;

    private String dateCreation;

    private String notes;
}