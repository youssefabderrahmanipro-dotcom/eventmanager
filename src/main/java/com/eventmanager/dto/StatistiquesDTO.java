package com.eventmanager.dto;

import lombok.Data;

@Data
public class StatistiquesDTO {

    private long totalEvenements;

    private long totalCommandes;

    private long totalPrestataires;

    private long totalPrestations;

    private double revenu;
}