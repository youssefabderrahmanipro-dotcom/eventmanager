package com.eventmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReponseAuthentification {

    private String token;

    private String id;

    private String nom;

    private String email;

    private String telephone;

    private String role;
}