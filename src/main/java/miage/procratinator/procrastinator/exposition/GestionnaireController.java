package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.*;
import miage.procratinator.procrastinator.metier.GestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    @Autowired
    private GestionnaireService gestionnaireService;

    /**
     * Crée un nouvel "AntiProcrastinateur" ou récupère un existant basé sur les informations fournies.
     * Cette méthode valide les données d'entrée et délègue le processus de création à la couche service.
     *
     * @param antiProcrastinateur l'objet AntiProcrastinateur contenant les détails à créer ou récupérer
     * @return ResponseEntity contenant l'entité AntiProcrastinateur créée ou récupérée et un code de statut HTTP
     */
    @PostMapping("/antiprocrastinateurs")
    public ResponseEntity<AntiProcrastinateur> creerAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        if (antiProcrastinateur == null || antiProcrastinateur.getPseudo().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        AntiProcrastinateur createdAntiprocrastinateur = gestionnaireService.creerAntiProcrastinateur(antiProcrastinateur);
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }

    /**
     * Crée un nouveau défi de procrastination en utilisant les informations fournies.
     * Les données du défi sont validées et transmises au service pour traitement.
     *
     * @param defiProcrastination l'entité DefiProcrastination contenant les détails du défi à créer
     * @return ResponseEntity contenant le défi de procrastination créé et un code de statut HTTP 201 (CREATED)
     */
    @PostMapping("/defis")
    public ResponseEntity<DefiProcrastination> creerDefiProcrastination(@RequestBody DefiProcrastination defiProcrastination) {
        DefiProcrastination createdDefiProcrastination = gestionnaireService.creerDefiProcrastinateur(defiProcrastination);
        return new ResponseEntity<>(createdDefiProcrastination, HttpStatus.CREATED);
    }

    /**
     * Crée un nouveau "GrandConcours" annuel en fonction des informations fournies.
     * Cette méthode reçoit les détails du concours, les transmet à la couche service pour traitement,
     * et retourne la réponse appropriée avec le concours créé.
     *
     * @param grandConcours l'objet GrandConcours contenant les informations du concours à créer
     * @return ResponseEntity contenant l'entité GrandConcours nouvellement créée et un code de statut HTTP 201 (CREATED)
     */
    @PostMapping("/concours")
    public ResponseEntity<GrandConcours> creerGrandConcoursAnnuel(@RequestBody GrandConcours grandConcours) {
        GrandConcours savedConcours = gestionnaireService.creerGrandConcours(grandConcours);
        return new ResponseEntity<>(savedConcours, HttpStatus.CREATED);
    }

    /**
     * Crée une nouvelle récompense en fonction des informations fournies.
     * Les données de la récompense sont validées et transmises à la couche service pour création.
     *
     * @param recompense l'objet Recompense contenant les détails de la récompense à créer
     * @return ResponseEntity contenant l'entité Recompense créée et un code de statut HTTP 201 (CREATED)
     */
    @PostMapping("/recompense")
    public ResponseEntity<Recompense> creerRecompense(@RequestBody Recompense recompense) {
        Recompense savedRecompense = gestionnaireService.creerRecompense(recompense);
        return new ResponseEntity<>(savedRecompense, HttpStatus.CREATED);
    }

    /**
     * Associe une récompense existante à un procrastinateur spécifique.
     * Cette méthode utilise l'identifiant de la récompense et de l'utilisateur pour effectuer l'attribution
     * et retourne le résultat sous forme d'une entité AttributionRecompense.
     *
     * @param idRecompense l'identifiant unique de la récompense à attribuer
     * @param idProcrastinateur l'identifiant unique du procrastinateur recevant la récompense
     * @return ResponseEntity contenant l'entité AttributionRecompense représentant l'attribution effectuée
     */
    @PostMapping("/recompense/{idRecompense}/to/{idProcrastinateur}")
    public ResponseEntity<AttributionRecompense> attribuerRecompense(@PathVariable Long idRecompense, @PathVariable Long idProcrastinateur) {
        AttributionRecompense attributionRecompense = gestionnaireService.attribuerRecompense(idRecompense, idProcrastinateur).getBody();
        return new ResponseEntity<>(attributionRecompense, HttpStatus.CREATED);
    }
}