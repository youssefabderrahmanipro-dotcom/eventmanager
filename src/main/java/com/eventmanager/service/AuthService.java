package com.eventmanager.service;

import com.eventmanager.dto.*;
import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.UtilisateurRepository;
import com.eventmanager.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;
    private final AuthenticationManager auth;
    private final MapperService mapper;

    public AuthService(UtilisateurRepository repo, PasswordEncoder enc, JwtUtil jwt, AuthenticationManager auth, MapperService mapper) {
        this.repo = repo;
        this.enc = enc;
        this.jwt = jwt;
        this.auth = auth;
        this.mapper = mapper;
    }

    public ReponseAuthentification login(AuthentificationRequete req) {
        auth.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getMotDePasse())
        );

        Utilisateur u = repo.findByEmail(req.getEmail())
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return mapper.toAuth(jwt.generate(u.getEmail()), u);
    }

    public ReponseAuthentification register(AuthentificationRequete req) {
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
    }
}