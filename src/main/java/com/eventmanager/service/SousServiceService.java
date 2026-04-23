package com.eventmanager.service;

import com.eventmanager.dto.SousServiceDTO;
import com.eventmanager.entity.Prestation;
import com.eventmanager.entity.SousService;
import com.eventmanager.repository.PrestationRepository;
import com.eventmanager.repository.SousServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SousServiceService {

    private final SousServiceRepository repository;
    private final PrestationRepository prestationRepository;
    private final MapperService mapper;

    public SousServiceDTO create(SousServiceDTO dto) {
        Prestation prestation = prestationRepository.findById(Long.parseLong(dto.getPrestationId()))
                .orElseThrow(() -> new RuntimeException("Prestation not found"));

        SousService entity = mapper.fromDto(dto, prestation);
        return mapper.toDto(repository.save(entity));
    }

    public List<SousServiceDTO> getByPrestation(Long prestationId) {
        return repository.findByPrestationId(prestationId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<SousServiceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}