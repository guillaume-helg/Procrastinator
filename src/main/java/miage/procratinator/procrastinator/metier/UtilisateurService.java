package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findUtilisateurByMail(utilisateur.getMail());
        Utilisateur utilisateur1;

        if (utilisateurs.isEmpty()) {
            utilisateur1 = new Utilisateur();
            utilisateur1.setPseudo(utilisateur.getPseudo());
            utilisateur1.setMail(utilisateur.getMail());
            /*
            * utilisateur.setDateInscription(null);
            * utilisateur.setPointsAccumules(0);
            * utilisateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
            */
            utilisateur1 = utilisateurRepository.save(utilisateur1);
        } else {
            utilisateur1 = utilisateurs.getFirst();
        }
        return utilisateur1;
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
            u.setExcusePreferee(updated.getExcusePreferee());
            return utilisateurRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
