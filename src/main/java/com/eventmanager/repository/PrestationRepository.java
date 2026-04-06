package com.eventmanager.repository;

import com.eventmanager.entity.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestationRepository extends JpaRepository<Prestation, Long> {

    List<Prestation> findByProprietaireId(Long proprietaireId);

    List<Prestation> findByCommandesId(Long commandeId);

}