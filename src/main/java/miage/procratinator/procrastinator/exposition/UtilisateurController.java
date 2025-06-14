package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.metier.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
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
    public ResponseEntity<Utilisateur> creerUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur saved = utilisateurService.creerUtilisateur(utilisateur);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les objets Utilisateur disponibles dans le système.
     *
     * @return un ResponseEntity contenant une liste de tous les utilisateurs existants
     */
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param id l'identifiant unique de l'utilisateur à récupérer
     * @return un ResponseEntity contenant l'objet Utilisateur si trouvé, ou une réponse HTTP avec le statut 404 (Not Found) si l'utilisateur n'existe pas
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return utilisateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Met à jour un utilisateur existant en fonction de son identifiant et des nouvelles informations fournies.
     * Si l'utilisateur avec l'identifiant donné n'est pas trouvé, une exception est levée.
     *
     * @param id l'identifiant unique de l'utilisateur à mettre à jour
     * @param utilisateur l'objet Utilisateur contenant les nouvelles données à mettre à jour
     * @return un ResponseEntity contenant l'objet Utilisateur mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.update(id, utilisateur));
    }

    /**
     * Supprime un utilisateur en fonction de son identifiant unique.
     *
     * @param id l'identifiant unique de l'utilisateur à supprimer
     * @return une réponse HTTP sans contenu avec le statut 204 (No Content) si l'opération est réussie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
