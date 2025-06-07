package miage.procratinator.procrastinator.metier;

import jakarta.servlet.http.HttpSession;
import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.TachesAEviterRepository;
import miage.procratinator.procrastinator.entities.*;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import miage.procratinator.procrastinator.entities.enumeration.StatutTache;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


@Service
public class ProcrastinateurService {

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

    @Autowired
    private TachesAEviterRepository tachesAEviterRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;


    /**
     * Connecte un utilisateur avec son adresse email
     * Si l'email existe, l'utilisateur est ajouté à la session
     * et retourné avec un code 200 (OK)
     * Sinon, retourne un message d'erreur avec un code 404
     *
     * @param email   l'email de l'utilisateur
     * @param session la session où stocker l'utilisateur connecté
     * @return l'utilisateur si trouvé, sinon un message d'erreur
     */
    public ResponseEntity<?> loginProcrastinateur(String email, HttpSession session) {
        List<Procrastinateur> utilisateurs = procrastinateurRepository.findProcrastinateurByMail(email);

        if (utilisateurs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email inconnu");
        }

        Procrastinateur utilisateur = utilisateurs.getFirst();
        session.setAttribute("utilisateur", utilisateur);

        return ResponseEntity.ok(utilisateur);
    }

    /**
     * Crée un procrastinateur s'il n'existe pas déjà via son ID utilisateur
     * Si trouvé, retourne l'existant ; sinon, initialise un nouveau procrastinateur
     * avec les données fournies, puis le sauvegarde
     *
     * @param bodyProcrastinateur Les données du procrastinateur à créer
     * @return Le procrastinateur existant ou nouvellement créé
     */
    public Procrastinateur createProcrastinateur(Procrastinateur bodyProcrastinateur) {
        List<Procrastinateur> procrastinateurs = procrastinateurRepository.findProcrastinateurByPseudo(bodyProcrastinateur.getPseudo());
        Procrastinateur procrastinateur;
        LocalDate today = LocalDate.now();
        if (procrastinateurs.isEmpty()) {
            procrastinateur = new Procrastinateur();
            procrastinateur.setMail(bodyProcrastinateur.getMail());
            procrastinateur.setPseudo(bodyProcrastinateur.getPseudo());
            procrastinateur.setExcusePreferee(bodyProcrastinateur.getExcusePreferee());
            procrastinateur.setDateInscription(today);
            procrastinateur.setPointsAccumules(0);
            procrastinateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
            procrastinateur = procrastinateurRepository.save(procrastinateur);
        } else {
            procrastinateur =  procrastinateurs.getFirst();
        }
        return procrastinateur;
    }

    /**
     * Crée une nouvelle tâche à éviter si elle n'existe pas déjà.
     * Si une tâche avec le même ID existe déjà, elle est simplement retournée
     * Sinon, une nouvelle tâche est créée avec les informations fournies
     * enregistrée en base de données, puis retournée
     *
     * @return la tâche existante ou la nouvelle tâche créée
     */
    public TacheAEviter creerTacheAEviter(TacheAEviter bodyTacheAEviter) {
        List<TacheAEviter> tacheAEviters = tachesAEviterRepository.findTacheAEviterByDescription(bodyTacheAEviter.getDescription());
        TacheAEviter tacheAEviter;

        LocalDate today = LocalDate.now();

        if (tacheAEviters.isEmpty()) {
            tacheAEviter = new TacheAEviter();
            tacheAEviter.setDescription(bodyTacheAEviter.getDescription());
            tacheAEviter.setIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
            tacheAEviter.setDegresUrgence(bodyTacheAEviter.getDegresUrgence());
            tacheAEviter.setDateLimite(bodyTacheAEviter.getDateLimite());
            tacheAEviter.setConsequence(bodyTacheAEviter.getConsequence());
            //tacheAEviter.setDateCreation(bodyTacheAEviter.getDateCreation());
            tacheAEviter.setDateCreation(today);

            tacheAEviter.setStatut(StatutTache.EN_ATTENTE);
            tacheAEviter = tachesAEviterRepository.save(tacheAEviter);
        } else {
            tacheAEviter = tacheAEviters.getFirst();
        }
        return tacheAEviter;
    }

    public ResponseEntity<?> updateStatutTache(TacheAEviter bodyTacheAEviter) {
        List<TacheAEviter> tacheAEviters = tachesAEviterRepository.findTacheAEviterByDescription(bodyTacheAEviter.getDescription());
        StatutTache nouveauStatut = bodyTacheAEviter.getStatut();
        TacheAEviter tacheAEviter = tacheAEviters.getFirst();

        if (tacheAEviters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tache inconnu");
        } else if (nouveauStatut == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Une tâche ne peut être nulle");
        } else if (nouveauStatut.equals(StatutTache.EVITEE_AVEC_SUCCES)) {
            return tacheEviteeAvecSucces(tacheAEviter);
        }

        tacheAEviter.setStatut(nouveauStatut);
        tacheAEviter = tachesAEviterRepository.save(tacheAEviter);

        return ResponseEntity.status(HttpStatus.OK).body("Tâche modifiée !");
    }

    public ResponseEntity<?> tacheEviteeAvecSucces(TacheAEviter tacheAEviter) {
        List<Procrastinateur> procrastinateurs = procrastinateurRepository.findProcrastinateurByMail(utilisateurCourant.getUtilisateurConnecte().getMail());
        Procrastinateur procrastinateur = procrastinateurs.getFirst();
        int pointsGagnes = this.calculTacheEvitee(tacheAEviter);
        procrastinateur.setPointsAccumules(procrastinateur.getPointsAccumules() + pointsGagnes);
        procrastinateurRepository.save(procrastinateur);

        String passageNiveau = (this.checkNiveauProcrastinateur(procrastinateur) != null)
                ? "\nLe procrastinateur vient d'être promu " + procrastinateur.getNiveauProcrastination() + " !"
                : "";

        return ResponseEntity.status(HttpStatus.OK).body("Tâche evitée avec succès ! + " + pointsGagnes + " points !" + passageNiveau);
    }

    public int calculTacheEvitee(TacheAEviter tache) {
        int pointsDegreUrgence = tache.getDegresUrgence().getValeur() * 10;
        int pointsJoursDeRetard = 0;

        if (LocalDate.now().isAfter(tache.getDateLimite())) {
            int joursDeRetard = (int) ChronoUnit.DAYS.between(tache.getDateLimite(), LocalDate.now());
            pointsJoursDeRetard = joursDeRetard * 5;
        }
        return Math.min(pointsDegreUrgence + pointsJoursDeRetard, 200);

    }

    public NiveauProcrastination checkNiveauProcrastinateur(Procrastinateur procrastinateur) {
        if ( procrastinateur.getPointsAccumules() >= NiveauProcrastination.INTERMEDIAIRE.getPointsRequis() && procrastinateur.getNiveauProcrastination() != NiveauProcrastination.INTERMEDIAIRE ) {
            procrastinateur.setNiveauProcrastination(NiveauProcrastination.INTERMEDIAIRE);
            procrastinateurRepository.save(procrastinateur);
            return procrastinateur.getNiveauProcrastination();
        } else if (procrastinateur.getPointsAccumules() >= NiveauProcrastination.EXPERT.getPointsRequis() && procrastinateur.getNiveauProcrastination() != NiveauProcrastination.EXPERT) {
            procrastinateur.setNiveauProcrastination(NiveauProcrastination.EXPERT);
            procrastinateurRepository.save(procrastinateur);
            return procrastinateur.getNiveauProcrastination();
        }
        return null;
    }

    public List<TacheAEviter> getTachesByProcrastinateurId() {
        return tachesAEviterRepository.findTacheAEviterByIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
    }
}
