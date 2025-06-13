package miage.procratinator.procrastinator.exposition;

import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, HttpSession session) {
        return procrastinateurService.loginProcrastinateur(email, session);
    }

    @PostMapping("/inscrire")
    public ResponseEntity<Procrastinateur> createProcrastinateur(@RequestBody Procrastinateur procrastinateur) {
        Procrastinateur savedProcrastinateur = procrastinateurService.createProcrastinateur(procrastinateur);
        return new ResponseEntity<>(savedProcrastinateur, HttpStatus.CREATED);
    }

    @PostMapping("/ajouterTache")
    public ResponseEntity<TacheAEviter> creerTacheAEviter(@RequestBody TacheAEviter tacheAEviter) {
        TacheAEviter creeTacheAEviter = procrastinateurService.creerTacheAEviter(tacheAEviter);
        return new ResponseEntity<>(creeTacheAEviter, HttpStatus.CREATED);
    }

    @GetMapping("/taches")
    public ResponseEntity<List<TacheAEviter>> getTachesByProcrastinateur() {
        List<TacheAEviter> taches = procrastinateurService.getTachesByProcrastinateurId();
        return new ResponseEntity<>(taches, HttpStatus.OK);
    }

    @PostMapping("/updateTache")
    public ResponseEntity<?> updateStatutTache(@RequestBody TacheAEviter tacheAEviter) {
        return procrastinateurService.updateStatutTache(tacheAEviter);
    }

    @PostMapping("/participerDefi/{idDefi}")
    public ResponseEntity<?> participerDefiProcrastination(@PathVariable Long idDefi) {
        return procrastinateurService.participerDefi(idDefi);
    }


    public void eviterPiegeProductivite() {

    }
}
