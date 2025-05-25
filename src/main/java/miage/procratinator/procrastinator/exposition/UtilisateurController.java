package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.metier.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    @Autowired
    UtilisateurService utilisateurService;

    /*
    @PostMapping("/soumettreExcuse/{id}")
    public ResponseEntity<AntiProcrastinateur> inscrireAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        AntiProcrastinateur createdAntiprocrastinateur = utilisateurService.creerAntiProcrastinateur(antiProcrastinateur.getPseudo());
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }

    @PostMapping("/voterExcuse/{id}")
    public ResponseEntity<AntiProcrastinateur> inscrireAntiprocrastinateur(@RequestBody AntiProcrastinateur antiProcrastinateur) {
        AntiProcrastinateur createdAntiprocrastinateur = utilisateurService.creerAntiProcrastinateur(antiProcrastinateur.getPseudo());
        return new ResponseEntity<>(createdAntiprocrastinateur, HttpStatus.CREATED);
    }
     */
}
