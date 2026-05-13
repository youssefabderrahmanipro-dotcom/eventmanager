package com.eventmanager.dto;

import com.eventmanager.entity.ClientType;
import lombok.Data;

import java.util.List;

@Data
public class EvenementDTO {

    private String id;
    // ← remplacer String type
    private Long typeEvenementId;
    private String typeEvenementLibelle; // ← pour l'affichage côté front
    private String titre;

    private String type;

    private String date;

    private String lieu;

    private String proprietaireId;

    private String statut;

    private String nomClient;
    private String prenom;

    private ClientType clientType;


    private List<String> prestationIds;
}