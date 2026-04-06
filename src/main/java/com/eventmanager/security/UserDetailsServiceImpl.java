package com.eventmanager.security;

import com.eventmanager.repository.UtilisateurRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UtilisateurRepository repo;

    public UserDetailsServiceImpl(UtilisateurRepository r) {
        repo = r;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var u = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(u.getEmail(), u.getMotDePasse(), List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().toUpperCase())));
    }
}
