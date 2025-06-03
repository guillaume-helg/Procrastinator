package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.metier.GestionnaireService;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestionnaire")
public class GestionnaireController {

    @Autowired
    private GestionnaireService gestionnaireService;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    /**
     * Inscrit un nouvel antiprocrastinateur.
     * Reçoit les infos dans la requête, crée l'utilisateur avec son pseudo,
     * puis retourne l'antiprocrastinateur créé avec le code 201
     *
     * @param antiProcrastinateur les infos de l'antiprocrastinateur à créer
     * @return l'utilisateur créé avec un code 201
     */
    @PostMapping("/inscrire-antiprocrastinateur")
    public ResponseEntity<AntiProcrastinateur> inscrireAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        AntiProcrastinateur createdAntiprocrastinateur = gestionnaireService.creerAntiProcrastinateur(antiProcrastinateur.getPseudo());
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }

    /**
     * Crée un nouveau défi de procrastination
     * Reçoit les infos du défi (ID et titre) dans la requête
     * le crée via le service, puis retourne le défi créé avec le code 201
     *
     * @param defiProcrastination les infos du défi à créer
     * @return le défi créé avec un code 201 (CREATED)
     */
    @PostMapping("/creer-defi")
    public ResponseEntity<DefiProcrastination> creerDefiProcrastination(@RequestBody DefiProcrastination defiProcrastination) {
        DefiProcrastination creeDefiProcrastination = gestionnaireService.creerDefiProcrastinateur(defiProcrastination.getIdDefiProcrastination(), defiProcrastination.getTitre());
        return new ResponseEntity<>(creeDefiProcrastination, HttpStatus.CREATED);
    }

    /**
     * Crée un nouveau grand concours annuel.
     * Reçoit les infos du concours dans la requête, le crée via le service,
     * puis retourne le concours créé avec le code 201
     *
     * @param grandConcours les infos du grand concours à créer
     * @return le concours créé avec un code 201
     */
    @PostMapping("/organiser-grand-concours")
    public ResponseEntity<GrandConcours> creerGrandConcourAnnuel(@RequestBody GrandConcours grandConcours) {
        GrandConcours saved = gestionnaireService.creerGrandConcour(grandConcours);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
