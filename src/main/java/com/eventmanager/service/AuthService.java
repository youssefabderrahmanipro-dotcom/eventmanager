package com.eventmanager.service;

import com.eventmanager.dto.*;
import com.eventmanager.entity.PasswordResetToken;
import com.eventmanager.entity.Prestataire;
import com.eventmanager.entity.Societe;
import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.PasswordResetTokenRepository;
import com.eventmanager.repository.PrestataireRepository;
import com.eventmanager.repository.SocieteRepository;
import com.eventmanager.repository.UtilisateurRepository;
import com.eventmanager.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;
    private final AuthenticationManager auth;
    private final MapperService mapper;

    private final SocieteRepository societeRepo;
    private final PrestataireRepository prestataireRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Value("${app.reset-password.url}")
    private String resetPasswordUrl;
    @Value("${app.reset-password.expiration-minutes}")
    private int expirationMinutes;


    public AuthService(UtilisateurRepository repo, PasswordEncoder enc, JwtUtil jwt, AuthenticationManager auth, MapperService mapper, SocieteRepository societeRepo, PrestataireRepository prestataireRepo) {
        this.repo = repo;
        this.enc = enc;
        this.jwt = jwt;
        this.auth = auth;
        this.mapper = mapper;
        this.societeRepo = societeRepo;
        this.prestataireRepo = prestataireRepo;
    }

    public ReponseAuthentification login(AuthentificationRequete req) {
        auth.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getMotDePasse())
        );

        Utilisateur u = repo.findByEmail(req.getEmail())
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return mapper.toAuth(jwt.generate(u.getEmail()), u);
    }

    /*public ReponseAuthentification register(AuthentificationRequete req) {
        if (repo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email déjà utilisé");

        Utilisateur u = new Utilisateur();
        u.setNom(req.getNom());
        u.setEmail(req.getEmail());
        u.setMotDePasse(enc.encode(req.getMotDePasse()));
        u.setRole(req.getRole());
        u.setTelephone(req.getTelephone());

        repo.save(u);

        return mapper.toAuth(jwt.generate(u.getEmail()), u);
    }*/

    public ReponseAuthentification register(AuthentificationRequete req) {

        if (repo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email déjà utilisé");

        Utilisateur u = new Utilisateur();
        u.setNom(req.getNom());
        u.setEmail(req.getEmail());
        u.setMotDePasse(enc.encode(req.getMotDePasse()));
        u.setRole(req.getRole());
        u.setTelephone(req.getTelephone());

        // ✅ Sauvegarder utilisateur d'abord
        repo.save(u);

        // ✅ CAS 1 : COMPANY
        if ("company".equalsIgnoreCase(req.getRole())) {
            Societe societe = new Societe();
            societe.setNom(req.getNom());
            societe.setEmail(req.getEmail());
            societe.setTelephone(req.getTelephone());

            societeRepo.save(societe);

            // 🔗 liaison
            u.setSociete(societe);
            repo.save(u);
        }

        // ✅ CAS 2 : PROVIDER
        if ("provider".equalsIgnoreCase(req.getRole())) {
            Prestataire prestataire = new Prestataire();
            prestataire.setNom(req.getNom());
            prestataire.setEmail(req.getEmail());
            prestataire.setTelephone(req.getTelephone());

            prestataireRepo.save(prestataire);

            // 🔗 liaison
            u.setPrestataire(prestataire);
            repo.save(u);
        }

        return mapper.toAuth(jwt.generate(u.getEmail()), u);
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {
        // On vérifie si l'utilisateur existe
        // Si non, on ne révèle rien (sécurité)
        repo.findByEmail(email).ifPresent(user -> {
            // Supprimer les anciens tokens
            tokenRepository.deleteByEmail(email);
            // Générer un token unique
            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(expirationMinutes);
            // Sauvegarder le token
            tokenRepository.save(new PasswordResetToken(token, email, expiry));
            // Construire le lien et envoyer l'email
            String resetLink = resetPasswordUrl + "?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);
        });
    }
}