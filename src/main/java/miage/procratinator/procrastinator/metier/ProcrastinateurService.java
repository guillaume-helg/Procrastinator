package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.DefiProcrastinationRepository;
import miage.procratinator.procrastinator.dao.ParticipationDefiRepository;
import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.TachesAEviterRepository;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.ParticipationDefi;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.TacheAEviter;
import miage.procratinator.procrastinator.entities.enumeration.*;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class ProcrastinateurService {

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

    @Autowired
    private TachesAEviterRepository tachesAEviterRepository;

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    @Autowired
    private ParticipationDefiRepository participationDefiRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    /**
     * Crée un procrastinateur s'il n'existe pas déjà via son ID utilisateur
     * Si trouvé, retourne l'existant ; sinon, initialise un nouveau procrastinateur
     * avec les données fournies, puis le sauvegarde
     *
     * @param procrastinateur Les données du procrastinateur à créer
     * @return Le procrastinateur existant ou nouvellement créé
     */
    public Procrastinateur createProcrastinateur(Procrastinateur procrastinateur) {
        if (procrastinateur == null) {
            throw new IllegalArgumentException("Le procrastinateur est null");
        }

        return procrastinateurRepository
            .findProcrastinateurByPseudo(procrastinateur.getPseudo()).stream().findFirst().orElseGet(
                ()-> {
                    procrastinateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
                    procrastinateur.setPointsAccumules(0);
                    procrastinateur.setDateInscription(LocalDate.now());
                    Procrastinateur nouveauProcrastinateur = new Procrastinateur();
                    BeanUtils.copyProperties(procrastinateur, nouveauProcrastinateur);
                    return procrastinateurRepository.save(nouveauProcrastinateur);
                }
            );
    }

    /**
     * Crée une nouvelle tâche à éviter si elle n'existe pas déjà.
     * Si une tâche avec le même ID existe déjà, elle est simplement retournée
     * Sinon, une nouvelle tâche est créée avec les informations fournies
     * enregistrée en base de données, puis retournée
     *
     * @return la tâche existante ou la nouvelle tâche créée
     */
    public TacheAEviter creerTacheAEviter(TacheAEviter tacheAEviter) {
        if (tacheAEviter == null) {
            throw new IllegalArgumentException("tacheAEviter est null");
        }

        return tachesAEviterRepository.findTacheAEviterByDescription(tacheAEviter.getDescription()).stream().findFirst().orElseGet(
                () -> {
                    tacheAEviter.setIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
                    tacheAEviter.setDateCreation(LocalDate.now());
                    tacheAEviter.setStatut(StatutTache.EN_ATTENTE);
                    TacheAEviter nouvelleTacheAEviter = new TacheAEviter();
                    BeanUtils.copyProperties(tacheAEviter, nouvelleTacheAEviter);
                    return tachesAEviterRepository.save(nouvelleTacheAEviter);
                }
        );
    }

    /**
     * Met à jour le statut d'une tâche spécifiée en fonction des données fournies.
     *
     * @param bodyTacheAEviter l'objet tâche contenant le statut mis à jour et la description.
     *                         La description est utilisée pour localiser la tâche à mettre à jour.
     * @return un ResponseEntity contenant le résultat de l'opération de mise à jour
     */
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

    /**
     * Gère l'évitement réussi d'une tâche, met à jour les points du procrastinateur,
     * vérifie une éventuelle promotion de niveau et retourne une réponse appropriée.
     *
     * @param tacheAEviter la tâche qui a été évitée et contribue à l'accumulation de points
     * @return un ResponseEntity contenant le message de succès et les détails de promotion
     */
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

    /**
     * Calcule le score d'une tâche évitée en fonction de son urgence et du dépassement de sa date limite.
     * Le score est déterminé par les points du degré d'urgence et les jours de retard éventuels.
     *
     * @param tache La tâche à évaluer, contenant les informations sur son degré d'urgence et sa date limite.
     * @return Le score de la tâche évitée, qui est la somme des points d'urgence et des points de retard,
     * plafonné à une valeur maximale de 200
     */
    public int calculTacheEvitee(TacheAEviter tache) {
        int pointsDegreUrgence = tache.getDegresUrgence().getValeur() * 10;
        int pointsJoursDeRetard = 0;

        if (LocalDate.now().isAfter(tache.getDateLimite())) {
            int joursDeRetard = (int) ChronoUnit.DAYS.between(tache.getDateLimite(), LocalDate.now());
            pointsJoursDeRetard = joursDeRetard * 5;
        }
        return Math.min(pointsDegreUrgence + pointsJoursDeRetard, 200);
    }

    /**
     * Vérifie et met à jour le niveau de procrastination du procrastinateur donné
     * en fonction de ses points accumulés. Si les points atteignent les exigences pour
     * un niveau de procrastination supérieur, le niveau est mis à jour et persisté.
     *
     * @param procrastinateur le procrastinateur dont le niveau de procrastination doit être vérifié et potentiellement mis à jour
     * @return le niveau de procrastination mis à jour si un changement a eu lieu, ou null si aucune mise à jour n'a été effectuée
     */
    public NiveauProcrastination checkNiveauProcrastinateur(Procrastinateur procrastinateur) {
        if (procrastinateur.getPointsAccumules() >= NiveauProcrastination.INTERMEDIAIRE.getPointsRequis() && procrastinateur.getNiveauProcrastination() != NiveauProcrastination.INTERMEDIAIRE) {
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

    /**
     * Vérifie si un procrastinateur est éligible à un niveau de récompense donné en fonction
     * de son ancienneté d'inscription et de ses points d'expérience accumulés.
     *
     * @param procrastinateur le procrastinateur dont l'éligibilité à la récompense est vérifiée
     * @param niveau          le niveau de récompense requis contenant les critères d'éligibilité
     * @return vrai si le procrastinateur remplit les conditions d'ancienneté et de points
     * d'expérience requis faux sinon
     */
    public boolean checkAttributionRecompense(Procrastinateur procrastinateur, NiveauRecompense niveau) {
        LocalDate dateInscription = procrastinateur.getDateInscription();

        int moisAnciennete = Period.between(dateInscription, LocalDate.now()).getMonths()
                + 12 * Period.between(dateInscription, LocalDate.now()).getYears();
        int pointsExperience = procrastinateur.getPointsAccumules();

        return moisAnciennete >= niveau.getNombreDeMois() && pointsExperience >= niveau.getPointsAttribues();
    }

    /**
     * Récupère la liste des tâches à éviter pour le procrastinateur identifié par l'ID de l'utilisateur courant.
     *
     * @return une liste d'objets TacheAEviter associés à l'ID du procrastinateur actuellement connecté.
     */
    public List<TacheAEviter> getTachesByProcrastinateurId() {
        return tachesAEviterRepository.findTacheAEviterByIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
    }

    /**
     * Gère la participation d'un utilisateur à un défi de procrastination.
     * Valide l'éligibilité de l'utilisateur à rejoindre le défi et vérifie que les critères
     * du défi (par ex. statut actif et limite de participants) sont respectés avant d'inscrire l'utilisateur.
     *
     * @param idDefi l'identifiant unique du défi auquel l'utilisateur souhaite participer
     * @return une ResponseEntity
     */
    public ResponseEntity<?> participerDefi(Long idDefi) {
        DefiProcrastination defi = defiProcrastinationRepository.findById(idDefi)
                .orElseThrow();

        if (utilisateurCourant == null || utilisateurCourant.getUtilisateurConnecte() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non connecté");
        }

        List<ParticipationDefi> participations = participationDefiRepository
                .findParticipationDefiByIdDefiProcrastinateur(idDefi);

        if (defi.getStatut() != Statut.ACTIF) {
            return ResponseEntity.badRequest().body("Le défi n'est pas actif");
        }

        Long userId = utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur();
        boolean alreadyParticipating = participations.stream()
                .anyMatch(participation -> participation.getIdProcrastinateur().equals(userId));

        if (alreadyParticipating) {
            return ResponseEntity.badRequest().body("Déjà inscrit au défi");
        }

        if (participations.size() >= 5) {
            return ResponseEntity.badRequest().body("Le nombre maximum de participants est atteint");
        }

        ParticipationDefi newParticipation = new ParticipationDefi();
        newParticipation.setIdProcrastinateur(userId);
        newParticipation.setDateInscription(LocalDate.now());
        newParticipation.setIdDefiProcrastinateur(defi.getIdDefiProcrastination());
        newParticipation.setStatutParticipationDefi(StatutParticipationDefi.INSCRIT);

        ParticipationDefi savedParticipation = participationDefiRepository.save(newParticipation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipation);
    }
}