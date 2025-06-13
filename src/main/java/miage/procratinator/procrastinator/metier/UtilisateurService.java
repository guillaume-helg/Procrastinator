package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    return utilisateurRepository.save(nouvelUtilisateur);
                });
    }

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public void deleteById(Long id) {
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur update(Long id, Utilisateur updated) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setPseudo(updated.getPseudo());
            return utilisateurRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
