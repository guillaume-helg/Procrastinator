package miage.procratinator.procrastinator.exposition;

import jakarta.servlet.http.HttpSession;
import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, HttpSession session) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findUtilisateurByMail(email);

        if (utilisateurs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email inconnu");
        }

        Utilisateur utilisateur = utilisateurs.getFirst();
        session.setAttribute("utilisateur", utilisateur);

        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@SessionAttribute(value = "utilisateur", required = false) Utilisateur utilisateur) {
        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non connecté");
        }
        return ResponseEntity.ok(utilisateur);
    }
}