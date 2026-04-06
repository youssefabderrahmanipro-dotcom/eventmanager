package com.eventmanager.dto;

import lombok.Data;

@Data
public class PrestataireDTO {

    private String id;

    private String nom;

    private String type;

    private String telephone;

    private String email;

    private String motDePasse;

    private String proprietaireId;
}