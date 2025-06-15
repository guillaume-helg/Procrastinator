package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.entities.Recompense;
import miage.procratinator.procrastinator.metier.GestionnaireService;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    @Autowired
    private GestionnaireService gestionnaireService;

    @PostMapping("/antiprocrastinateurs")
    public ResponseEntity<AntiProcrastinateur> inscrireAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        if (antiProcrastinateur == null || antiProcrastinateur.getPseudo().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        AntiProcrastinateur createdAntiprocrastinateur = gestionnaireService.creerAntiProcrastinateur(antiProcrastinateur);
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }

    @PostMapping("/defis")
    public ResponseEntity<DefiProcrastination> creerDefiProcrastination(@RequestBody DefiProcrastination defiProcrastination) {
        DefiProcrastination createdDefiProcrastination = gestionnaireService.creerDefiProcrastinateur(defiProcrastination);
        return new ResponseEntity<>(createdDefiProcrastination, HttpStatus.CREATED);
    }

    @PostMapping("/concours")
    public ResponseEntity<GrandConcours> creerGrandConcoursAnnuel(@RequestBody GrandConcours grandConcours) {
        GrandConcours savedConcours = gestionnaireService.creerGrandConcours(grandConcours);
        return new ResponseEntity<>(savedConcours, HttpStatus.CREATED);
    }

    @PostMapping("/recompense")
    public ResponseEntity<Recompense> creerRecompense(@RequestBody Recompense recompense) {
        Recompense savedRecompense = gestionnaireService.creerRecompense(recompense);
        return new ResponseEntity<>(savedRecompense, HttpStatus.CREATED);
    }
}