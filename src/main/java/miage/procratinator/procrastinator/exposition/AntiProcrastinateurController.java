package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.DefiProcrastination;
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

    @GetMapping("/hello")
    public String sayHello() {
        return "Anti-procrastinator here to help!";
    }

    @PostMapping("/creerPiegeProductivite")
    public ResponseEntity<DefiProcrastination> creerPiegeProductivite(@RequestBody PiegeProductivite piegeProductivite) {
        DefiProcrastination creeDefiProcrastination = antiProcrastinateurService.creerPiegeProductivite(piegeProductivite.getIdPiegeProductivite(), piegeProductivite.getTitre(), piegeProductivite.getDescription());
        return new ResponseEntity<>(creeDefiProcrastination, HttpStatus.CREATED);
    }

}
