package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.ExcuseRepository;
import miage.procratinator.procrastinator.dao.VoteExcuseRepository;
import miage.procratinator.procrastinator.entities.*;
import miage.procratinator.procrastinator.entities.enumeration.StatutExcuse;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoteExcuseService {

    @Autowired
    private ExcuseRepository excuseRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    @Autowired
    private VoteExcuseRepository voteExcuseRepository;

    
    public Excuse creerExcuse(Excuse excuse) {
        if (excuse == null) {
            throw new IllegalArgumentException("L'excuse ne peut pas être null");
        }
        
        if (!utilisateurCourant.estProcrastinateur()) {
            throw new IllegalArgumentException("L'utilisateur n'est pas procrastinateur");
        }
        
        return excuseRepository
                .findExcuseByIdExcuse(excuse.getIdExcuse())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Excuse nouvelleExcuse = new Excuse();
                    excuse.setDateSoumission(LocalDate.now());
                    excuse.setIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
                    excuse.setStatut(StatutExcuse.EN_ATTENTE);
                    BeanUtils.copyProperties(excuse, nouvelleExcuse);
                    return excuseRepository.save(nouvelleExcuse);
                });
    }

    public List<Excuse> classement() {
        return excuseRepository.findAll().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getVotesRecus(), e1.getVotesRecus()))
            .toList();
    }

    public ResponseEntity<?> updateVoteExcuse(Long idExcuse) {
        if (idExcuse == null) {
            throw new IllegalArgumentException("Id de l'excuse ne doit pas etre nul");
        }

        if (utilisateurDejaVoter(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur(), idExcuse)) {
            throw new IllegalArgumentException("Utilisateur a déjà voté pour cette excuse");
        }

        return excuseRepository.findExcuseByIdExcuse(idExcuse).stream()
                .findFirst()
                .map(excuse -> {
                    excuse.setVotesRecus(excuse.getVotesRecus() + 1);
                    excuseRepository.save(excuse);
                    voteExcuseRepository.save(
                            new VoteExcuse(null,
                                    utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur(),
                                    idExcuse,
                                    LocalDate.now()));
                    return ResponseEntity.ok("+1 vote");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Excuse non trouvée"));
    }

    private boolean utilisateurDejaVoter(Long idUtilisateur, Long idExcuse) {
        return voteExcuseRepository.findVoteExcuseByIdUtilisateurAndIdExcuse(idUtilisateur, idExcuse).stream().findFirst().isPresent();
    }
}