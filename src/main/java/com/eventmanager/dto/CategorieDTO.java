package com.eventmanager.dto;

import lombok.Data;

@Data
public class CategorieDTO {

    private Long id;
    private String name;
    private String description;
    private Long proprietaireId;

    // ✅ Ajouter ces deux champs
    private Long typeEvenementId;
    private String typeEvenementLibelle;


}