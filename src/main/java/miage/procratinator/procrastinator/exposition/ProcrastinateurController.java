package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.TacheAEviter;
import miage.procratinator.procrastinator.metier.ProcrastinateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/procrastinateur")
public class ProcrastinateurController {

    @Autowired
    private ProcrastinateurService procrastinateurService;

    @PostMapping("/inscrire")
    public ResponseEntity<Procrastinateur> createProcrastinateur(@RequestBody Procrastinateur procrastinateur) {
        Procrastinateur savedProcrastinateur = procrastinateurService.createProcrastinateur(procrastinateur.getIdUtilisateur());
        return new ResponseEntity<>(savedProcrastinateur, HttpStatus.CREATED);
    }

    @PostMapping("/ajouterTache")
    public ResponseEntity<TacheAEviter> creerTacheAEviter(@RequestBody TacheAEviter tacheAEviter) {
        TacheAEviter creeTacheAEviter = procrastinateurService.creerTacheAEviter(tacheAEviter.getIdTacheAEviter(), tacheAEviter.getIdProcrastinateur(), tacheAEviter.getDegresUrgence(), tacheAEviter.getConsequence());
        return new ResponseEntity<>(creeTacheAEviter, HttpStatus.CREATED);
    }

    public void participerDefiProcrastination() {

    }

    public void eviterPiegeProductivite() {

    }
}
