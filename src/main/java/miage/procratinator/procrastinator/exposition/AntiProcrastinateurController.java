package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.PiegeProductivite;
import miage.procratinator.procrastinator.metier.AntiprocrastinateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/antiprocrastinateur")
public class AntiProcrastinateurController {

    @Autowired
    AntiprocrastinateurService antiProcrastinateurService;

    /**
     * Crée un nouveau piège de productivité
     * Reçoit les infos du piège dans la requête et le crée avec le service
     * Retourne le piège créé avec le code 201 (CREATED)
     *
     * @param piegeProductivite le piège à créer
     * @return le piège créé
     */
    @PostMapping("/creerPiegeProductivite")
    public ResponseEntity<?> creerPiegeProductivite(@RequestBody PiegeProductivite piegeProductivite) {
        PiegeProductivite creePiegeProductivite = antiProcrastinateurService.creerPiegeProductivite(piegeProductivite);
        return new ResponseEntity<>(creePiegeProductivite, HttpStatus.CREATED);
    }

    @GetMapping("/analyse/{id}")
    public ResponseEntity<?> getAnalyse(@PathVariable Long idUtilisateur) {
        return new ResponseEntity<>(antiProcrastinateurService.getAnalyse(idUtilisateur), HttpStatus.CREATED);
    }

    @GetMapping("/analyse")
    public ResponseEntity<?> getAnalyse() {
        return new ResponseEntity<>(antiProcrastinateurService.getAnalyse(), HttpStatus.CREATED);
    }
}
