package com.eventmanager.repository;

import com.eventmanager.entity.SousPrestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SousPrestationRepository extends JpaRepository<SousPrestation, Long> {
    List<SousPrestation> findByPrestationId(Long prestationId);
}