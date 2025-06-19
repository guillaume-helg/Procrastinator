package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.*;
import miage.procratinator.procrastinator.entities.*;
import miage.procratinator.procrastinator.entities.enumeration.*;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static miage.procratinator.procrastinator.utilities.Utilitaires.calculerDifferenceEntreDate;


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
    private PiegeProductiviteRepository piegeProductiviteRepository;
    
    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    @Autowired
    private GestionnaireService gestionnaireService;

    @Autowired
    private ExcuseRepository excuseRepository;

    @Autowired
    private GrandConcoursRepository grandConcoursRepository;

    @Autowired
    private ParticipationGrandConcoursRepository participationGrandConcoursRepository;

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

        return tachesAEviterRepository.findTacheAEviterByIdTacheAEviter(tacheAEviter.getIdTacheAEviter()).stream().findFirst().orElseGet(
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
        List<TacheAEviter> tacheAEviters = tachesAEviterRepository.findTacheAEviterByIdTacheAEviter(bodyTacheAEviter.getIdTacheAEviter());
        StatutTache nouveauStatut = bodyTacheAEviter.getStatut();
        TacheAEviter tacheAEviter = tacheAEviters.getFirst();

        if (tacheAEviters.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tache inconnu");
        } else if (nouveauStatut == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Une tâche ne peut être nulle");
        } else if (nouveauStatut.equals(StatutTache.EVITEE_AVEC_SUCCES)) {
            tacheAEviter.setStatut(nouveauStatut);
            tachesAEviterRepository.save(tacheAEviter);
            return tacheEviteeAvecSucces(tacheAEviter);
        }

        tacheAEviter.setStatut(nouveauStatut);
        tachesAEviterRepository.save(tacheAEviter);

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

        return ResponseEntity.status(HttpStatus.OK)
                .body("Tâche evitée avec succès ! + "
                + pointsGagnes + " points !"
                + this.checkNiveauProcrastinateur(procrastinateur));
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
            pointsJoursDeRetard = calculerDifferenceEntreDate(tache.getDateLimite(), LocalDate.now()).getDays() * 5;
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
    public String checkNiveauProcrastinateur(Procrastinateur procrastinateur) {
        String progression = "";
        for(NiveauProcrastination niveau : NiveauProcrastination.values()) {
            if(procrastinateur.getPointsAccumules() >= niveau.getPointsRequis()) {
                if(procrastinateur.getNiveauProcrastination() != niveau) {
                    procrastinateur.setNiveauProcrastination(niveau);
                    procrastinateurRepository.save(procrastinateur);
                    progression = "\nL'utilisateur vient d'évoluer au niveau : " + procrastinateur.getNiveauProcrastination();
                }
            }
        }
        return progression;
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
                .findParticipationDefiByIdDefi(idDefi);

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
        newParticipation.setIdDefi(defi.getIdDefiProcrastination());
        newParticipation.setStatutParticipationDefi(StatutParticipationDefi.INSCRIT);

        ParticipationDefi savedParticipation = participationDefiRepository.save(newParticipation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipation);
    }

    /**
     * Valide un défi pour l'utilisateur courant, met à jour les entités concernées,
     * et récompense l'utilisateur avec des points lors de la réussite du défi.
     *
     * @param idDefi l'identifiant du défi à valider
     * @return un ResponseEntity contenant le statut de l'opération et
     * les informations sur la completion du défi et les points gagnés
     * @throws IllegalArgumentException si le défi ou les détails de participation
     *                                  correspondant à l'idDefi n'existent pas
     */
    public ResponseEntity<?> validerDefi(Long idDefi) {
        Procrastinateur procrastinateur = (Procrastinateur) utilisateurCourant.getUtilisateurConnecte();

        ParticipationDefi participationDefi = participationDefiRepository.findParticipationDefiByIdDefiAndIdProcrastinateur(idDefi, procrastinateur.getIdUtilisateur()).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id participation defi inexistant")
        );

        DefiProcrastination defi = defiProcrastinationRepository.findByIdDefiProcrastination(idDefi).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id defi inexistant")
        );
        
        participationDefi.setStatutParticipationDefi(StatutParticipationDefi.TERMINE);
        defi.setStatut(Statut.INACTIF);
        procrastinateur.setPointsAccumules(procrastinateur.getPointsAccumules() + defi.getPointsAGagner());
        checkNiveauProcrastinateur(procrastinateur);

        defi = defiProcrastinationRepository.save(defi);
        procrastinateurRepository.save(procrastinateur);
        participationDefiRepository.save(participationDefi);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Défi valider !!! "+ defi.getPointsAGagner() + " points !" + checkNiveauProcrastinateur(procrastinateur));
    }

    /**
     * Désactive un piège de productivité en modifiant son statut, attribue les points correspondants 
     * à l'utilisateur connecté et vérifie le niveau de ce dernier.
     *
     * @param idPiege l'identifiant unique du piège de productivité à désactiver
     * @return un message confirmant que le piège a été évité avec succès
     * @throws IllegalArgumentException si l'identifiant du piège est invalide ou si le piège n'est pas actif
     */
    public String eviterLePiege(Long idPiege) throws IllegalArgumentException {
        PiegeProductivite piegeProductivite = piegeProductiviteRepository.findByIdPiegeProductivite(idPiege).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id piege inexistant")
        );

        if (piegeProductivite.getStatut() != Statut.ACTIF) {
            throw new IllegalArgumentException("Piege non actif !");
        }

        piegeProductivite.setStatut(Statut.INACTIF);
        piegeProductivite.setIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());

        piegeProductiviteRepository.save(piegeProductivite);

        Procrastinateur procrastinateur = procrastinateurRepository.findProcrastinateurByMail(utilisateurCourant.getUtilisateurConnecte().getMail()).stream().findFirst().orElseThrow();
        procrastinateur.setPointsAccumules(procrastinateur.getPointsAccumules() + 50);
        checkNiveauProcrastinateur(procrastinateur);

        return "Piège évité avec succès";
    }

    /**
     * Gère le cas où un utilisateur tombe dans un "piège" en réduisant ses points accumulés
     * et déclenche les mises à jour appropriées comme l'attribution de récompenses et la vérification du niveau.
     *
     * @param idPiege L'identifiant unique du piège à vérifier et traiter.
     * @return Un message indiquant que l'utilisateur est tombé dans un piège.
     * @throws IllegalArgumentException si le piège avec l'id fourni n'existe pas.
     */
    public String tomberDansPiege(Long idPiege) {
        piegeProductiviteRepository.findByIdPiegeProductivite(idPiege).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id piege inexistant")
        );

        Procrastinateur procrastinateur = procrastinateurRepository.findProcrastinateurByMail(utilisateurCourant.getUtilisateurConnecte().getMail()).stream().findFirst().orElseThrow();
        procrastinateur.setPointsAccumules(procrastinateur.getPointsAccumules() - 50);
        gestionnaireService.attribuerRecompense(1L, utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
        checkNiveauProcrastinateur(procrastinateur);
        procrastinateurRepository.save(procrastinateur);

        return "Tu es tombé dans un piège";
    }

    /**
     * Valide une excuse en mettant à jour son statut à "APPROUVEE".
     *
     * @param idExcuse l'identifiant de l'excuse à valider
     * @return une ResponseEntity contenant l'excuse mise à jour et un statut HTTP CREATED
     * @throws IllegalArgumentException si aucune excuse n'est trouvée avec l'identifiant fourni
     */
    public ResponseEntity<?> validerExcuse(Long idExcuse) {
        Excuse excuse = excuseRepository.findExcuseByIdExcuse(idExcuse).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id excuse inexistant")
        );
        excuse.setStatut(StatutExcuse.APPROUVEE);

        return new ResponseEntity<>(excuseRepository.save(excuse), HttpStatus.CREATED);
    }

    /**
     * Rejette une excuse en mettant à jour son statut à "REJETEE" et sauvegarde les modifications dans le dépôt.
     *
     * @param idExcuse l'identifiant unique de l'excuse à rejeter
     * @return une ResponseEntity contenant l'objet excuse mis à jour et un statut HTTP CREATED
     */
    public ResponseEntity<?> rejeterExcuse(Long idExcuse) {
        Excuse excuse = excuseRepository.findExcuseByIdExcuse(idExcuse).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id excuse inexistant")
        );
        excuse.setStatut(StatutExcuse.REJETEE);

        return new ResponseEntity<>(excuseRepository.save(excuse), HttpStatus.CREATED);
    }

    /**
     * Récupère la liste des attributions de récompenses pour l'utilisateur actuellement connecté.
     *
     * @return ResponseEntity contenant la liste des objets AttributionRecompense associés à l'utilisateur actuel, avec un statut HTTP OK.
     */
    public ResponseEntity<?> getRecompensesUtilisateur() {
        List<AttributionRecompense> attributionRecompense = attributionRecompenseRepository.findAttributionRecompensesByIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
        return new ResponseEntity<>(attributionRecompense, HttpStatus.OK);
    }

    /**
     * Permet à un utilisateur de participer à un Grand Concours spécifique identifié par l'ID fourni
     * si l'utilisateur est éligible et n'est pas déjà inscrit.
     *
     * @param id L'identifiant du Grand Concours auquel l'utilisateur souhaite participer.
     * @return Une ResponseEntity contenant :
     * - Statut 201 (Created) et l'objet ParticipationGrandConcours créé si l'opération réussit.
     * - Statut 400 (Bad Request) avec un message approprié si la requête est invalide ou si l'utilisateur participe déjà.
     * - Statut 401 (Unauthorized) si l'utilisateur n'est pas authentifié.
     */
    public ResponseEntity<?> participerGrandConcours(Long id) {
        GrandConcours grandConcours = grandConcoursRepository.findGrandConcourByIdGrandConcours(id).stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id grand concours inexistant")
        );

        if (utilisateurCourant == null || utilisateurCourant.getUtilisateurConnecte() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non connecté");
        }

        List<ParticipationGrandConcours> participations = participationGrandConcoursRepository.findParticipationDefiByIdGrandConcours(id);

        if (grandConcours.getDateDebut().isAfter(LocalDate.now()) && grandConcours.getDateFin().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Le grand concours n'est pas actif");
        }

        Long userId = utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur();
        boolean alreadyParticipating = participations.stream()
                .anyMatch(participation -> participation.getIdProcrastinateur().equals(userId));

        if (alreadyParticipating) {
            return ResponseEntity.badRequest().body("Déjà inscrit au concours");
        }

        ParticipationGrandConcours newParticipation = new ParticipationGrandConcours();
        newParticipation.setIdProcrastinateur(userId);
        newParticipation.setDateInscription(LocalDate.now());
        newParticipation.setIdGrandConcours(grandConcours.getIdGrandConcours());
        newParticipation.setStatutParticipationGrandConcours(StatutParticipationDefi.INSCRIT);

        ParticipationGrandConcours savedParticipation = participationGrandConcoursRepository.save(newParticipation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipation);
    }

    /**
     * Définit l'excuse préférée pour l'utilisateur actuellement connecté et persiste le changement.
     *
     * @param excuse l'excuse à définir comme excuse préférée pour l'utilisateur
     * @return une ResponseEntity contenant l'excuse fournie et un statut HTTP CREATED
     */
    public ResponseEntity<?> setExcuse(String excuse) {
        Procrastinateur procrastinateur = procrastinateurRepository.findProcrastinateurByMail(utilisateurCourant.getUtilisateurConnecte().getMail()).stream().findFirst().orElseThrow();
        procrastinateur.setExcusePreferee(excuse);
        procrastinateurRepository.save(procrastinateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(excuse);
    }
}