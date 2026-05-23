package com.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "commandes")
public class Commande {

    // Enums internes
    public enum PaymentMethod {
        especes("Espèces"),
        carte("Carte bancaire"),
        virement("Virement bancaire"),
        cheque("Chèque");

        private String label;
        PaymentMethod(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public enum PaymentType {
        comptant("Comptant"),
        echelonne("Échelonné"),
        differe("Différé");

        private String label;
        PaymentType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public enum PaymentStatus {
        en_attente("En attente"),
        partiel("Partiel"),
        paye("Payé");

        private String label;
        PaymentStatus(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String nomClient;
    private String statut;
    private Double prixTotal;
    private String notes;
    private String dateCreation;

    @ManyToOne
    @JoinColumn(name = "prestataire_id")
    private Utilisateur prestataire;

    @Column(columnDefinition = "TEXT")
    private String prestataireIds;

    @Column(columnDefinition = "TEXT")
    private String pricingType;

    @Column(columnDefinition = "TEXT")
    private String quantities;

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @ManyToOne
    @JoinColumn(name = "package_partenaire_id")
    private PackagePartenaire packagePartenaire;

    @ManyToMany
    @JoinTable(name = "commande_prestations",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "prestation_id"))
    private List<Prestation> prestations;

    @ManyToMany
    @JoinTable(name = "commande_packs",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "pack_id"))
    private List<Pack> packs;

    @ManyToMany
    @JoinTable(name = "commande_sous_services",
            joinColumns = @JoinColumn(name = "commande_id"),
            inverseJoinColumns = @JoinColumn(name = "sous_service_id"))
    private List<SousService> sousServices;

    // Nouveaux champs pour le paiement
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDate paymentDueDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}