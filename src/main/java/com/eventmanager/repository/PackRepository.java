package com.eventmanager.repository;

import com.eventmanager.entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackRepository extends JpaRepository<Pack, Long> {

    List<Pack> findByProprietaireId(Long proprietaireId);

    List<Pack> findByCommandesId(Long commandeId);
}