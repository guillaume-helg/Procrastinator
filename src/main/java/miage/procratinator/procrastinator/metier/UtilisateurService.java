package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Crée un nouvel utilisateur ou récupère un utilisateur existant si un utilisateur avec le même email existe déjà.
     *
     * @param utilisateur les informations de l'utilisateur à créer ou vérifier. Contient des détails comme le pseudo et l'email.
     * @return l'objet utilisateur créé ou existant.
     */
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findUtilisateurByMail(utilisateur.getMail())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Utilisateur nouvelUtilisateur = new Utilisateur();
                    nouvelUtilisateur.setPseudo(utilisateur.getPseudo());
                    nouvelUtilisateur.setMail(utilisateur.getMail());
                    nouvelUtilisateur.setDateInscription(LocalDate.now());
                    return utilisateurRepository.save(nouvelUtilisateur);
                });
    }

    /**
     * Récupère toutes les entités utilisateur du référentiel de données.
     *
     * @return une liste de tous les objets Utilisateur disponibles dans le référentiel
     */
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son identifiant unique.
     *
     * @param id l'identifiant unique de l'utilisateur à récupérer
     * @return un Optional contenant l'utilisateur s'il est trouvé, ou un Optional vide si aucun utilisateur avec l'ID spécifié n'existe
     */
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    /**
     * Supprime un utilisateur de la base de données en fonction de son identifiant unique.
     *
     * @param id l'identifiant unique de l'utilisateur à supprimer
     */
    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    /**
     * Met à jour le pseudo d'un utilisateur existant identifié par l'ID spécifié.
     * Si l'utilisateur n'est pas trouvé, une RuntimeException est levée.
     *
     * @param id      l'ID de l'utilisateur à mettre à jour
     * @param updated l'objet Utilisateur contenant le nouveau pseudo
     * @return l'objet Utilisateur mis à jour après avoir été sauvegardé dans le repository
     */
    public Utilisateur update(Long id, Utilisateur updated) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setPseudo(updated.getPseudo());
            return utilisateurRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
