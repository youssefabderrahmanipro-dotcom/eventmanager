package com.eventmanager.controller;

import com.eventmanager.dto.*;
import com.eventmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService s;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public AuthController(AuthService s) {
        this.s = s;
    }

    @PostMapping("/login")
    public ResponseEntity<ReponseAuthentification> login(@RequestBody AuthentificationRequete r) {
        return ResponseEntity.ok(s.login(r));
    }

    /*@PostMapping("/register")
    public ResponseEntity<ReponseAuthentification> register(@RequestBody AuthentificationRequete r) {
        return ResponseEntity.ok(s.register(r));
    }*/
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthentificationRequete r) {
        try {
            s.register(r);
            return ResponseEntity.ok(Map.of("message", "Code envoyé à " + r.getEmail()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequete r) {
        try {
            ReponseAuthentification rep = s.verifyOtp(r.getEmail(), r.getOtp());
            return ResponseEntity.ok(rep);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> body) {
        try {
            s.resendOtp(body.get("email"));
            return ResponseEntity.ok(Map.of("message", "Code renvoyé"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody MotDePasseOublieRequete r) {
        s.forgotPassword(r.getEmail());
        return ResponseEntity.ok(
                Map.of("message", "Si cet email existe, un lien a été envoyé.")
        );
    }

    // GET : affiche le formulaire HTML dans le navigateur
    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordPage(@RequestParam String token) {
        String html = """
                    <!DOCTYPE html>
                    <html lang="fr">
                    <head>
                      <meta charset="UTF-8"/>
                      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                      <title>Réinitialisation — Event Manager</title>
                      <style>
                        * { box-sizing: border-box; margin: 0; padding: 0; }
                        body {
                          background: #090909; min-height: 100vh;
                          display: flex; align-items: center; justify-content: center;
                          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                          padding: 24px;
                        }
                        .card {
                          background: #111; border: 0.5px solid #2A2A2A;
                          border-radius: 20px; padding: 40px 32px;
                          width: 100%; max-width: 420px;
                        }
                        .logo {
                          width: 64px; height: 64px; border-radius: 16px;
                          background: linear-gradient(135deg, #C9A84C, #8A6A24);
                          display: flex; align-items: center; justify-content: center;
                          font-size: 28px; font-weight: 700; color: #090909;
                          margin: 0 auto 12px;
                        }
                        .brand { text-align: center; margin-bottom: 32px; }
                        .brand h1 { color: #F5F5F0; font-size: 16px; letter-spacing: 4px; text-transform: uppercase; margin: 4px 0 2px; }
                        .brand p  { color: rgba(245,245,240,0.5); font-size: 12px; letter-spacing: 1px; }
                        .title { font-size: 24px; font-weight: 700; color: #F5F5F0; margin-bottom: 6px; }
                        .sub   { font-size: 13px; color: rgba(245,245,240,0.55); margin-bottom: 6px; line-height: 1.5; }
                        .bar   { width: 32px; height: 2px; background: #C9A84C; border-radius: 1px; margin-bottom: 28px; }
                        label  { display: block; font-size: 10px; letter-spacing: 1.5px; color: #F5F5F0; font-weight: 600; margin-bottom: 6px; margin-top: 16px; }
                        input  {
                          width: 100%; padding: 14px;
                          background: #181818; border: 0.5px solid #2A2A2A;
                          border-radius: 12px; color: #F5F5F0; font-size: 14px;
                          outline: none; transition: border-color .2s;
                        }
                        input:focus { border-color: rgba(201,168,76,0.4); background: #141414; }
                        .error {
                          background: rgba(224,87,87,0.10); border: 0.5px solid rgba(224,87,87,0.3);
                          border-radius: 10px; padding: 12px; color: #E05757;
                          font-size: 13px; margin-bottom: 16px; display: none;
                        }
                        .btn {
                          width: 100%; margin-top: 24px; padding: 16px;
                          background: linear-gradient(135deg, #C9A84C, #8A6A24);
                          border: none; border-radius: 14px; color: #090909;
                          font-size: 15px; font-weight: 600; letter-spacing: 0.8px;
                          cursor: pointer; transition: opacity .2s;
                        }
                        .btn:hover { opacity: 0.88; }
                        .success { text-align: center; display: none; }
                        .check {
                          width: 72px; height: 72px; border-radius: 50%;
                          background: linear-gradient(135deg, #C9A84C, #8A6A24);
                          display: flex; align-items: center; justify-content: center;
                          font-size: 32px; color: #090909; font-weight: 700;
                          margin: 0 auto 20px;
                        }
                        .success h2 { color: #F5F5F0; font-size: 22px; margin-bottom: 10px; }
                        .success p  { color: rgba(245,245,240,0.55); font-size: 13px; line-height: 1.6; }
                        .btn-login {
                                      display: inline-block; width: 100%; margin-top: 20px; padding: 16px;
                                      background: linear-gradient(135deg, #C9A84C, #8A6A24);
                                      border: none; border-radius: 14px; color: #090909;
                                      font-size: 15px; font-weight: 600; letter-spacing: 0.8px;
                                      cursor: pointer; text-decoration: none; text-align: center;
                                      transition: opacity .2s;
                                    }
                                    .btn-login:hover { opacity: 0.88; }
                                    .divider { width: 100%; height: 0.5px; background: #2A2A2A; margin: 24px 0; }
                      </style>
                    </head>
                    <body>
                      <div class="card">
                        <div class="brand">
                          <div class="logo">M</div>
                          <h1>Event Manager</h1>
                          <p>Gestion d'Événements</p>
                        </div>
                        <div id="formSection">
                          <div class="title">Nouveau mot de passe</div>
                          <div class="sub">Choisissez un nouveau mot de passe sécurisé.</div>
                          <div class="bar"></div>
                          <div class="error" id="errBox"></div>
                          <form id="resetForm">
                            <label>NOUVEAU MOT DE PASSE</label>
                            <input type="password" id="pw" placeholder="••••••••" required minlength="6"/>
                            <label>CONFIRMER LE MOT DE PASSE</label>
                            <input type="password" id="pw2" placeholder="••••••••" required minlength="6"/>
                            <button class="btn" type="submit">Réinitialiser le mot de passe</button>
                          </form>
                        </div>
                       <div class="success" id="successSection">
                                <div class="check">✓</div>
                                <h2>Mot de passe mis à jour !</h2>
                                <p>Votre mot de passe a été réinitialisé avec succès.</p>
                                <div class="divider"></div>
                                <a href="__LOGIN_URL__" class="btn-login">Se connecter à l'application</a>
                                <p style="margin-top:16px; font-size:11px; color:rgba(245,245,240,0.3);">
                                  Si le bouton ne fonctionne pas, ouvrez manuellement l'application.
                                </p>
                              </div>
                      </div>
                      <script>
                        const TOKEN = '__TOKEN__';
                        document.getElementById('resetForm').addEventListener('submit', async (e) => {
                          e.preventDefault();
                          const pw  = document.getElementById('pw').value;
                          const pw2 = document.getElementById('pw2').value;
                          const err = document.getElementById('errBox');
                          if (pw !== pw2) {
                            err.textContent = 'Les mots de passe ne correspondent pas';
                            err.style.display = 'block'; return;
                          }
                          if (pw.length < 6) {
                            err.textContent = 'Minimum 6 caractères requis';
                            err.style.display = 'block'; return;
                          }
                          err.style.display = 'none';
                          const res = await fetch('/api/auth/reset-password', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ token: TOKEN, nouveauMotDePasse: pw })
                          });
                          if (res.ok) {
                            document.getElementById('formSection').style.display  = 'none';
                            document.getElementById('successSection').style.display = 'block';
                          } else {
                            const d = await res.json().catch(() => ({}));
                            err.textContent = d.message || 'Lien invalide ou expiré.';
                            err.style.display = 'block';
                          }
                        });
                      </script>
                    </body>
                    </html>
                """.replace("__TOKEN__", token)
                .replace("__LOGIN_URL__", frontendUrl + "/auth/login");

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    // POST : traite la réinitialisation
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequete req) {
        try {
            s.resetPassword(req.getToken(), req.getNouveauMotDePasse());
            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
