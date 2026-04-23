package com.eventmanager.service;

import com.eventmanager.dto.CommandeDTO;
import com.eventmanager.dto.CommandePrestationDTO;
import com.eventmanager.entity.*;
import com.eventmanager.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    private final CommandeRepository repo;
    private final UtilisateurRepository uRepo;
    private final EvenementRepository eRepo;
    private final PrestationRepository prestationRepo;
    private final PackRepository packRepo;
    private final MapperService mapper;
    private final CommandePrestationRepository commandePrestationRepository;

    public CommandeService(
            CommandeRepository r,
            UtilisateurRepository u,
            EvenementRepository e,
            PrestationRepository p,
            PackRepository packR,
            MapperService m, CommandePrestationRepository commandePrestationRepository
    ) {
        repo = r;
        uRepo = u;
        eRepo = e;
        prestationRepo = p;
        packRepo = packR;
        mapper = m;
        this.commandePrestationRepository = commandePrestationRepository;
    }

    public List<CommandeDTO> findAll(String email) {
        Utilisateur u = uRepo.findByEmail(email).orElseThrow();
        return repo.findByProprietaireId(u.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CommandeDTO create(CommandeDTO dto, String email) {

        Utilisateur u = uRepo.findByEmail(email).orElseThrow();

        Commande o = new Commande();
        o.setTitre(dto.getTitre());
        o.setNomClient(dto.getNomClient());
        o.setStatut(dto.getStatut() != null ? dto.getStatut() : "devis");
        o.setNotes(dto.getNotes());
        o.setDateCreation(LocalDate.now().toString());
        o.setProprietaire(u);

        if (dto.getEvenementId() != null && !dto.getEvenementId().isEmpty()) {
            o.setEvenement(
                    eRepo.findById(Long.valueOf(dto.getEvenementId())).orElse(null)
            );
        }

        if (dto.getPrestationIds() != null && !dto.getPrestationIds().isEmpty()) {
            List<Prestation> prestations = prestationRepo.findAllById(
                    dto.getPrestationIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            o.setPrestations(prestations);
        }

        if (dto.getPackIds() != null && !dto.getPackIds().isEmpty()) {
            List<Pack> packs = packRepo.findAllById(
                    dto.getPackIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            o.setPacks(packs);
        }

        double total = 0;

        if (o.getPrestations() != null) {
            total += o.getPrestations()
                    .stream()
                    .mapToDouble(Prestation::getPrix)
                    .sum();
        }

        if (o.getPacks() != null) {
            total += o.getPacks()
                    .stream()
                    .mapToDouble(Pack::getPrix)
                    .sum();
        }

        o.setPrixTotal(total);

        return mapper.toDto(repo.save(o));
    }

    public CommandeDTO update(Long id, CommandeDTO dto) {

        Commande o = repo.findById(id).orElseThrow();

        o.setTitre(dto.getTitre());
        o.setNomClient(dto.getNomClient());
        o.setStatut(dto.getStatut());
        o.setNotes(dto.getNotes());

        if (dto.getEvenementId() != null && !dto.getEvenementId().isEmpty()) {
            o.setEvenement(
                    eRepo.findById(Long.valueOf(dto.getEvenementId())).orElse(null)
            );
        }
        /*******************************************************************************/
        if (dto.getPrestationIds() != null) {
            List<Prestation> prestations = prestationRepo.findAllById(
                    dto.getPrestationIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            o.setPrestations(prestations);
            if (dto.getPrestationsWithQuantite() != null && !dto.getPrestationsWithQuantite().isEmpty()) {

                for (CommandePrestationDTO p : dto.getPrestationsWithQuantite()) {

                    CommandePrestation cp = new CommandePrestation();

                    cp.setCommandeId(o.getId());;

                    cp.setPrestationId(Long.valueOf(p.getPrestationId()));

                    cp.setQuantite(p.getQuantite());

                    commandePrestationRepository.save(cp);
                }
            }
        }

        if (dto.getPackIds() != null) {
            List<Pack> packs = packRepo.findAllById(
                    dto.getPackIds().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            o.setPacks(packs);
        }

        double total = 0;

        if (o.getPrestations() != null) {
            total += o.getPrestations().stream().mapToDouble(Prestation::getPrix).sum();
        }

        if (o.getPacks() != null) {
            total += o.getPacks().stream().mapToDouble(Pack::getPrix).sum();
        }

        o.setPrixTotal(total);

        return mapper.toDto(repo.save(o));
    }

    public CommandeDTO updateStatus(Long id, String status) {
        Commande o = repo.findById(id).orElseThrow();
        o.setStatut(status);
        return mapper.toDto(repo.save(o));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}