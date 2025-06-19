package miage.procratinator.procrastinator.exposition;

import miage.procratinator.procrastinator.entities.Excuse;
import miage.procratinator.procrastinator.metier.VoteExcuseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excuse")
public class VoteExcuseController {

    @Autowired
    private VoteExcuseService voteExcuseService;

    /**
     * Crée une nouvelle excuse en sauvegardant les informations fournies.
     *
     * @param excuse l'objet Excuse à créer, contenant les détails tels que le texte,
     *               la situation, la catégorie, et les informations sur le procrastinateur
     * @return un ResponseEntity contenant l'objet Excuse créé et le statut HTTP 201 (Created)
     */
    @PostMapping("/cree")
    public ResponseEntity<Excuse> creerExcuse(@RequestBody Excuse excuse) {
        Excuse savedExcuse = voteExcuseService.creerExcuse(excuse);
        return new ResponseEntity<>(savedExcuse, HttpStatus.CREATED);
    }

    /**
     * Récupère le classement des excuses basé sur le nombre de votes reçus.
     *
     * @return un ResponseEntity contenant la liste des excuses triées par votes décroissants
     */
    @GetMapping()
    public ResponseEntity<?> classementExcuse() {
        return ResponseEntity.ok(voteExcuseService.classement());
    }

    /**
     * Enregistre un vote pour une excuse spécifique.
     *
     * @param id l'identifiant de l'excuse pour laquelle l'utilisateur souhaite voter
     * @return un ResponseEntity contenant une réponse indiquant le succès du vote ou des informations d'erreur
     */
    @PostMapping("/voter/{id}")
    public ResponseEntity<?> voterExcuse(@PathVariable Long id) {
        return voteExcuseService.updateVoteExcuse(id);
    }
}
