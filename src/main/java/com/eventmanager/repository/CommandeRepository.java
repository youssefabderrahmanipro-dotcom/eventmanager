package com.eventmanager.repository;

import com.eventmanager.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByProprietaireId(Long proprietaireId);

    List<Commande> findByPrestationsId(Long prestationId);

    List<Commande> findByPacksId(Long packId);

}