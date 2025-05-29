package miage.procratinator.procrastinator.utilities;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.entities.enumeration.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UtilisateurCourant {
    @Autowired
    private HttpSession session;

    public Utilisateur getUtilisateurConnecte() {
        return (Utilisateur) session.getAttribute("utilisateur");
    }

    public boolean estConnecte() {
        return getUtilisateurConnecte() != null;
    }

    public boolean estGestionnaire() {
        Utilisateur u = getUtilisateurConnecte();
        return u != null && u.getRole() == Role.GESTIONNAIRE;
    }

    public boolean estProcrastinateur() {
        Utilisateur u = getUtilisateurConnecte();
        return u != null && u.getRole() == Role.PROCRASTINATEUR;
    }

    public boolean estAntiProcrastinateur() {
        Utilisateur u = getUtilisateurConnecte();
        return u != null && u.getRole() == Role.ANTI_PROCRASTINATEUR;
    }
}
