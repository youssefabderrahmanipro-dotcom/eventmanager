package com.eventmanager.dto;

import com.eventmanager.entity.ClientType;
import lombok.Data;

import java.util.List;

@Data
public class EvenementDTO {

    private String id;

    private String titre;

    private String type;

    private String date;

    private String lieu;

    private String description;

    private String proprietaireId;

    private String statut;

    private String nomClient;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostale;
    private ClientType clientType;


    private List<String> prestationIds;
}