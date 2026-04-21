package com.eventmanager.repository;

import com.eventmanager.entity.SousService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SousServiceRepository extends JpaRepository<SousService, Long> {

    List<SousService> findByPrestationId(Long prestationId);
}