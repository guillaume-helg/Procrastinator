package miage.procratinator.procrastinator.exposition;

import jakarta.servlet.http.HttpSession;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.Recompense;
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

    /**
     * Crée un nouveau procrastinateur et l'enregistre dans le système.
     *
     * @param procrastinateur l'objet Procrastinateur représentant les informations du procrastinateur à créer
     * @return un ResponseEntity contenant l'objet Procrastinateur créé, accompagné du statut HTTP CREATED
     */
    @PostMapping("/inscrire")
    public ResponseEntity<Procrastinateur> createProcrastinateur(@RequestBody Procrastinateur procrastinateur) {
        Procrastinateur savedProcrastinateur = procrastinateurService.createProcrastinateur(procrastinateur);
        return new ResponseEntity<>(savedProcrastinateur, HttpStatus.CREATED);
    }

    /**
     * Crée une nouvelle tâche que le procrastinateur souhaite éviter
     * et enregistre cette tâche dans le système.
     *
     * @param tacheAEviter l'objet TacheAEviter représentant les détails de la tâche à éviter
     * @return un ResponseEntity contenant l'objet TacheAEviter créé,
     *         accompagné du statut HTTP CREATED
     */
    @PostMapping("/ajouterTache")
    public ResponseEntity<TacheAEviter> creerTacheAEviter(@RequestBody TacheAEviter tacheAEviter) {
        TacheAEviter creeTacheAEviter = procrastinateurService.creerTacheAEviter(tacheAEviter);
        return new ResponseEntity<>(creeTacheAEviter, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste des tâches que le procrastinateur actuel
     * doit éviter ou accomplir, identifiées pour son compte.
     *
     * @return un ResponseEntity contenant une liste d'objets TacheAEviter,
     *         avec un statut HTTP OK si la récupération est réussie
     */
    @GetMapping("/taches")
    public ResponseEntity<List<TacheAEviter>> getTachesByProcrastinateur() {
        List<TacheAEviter> taches = procrastinateurService.getTachesByProcrastinateurId();
        return new ResponseEntity<>(taches, HttpStatus.OK);
    }

    /**
     * Met à jour le statut d'une tâche donnée pour le procrastinateur actuel.
     *
     * @param tacheAEviter l'objet TacheAEviter contenant les informations nécessaires à la mise à jour,
     *                     y compris l'identifiant et le nouveau statut de la tâche
     * @return un ResponseEntity indiquant le résultat de l'opération, accompagné d'un statut HTTP approprié
     */
    @PostMapping("/updateTache")
    public ResponseEntity<?> updateStatutTache(@RequestBody TacheAEviter tacheAEviter) {
        return procrastinateurService.updateStatutTache(tacheAEviter);
    }

    /**
     * Permet à l'utilisateur actuel de participer à un défi de procrastination
     * identifié par son identifiant. Cette action peut influencer les statistiques
     * et la progression de l'utilisateur dans le système.
     *
     * @param idDefi l'identifiant du défi de procrastination auquel participer
     * @return un ResponseEntity contenant des informations sur le résultat de la participation,
     *         y compris un statut HTTP approprié
     */
    @PostMapping("/participerDefi/{idDefi}")
    public ResponseEntity<?> participerDefiProcrastination(@PathVariable Long idDefi) {
        return procrastinateurService.participerDefi(idDefi);
    }

    /**
     * Permet au procrastinateur actuel de valider un défi de procrastination
     * identifié par son identifiant. Une fois validé, le défi est considéré
     * comme accompli, ce qui peut entraîner des effets métier tels que le gain
     * de récompenses ou points.
     *
     * @param idDefi l'identifiant du défi de procrastination à valider
     * @return un ResponseEntity indiquant le résultat de l'opération,
     *         avec un statut approprié et des informations liées à la validation
     */
    @PostMapping("/validerDefi/{idDefi}")
    public ResponseEntity<?> validerDefiProcrastination(@PathVariable Long idDefi) {
        return procrastinateurService.validerDefi(idDefi);
    }

    /**
     * Permet au procrastinateur actuel d'éviter un piège de productivité
     * identifié par son identifiant. Cette action met le piège hors service
     * et peut offrir des récompenses telles que des points accumulés.
     *
     * @param idPiege l'identifiant du piège de productivité à éviter
     * @return un message indiquant que le piège a été évité avec succès
     */
    @PostMapping("/eviter/{idPiege}")
    public String eviterPiegeProductivite(@PathVariable Long idPiege) {
        return procrastinateurService.eviterLePiege(idPiege);
    }

    /**
     * Permet au procrastinateur actuel de tomber dans un piège de productivité
     * identifié par son identifiant. Cette action peut entraîner une perte de points
     * accumulés et d'autres conséquences métier liées.
     *
     * @param idPiege l'identifiant du piège de productivité
     * @return un message indiquant que l'utilisateur est tombé dans le piège
     */
    @PostMapping("/tomber/{idPiege}")
    public String tomberPiegeProductivite(@PathVariable Long idPiege) {
        return procrastinateurService.tomberDansPiege(idPiege);
    }

    /**
     * Valide une excuse spécifique identifiée par son identifiant.
     *
     * @param id l'identifiant de l'excuse à valider
     * @return un ResponseEntity contenant l'excuse mise à jour avec son statut modifié
     */
    @PostMapping("/validerExcuse/{id}")
    public ResponseEntity<?> validerExcuse(@PathVariable Long id) {
        return new ResponseEntity<>(procrastinateurService.validerExcuse(id), HttpStatus.OK);
    }

    /**
     * Rejette une excuse spécifique identifiée par son identifiant.
     *
     * @param id l'identifiant de l'excuse à rejeter
     * @return un ResponseEntity contenant l'excuse mise à jour avec son statut modifié
     */
    @PostMapping("/rejeterExcuse/{id}")
    public ResponseEntity<?> rejeterExcuse(@PathVariable Long id) {
        return new ResponseEntity<>(procrastinateurService.rejeterExcuse(id), HttpStatus.OK);
    }

    /**
     * Récupère les récompenses associées au procrastinateur actuel.
     *
     * @return un ResponseEntity contenant la liste des récompenses de l'utilisateur connecté
     */
    @GetMapping("/recompenses")
    public ResponseEntity<?> getRecompensesProcrastinateur() {
        return procrastinateurService.getRecompensesUtilisateur();
    }
}
