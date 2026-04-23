package com.eventmanager.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ClientType {
    PARTICULIER,
    PARTENAIRE;

    @JsonCreator
    public static ClientType fromString(String value) {
        return value == null ? null : ClientType.valueOf(value.toUpperCase());
    }
}