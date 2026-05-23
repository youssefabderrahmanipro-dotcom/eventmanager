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

import com.eventmanager.entity.OtpCode;
import com.eventmanager.repository.OtpCodeRepository;
import java.security.SecureRandom;


@Service
public class AuthService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;
    private final AuthenticationManager auth;
    private final MapperService mapper;

    private final OtpCodeRepository otpRepo;

    private final SocieteRepository societeRepo;
    private final PrestataireRepository prestataireRepo;

    private final PasswordResetTokenRepository tokenRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Value("${app.reset-password.url}")
    private String resetPasswordUrl;
    @Value("${app.reset-password.expiration-minutes}")
    private int expirationMinutes;


    public AuthService(UtilisateurRepository repo, PasswordEncoder enc, JwtUtil jwt, AuthenticationManager auth, MapperService mapper, OtpCodeRepository otpRepo, SocieteRepository societeRepo, PrestataireRepository prestataireRepo, PasswordResetTokenRepository tokenRepo) {
        this.repo = repo;
        this.enc = enc;
        this.jwt = jwt;
        this.auth = auth;
        this.mapper = mapper;
        this.otpRepo = otpRepo;
        this.societeRepo = societeRepo;
        this.prestataireRepo = prestataireRepo;
        this.tokenRepo = tokenRepo;
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

    // ── Register modifié : sauvegarde + envoi OTP (pas de JWT ici) ──────────────
    @Transactional
    public void register(AuthentificationRequete req) {

        if (repo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email déjà utilisé");

        Utilisateur u = new Utilisateur();
        u.setNom(req.getNom());
        u.setEmail(req.getEmail());
        u.setMotDePasse(enc.encode(req.getMotDePasse()));
        u.setRole(req.getRole());
        u.setTelephone(req.getTelephone());
        u.setVerified(false);
        repo.save(u);

        // ✅ CAS 1 : COMPANY
        if ("company".equalsIgnoreCase(req.getRole())) {
            Societe societe = new Societe();
            societe.setNom(req.getNom());
            societe.setEmail(req.getEmail());
            societe.setTelephone(req.getTelephone());
            societeRepo.save(societe);
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
            u.setPrestataire(prestataire);
            repo.save(u);
        }

        // ✅ Génération et envoi OTP
        sendOtp(req.getEmail());
    }

    // ── Génère et envoie l'OTP ───────────────────────────────────────────────────
    private void sendOtp(String email) {
        otpRepo.deleteByEmail(email);
        String code = String.format("%06d", new SecureRandom().nextInt(999999));
        otpRepo.save(new OtpCode(email, code, LocalDateTime.now().plusMinutes(5)));
        emailService.sendOtpEmail(email, code);
    }

    // ── Vérification OTP → retourne le JWT ───────────────────────────────────────
    @Transactional
    public ReponseAuthentification verifyOtp(String email, String otp) {

        OtpCode otpCode = otpRepo.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("Code introuvable"));

        if (otpCode.isUsed())    throw new RuntimeException("Ce code a déjà été utilisé");
        if (otpCode.isExpired()) throw new RuntimeException("Code expiré, renvoyez-en un nouveau");
        if (!otpCode.getCode().equals(otp)) throw new RuntimeException("Code incorrect");

        Utilisateur u = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        u.setVerified(true);
        repo.save(u);

        otpCode.setUsed(true);
        otpRepo.save(otpCode);

        // ✅ Génère et retourne le JWT comme le login
        return mapper.toAuth(jwt.generate(u.getEmail()), u);
    }
    
    // ── Renvoi OTP ────────────────────────────────────────────────────────────────
    @Transactional
    public void resendOtp(String email) {
        repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email introuvable"));
        sendOtp(email);
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
    @Transactional
    public void forgotPassword(String email) {
        // Si l'email n'existe pas → on ne dit rien (sécurité)
        repo.findByEmail(email).ifPresent(u -> {
            // Supprimer les anciens tokens de cet email
            tokenRepo.deleteByEmail(email);
            // Créer un nouveau token valable 15 min
            String token  = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
            tokenRepo.save(new PasswordResetToken(token, email, expiry));
            // Envoyer l'email
            String lien = resetPasswordUrl + "?token=" + token;
            emailService.sendPasswordResetEmail(email, lien);
        });
    }

    @Transactional
    public void resetPassword(String token, String nouveauMotDePasse) {
        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (prt.isUsed())    throw new RuntimeException("Ce lien a déjà été utilisé");
        if (prt.isExpired()) throw new RuntimeException("Ce lien a expiré, recommencez");

        Utilisateur u = repo.findByEmail(prt.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        u.setMotDePasse(enc.encode(nouveauMotDePasse));
        repo.save(u);

        prt.setUsed(true);
        tokenRepo.save(prt);
    }
}