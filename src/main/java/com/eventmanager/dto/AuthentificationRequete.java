package com.eventmanager.dto;

import lombok.Data;

@Data
public class AuthentificationRequete {

    private String nom;

    private String email;

    private String motDePasse;

    private String telephone;

    private String role;
}