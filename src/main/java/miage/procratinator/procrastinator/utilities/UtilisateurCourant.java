package miage.procratinator.procrastinator.utilities;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UtilisateurCourant {

    @Autowired
    private HttpSession session;

    /**
     * Récupère l'utilisateur actuellement connecté à partir de la session.
     *
     * @return l'objet Utilisateur représentant l'utilisateur connecté,
     * ou null si aucun utilisateur n'est connecté.
     */
    public Utilisateur getUtilisateurConnecte() {
        return (Utilisateur) session.getAttribute("utilisateur");
    }

    /**
     * Vérifie si un utilisateur est actuellement connecté à la session.
     *
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean estConnecte() {
        return getUtilisateurConnecte() != null;
    }

    /**
     * Vérifie si l'utilisateur actuellement connecté est un procrastinateur.
     *
     * @return true si l'utilisateur connecté est un procrastinateur, false sinon
     */
    public boolean estProcrastinateur() {
        Utilisateur utilisateur = getUtilisateurConnecte();
        return utilisateur instanceof Procrastinateur;
    }
}
