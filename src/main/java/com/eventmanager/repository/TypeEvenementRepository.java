package com.eventmanager.repository;

import com.eventmanager.entity.TypeEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TypeEvenementRepository.java
@Repository
public interface TypeEvenementRepository extends JpaRepository<TypeEvenement, Long> {}