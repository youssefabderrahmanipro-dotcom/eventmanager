package com.eventmanager;

import com.eventmanager.entity.Utilisateur;
import com.eventmanager.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EventmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagerApplication.class, args);
	}
	@Bean
	CommandLineRunner init(UtilisateurRepository repo, PasswordEncoder enc){
		return args->{
			if(repo.findByEmail("societe@demo.com").isEmpty()){
				Utilisateur u=new Utilisateur(); u.setNom("Societe Events");
				u.setEmail("societe@demo.com"); u.setMotDePasse(enc.encode("123456"));
				u.setRole("company");
				u.setTelephone("+212600000000");
				repo.save(u);
			}
			if(repo.findByEmail("prestataire@demo.com").isEmpty()){
				Utilisateur u=new Utilisateur(); u.setNom("Prestataire Youssef");
				u.setEmail("prestataire@demo.com"); u.setMotDePasse(enc.encode("123456"));
				u.setRole("provider");
				u.setTelephone("+212600000000");
				repo.save(u);
			}
		};
	}
}
