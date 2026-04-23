package com.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commande_prestation_details")
public class CommandePrestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long prestationId;

    private Long commandeId;

    private int quantite;
}