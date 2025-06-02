package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.TacheAEviter;
import miage.procratinator.procrastinator.metier.ProcrastinateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procrastinateur")
public class ProcrastinateurController {

    @Autowired
    private ProcrastinateurService procrastinateurService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Procrastinator here to help!";
    }

    @GetMapping("/inscrire")
    public String sayInscrire() {
        return "Procrastinator want to subscribe!";
    }

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

    @GetMapping("/{idProcrastinateur}/taches")
    public ResponseEntity<List<TacheAEviter>> getTachesByProcrastinateur(@PathVariable Long idProcrastinateur) {
        List<TacheAEviter> taches = procrastinateurService.getTachesByProcrastinateurId(idProcrastinateur);
        return new ResponseEntity<>(taches, HttpStatus.OK);
    }

    public void participerDefiProcrastination() {

    }

    public void eviterPiegeProductivite() {

    }
}
