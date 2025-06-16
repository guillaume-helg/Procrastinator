package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.metier.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    UtilisateurService utilisateurService;

    /**
     * Crée un nouvel utilisateur en enregistrant les informations reçues ou retourne un utilisateur existant
     * si un utilisateur avec le même email est déjà enregistré.
     *
     * @param utilisateur les informations de l'utilisateur à créer, incluant les détails comme le pseudo et l'email
     * @return une réponse HTTP contenant l'objet Utilisateur créé ou existant, et le statut HTTP 201 (Created)
     */
    @PostMapping
    public Utilisateur creerUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.creerUtilisateur(utilisateur);
    }

    /**
     * Récupère tous les objets Utilisateur disponibles dans le système.
     *
     * @return une liste de tous les utilisateurs existants
     */
    @GetMapping
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurService.findAll();
    }
}