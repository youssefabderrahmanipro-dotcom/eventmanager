package com.eventmanager.dto;



import lombok.Data;

import java.util.List;
import java.util.Map;

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
    private List<Long> sousServiceIds;
    private List<CommandePrestationDTO> prestationsWithQuantite;
    private Map<String, String> pricingType;
    private Map<String, Integer> quantities;  // ← ajouter
    private String prestataireId;
    private List<String> prestataireIds;

}