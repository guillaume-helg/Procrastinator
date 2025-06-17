package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.*;
import miage.procratinator.procrastinator.entities.*;
import miage.procratinator.procrastinator.entities.enumeration.Statut;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static miage.procratinator.procrastinator.utilities.Utilitaires.calculerDifferenceEntreDate;

@Service
public class GestionnaireService {

    @Autowired
    private AntiProcrastinateurRepository antiProcrastinateurRepository;

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    @Autowired
    private GrandConcoursRepository grandConcoursRepository;

    @Autowired
    private RecompenseRepository recompenseRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    /**
     * Crée ou récupère un utilisateur AntiProcrastinateur basé sur le pseudo fourni.
     * Cette méthode implémente un pattern "trouver ou créer" pour gérer les entités AntiProcrastinateur.
     *
     * @return AntiProcrastinateur L'entité AntiProcrastinateur existante ou nouvellement créée
     * <p>
     * La méthode suit la logique suivante :
     * 1. Vérifie d'abord si un AntiProcrastinateur avec le pseudo donné existe déjà
     * 2. Si aucun n'existe, en crée un nouveau avec le pseudo fourni
     * 3. Si un existe déjà, retourne l'instance existante (prend le premier si plusieurs existent)
     */
    public AntiProcrastinateur creerAntiProcrastinateur(AntiProcrastinateur antiProcrastinateur) {
        return antiProcrastinateurRepository
                .findByPseudo(antiProcrastinateur.getPseudo())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    AntiProcrastinateur nouveauAntiProcrastinateur = new AntiProcrastinateur();
                    antiProcrastinateur.setDateInscription(LocalDate.now());
                    BeanUtils.copyProperties(antiProcrastinateur, nouveauAntiProcrastinateur);
                    return antiProcrastinateurRepository.save(nouveauAntiProcrastinateur);
                });
    }

    /**
     * Crée ou récupère un défi de procrastination.
     * Cette méthode suit un modèle "trouver ou créer" pour gérer les entités de type DefiProcrastination.
     * Si un défi avec le même ID existe, il est retourné. Sinon, un nouveau défi est créé avec les informations fournies.
     *
     * @param defiProcrastination L'entité contenant les informations du défi de procrastination à créer ou récupérer
     * @return DefiProcrastination L'entité existante ou nouvellement créée
     * @throws IllegalArgumentException si defiProcrastination est null
     */
    public DefiProcrastination creerDefiProcrastinateur(DefiProcrastination defiProcrastination) {
        if (defiProcrastination == null) {
            throw new IllegalArgumentException("Le défi de procrastination ne peut pas être null");
        }

        return defiProcrastinationRepository
                .findByIdDefiProcrastination(defiProcrastination.getIdDefiProcrastination())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    defiProcrastination.setIdGestionnaire(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
                    defiProcrastination.setDuree((float) calculerDifferenceEntreDate(
                            defiProcrastination.getDateDebut(),
                            defiProcrastination.getDateFin()).getDays()
                    );

                    DefiProcrastination newDefi = new DefiProcrastination();
                    BeanUtils.copyProperties(defiProcrastination, newDefi);
                    return defiProcrastinationRepository.save(newDefi);
                });
    }

    /**
     * Crée ou récupère un grand concours.
     * Cette méthode implémente un pattern "trouver ou créer" pour les entités GrandConcours.
     *
     * @param grandConcour L'entité contenant les informations du concours à créer ou récupérer
     * @return GrandConcours L'entité existante ou nouvellement créée
     * @throws IllegalArgumentException si grandConcour est null
     */
    public GrandConcours creerGrandConcours(GrandConcours grandConcour) {
        if (grandConcour == null) {
            throw new IllegalArgumentException("Le grand concours ne peut pas être null");
        }

        return grandConcoursRepository
                .findGrandConcourByIdGrandConcour(grandConcour.getIdGrandConcour())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    GrandConcours nouveauConcours = new GrandConcours();
                    BeanUtils.copyProperties(grandConcour, nouveauConcours);
                    return grandConcoursRepository.save(nouveauConcours);
                });
    }

    public Recompense creerRecompense(Recompense recompense) {
        if (recompense == null) {
            throw new IllegalArgumentException("Le grand concours ne peut pas être null");
        }

        return recompenseRepository.findRecompenseByIdRecompense(recompense.getIdRecompense())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    GrandConcours nouveauConcours = new GrandConcours();
                    BeanUtils.copyProperties(recompense, nouveauConcours);
                    return recompenseRepository.save(recompense);
                });
    }

    public ResponseEntity<AttributionRecompense> attribuerRecompense(Long idRecompense, Long idProcrastinateur) {
        if (idRecompense == null) {
            throw new IllegalArgumentException("idRecompense ne peut pas être null");
        }

        if (idProcrastinateur == null) {
            throw new IllegalArgumentException("idProcrastinateur ne peut pas être null");
        }

        AttributionRecompense attributionRecompense = new AttributionRecompense();
        attributionRecompense.setIdRecompense(idRecompense);
        attributionRecompense.setIdProcrastinateur(idProcrastinateur);
        attributionRecompense.setStatut(Statut.ACTIF);
        attributionRecompense.setDateObtention(LocalDate.now());
        attributionRecompense.setContexte("Attribuer au mérite par notre Gestionnaire : Big Boss");
        attributionRecompense.setDateExpiration(LocalDate.now().plusDays(30));
        return ResponseEntity.ok(attributionRecompenseRepository.save(attributionRecompense));
    }
}