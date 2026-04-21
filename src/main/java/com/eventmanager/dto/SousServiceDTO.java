package com.eventmanager.dto;

import lombok.Data;

@Data
public class SousServiceDTO {

    private String id;
    private String nom;
    private String description;
    private Double prix;
    private String prestationId;
}