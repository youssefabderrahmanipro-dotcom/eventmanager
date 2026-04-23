package com.eventmanager.dto;

import lombok.Data;

@Data
public class CommandePrestationDTO {

    private String prestationId;
    private Long commandeId;
    private int quantite;
}