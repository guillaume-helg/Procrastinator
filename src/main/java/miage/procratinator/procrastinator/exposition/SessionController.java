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

    /**
     * Authentifie un utilisateur en fonction de l'email fourni. Si un utilisateur correspondant est trouvé,
     * l'utilisateur est stocké dans la session HTTP et retourné dans la réponse.
     *
     * @param email   l'adresse email de l'utilisateur tentant de se connecter
     * @param session la session HTTP pour stocker l'utilisateur authentifié
     * @return un ResponseEntity contenant l'utilisateur authentifié si l'email est trouvé,
     * ou un message d'erreur avec le statut HTTP approprié si non
     */
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

    /**
     * Déconnecte l'utilisateur courant en invalidant sa session HTTP.
     *
     * @param session l'objet HttpSession représentant la session de l'utilisateur à invalider
     * @return un ResponseEntity contenant un message de succès après la déconnexion
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Déconnexion réussie");
    }

    /**
     * Récupère l'utilisateur actuel de la session si disponible.
     * Si l'utilisateur n'est pas authentifié, retourne une réponse non autorisée.
     *
     * @param utilisateur l'utilisateur actuel récupéré de la session HTTP, ou null si non authentifié
     * @return un ResponseEntity contenant l'utilisateur authentifié si présent, ou un message non autorisé si non authentifié
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@SessionAttribute(value = "utilisateur", required = false) Utilisateur utilisateur) {
        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non connecté");
        }
        return ResponseEntity.ok(utilisateur);
    }
}