package com.eventmanager.dto;

import lombok.Data;

import java.util.List;

@Data
public class PackDTO {

    private String id;

    private String nom;

    private String description;

    private List<String> prestationIds;

    private Double prix;

    private String proprietaireId;

    private Double reduction;
}