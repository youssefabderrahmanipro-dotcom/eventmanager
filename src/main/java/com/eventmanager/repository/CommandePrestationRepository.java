package com.eventmanager.repository;

import com.eventmanager.entity.CommandePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandePrestationRepository extends JpaRepository<CommandePrestation, Long> {

}