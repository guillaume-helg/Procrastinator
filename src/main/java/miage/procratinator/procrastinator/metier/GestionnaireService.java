package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.AntiProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.DefiProcrastinationRepository;
import miage.procratinator.procrastinator.dao.GrandConcoursRepository;
import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UtilisateurCourant utilisateurCourant;

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

    /**
     * Crée ou récupère un utilisateur AntiProcrastinateur basé sur le pseudo fourni.
     * Cette méthode implémente un pattern "trouver ou créer" pour gérer les entités AntiProcrastinateur.
     *
     * @param pseudo Le nom d'utilisateur/pseudo de l'AntiProcrastinateur à créer ou récupérer
     * @return AntiProcrastinateur L'entité AntiProcrastinateur existante ou nouvellement créée
     *
     * La méthode suit la logique suivante :
     * 1. Vérifie d'abord si un AntiProcrastinateur avec le pseudo donné existe déjà
     * 2. Si aucun n'existe, en crée un nouveau avec le pseudo fourni
     * 3. Si un existe déjà, retourne l'instance existante (prend le premier si plusieurs existent)
     */
    public AntiProcrastinateur creerAntiProcrastinateur(String pseudo) {
        List<AntiProcrastinateur> clients = antiProcrastinateurRepository.findByPseudo(pseudo);
        AntiProcrastinateur antiProcrastinateur;

        if (clients.isEmpty()) {
            antiProcrastinateur = new AntiProcrastinateur();
            antiProcrastinateur.setPseudo(pseudo);
            antiProcrastinateur = antiProcrastinateurRepository.save(antiProcrastinateur);
        } else {
            antiProcrastinateur = clients.getFirst();
        }
        return antiProcrastinateur;
    }

    /**
     * Crée ou récupère un défi de procrastination.
     * La méthode suit la logique suivante :
     * - Recherche un défi existant avec l'ID fourni
     * - Si aucun défi n'existe, en crée un nouveau avec les propriétés du défi fourni
     * - Si un défi existe déjà, retourne l'instance existante
     * @param defiProcrastination L'entité contenant les informations du défi à créer ou récupérer
     *                           (ID, titre, description, durée, difficulté, points, dates, etc.)
     * @return DefiProcrastination L'entité DefiProcrastination existante ou nouvellement créée
     */
    public DefiProcrastination creerDefiProcrastinateur(DefiProcrastination defiProcrastination) {
        List<DefiProcrastination> existingDefis = defiProcrastinationRepository
                .findByIdDefiProcrastination(defiProcrastination.getIdDefiProcrastination());

        defiProcrastination.setIdGestionnaire(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
        defiProcrastination.setDuree(
            (float) calculerDifferenceEntreDate(
                    defiProcrastination.getDateDebut(),
                    defiProcrastination.getDateFin()).getDays()
        );

        if (existingDefis.isEmpty()) {
            DefiProcrastination newDefi = new DefiProcrastination();
            BeanUtils.copyProperties(defiProcrastination, newDefi); // Classe de Spring qui fait plaisir
            return defiProcrastinationRepository.save(newDefi);
        }
        return existingDefis.getFirst();
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
}