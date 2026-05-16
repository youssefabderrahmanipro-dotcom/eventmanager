package com.eventmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Réinitialisation de votre mot de passe — Event Manager");
            helper.setFrom("votre.email@gmail.com");

            String html = """
                <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto; background: #111; color: #F5F5F0; padding: 32px; border-radius: 12px;">
                    <div style="text-align: center; margin-bottom: 28px;">
                        <div style="background: linear-gradient(135deg, #C9A84C, #8A6A24); width: 60px; height: 60px; border-radius: 16px; display: inline-flex; align-items: center; justify-content: center; font-size: 28px; font-weight: bold; color: #090909;">M</div>
                        <h2 style="color: #C9A84C; letter-spacing: 3px; margin-top: 12px;">EVENT MANAGER</h2>
                    </div>
                    <h3 style="color: #F5F5F0;">Réinitialisation du mot de passe</h3>
                    <p style="color: rgba(245,245,240,0.7); line-height: 1.6;">
                        Vous avez demandé à réinitialiser votre mot de passe. Cliquez sur le bouton ci-dessous pour continuer.
                    </p>
                    <div style="text-align: center; margin: 32px 0;">
                        <a href="%s" style="background: linear-gradient(135deg, #C9A84C, #8A6A24); color: #090909; padding: 14px 32px; border-radius: 10px; text-decoration: none; font-weight: bold; font-size: 15px;">
                            Réinitialiser mon mot de passe
                        </a>
                    </div>
                    <p style="color: rgba(245,245,240,0.45); font-size: 12px; text-align: center;">
                        Ce lien expire dans <strong style="color: #C9A84C;">15 minutes</strong>.<br>
                        Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.
                    </p>
                </div>
            """.formatted(resetLink);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}