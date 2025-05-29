package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcour;
import miage.procratinator.procrastinator.entities.Procrastinateur;
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

    @PostMapping("/inscrire-antiprocrastinateur")
    public ResponseEntity<AntiProcrastinateur> inscrireAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        AntiProcrastinateur createdAntiprocrastinateur = gestionnaireService.creerAntiProcrastinateur(antiProcrastinateur.getPseudo());
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }

    @PostMapping("/creer-defi")
    public ResponseEntity<DefiProcrastination> creerDefiProcrastination(@RequestBody DefiProcrastination defiProcrastination) {
        DefiProcrastination creeDefiProcrastination = gestionnaireService.creerDefiProcrastinateur(defiProcrastination.getIdDefiProcrastination(), defiProcrastination.getTitre());
        return new ResponseEntity<>(creeDefiProcrastination, HttpStatus.CREATED);
    }

    @PostMapping("/organiser-grand-concours")
    public ResponseEntity<GrandConcour> creerGrandConcourAnnuel(@RequestBody GrandConcour grandConcour) {
        GrandConcour saved = gestionnaireService.creerGrandConcour(grandConcour);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
