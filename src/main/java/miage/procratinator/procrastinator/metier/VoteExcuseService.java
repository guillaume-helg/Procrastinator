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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Crée une nouvelle excuse si elle n'existe pas dans le dépôt ou retourne une excuse existante. 
     *
     * Cette méthode vérifie d'abord si l'excuse fournie est nulle, levant une exception dans ce cas.
     * Elle vérifie également si l'utilisateur connecté est un procrastinateur. Si ces conditions 
     * sont respectées, elle tente de retrouver l'excuse dans le dépôt. Si cette excuse n'existe pas, 
     * elle enregistre une nouvelle excuse avec les informations fournies.
     *
     * @param excuse l'objet Excuse à créer ou vérifier, contenant les détails de l'excuse
     * @return l'excuse créée ou déjà existante dans le dépôt
     * @throws IllegalArgumentException si l'excuse est nulle ou si l'utilisateur n'est pas un procrastinateur
     */
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

    /**
     * Récupère toutes les excuses disponibles, triées par le nombre de votes reçus en ordre décroissant.
     *
     * @return une liste d'excuses triée par votes reçus de la plus votée à la moins votée
     */
    public List<Excuse> classement() {
        return excuseRepository.findAllByOrderByVotesRecusDesc();
    }

    /**
     * Met à jour le compteur de votes pour une excuse spécifique si l'excuse existe et que l'utilisateur n'a pas déjà voté pour celle-ci.
     * <p>
     * Cette méthode vérifie que l'ID de l'excuse n'est pas nul et que l'utilisateur actuellement connecté
     * n'a pas déjà voté pour l'excuse spécifiée. Si l'excuse existe, son compteur de votes est incrémenté
     * et le vote est enregistré dans le système. Des exceptions sont levées si l'ID de l'excuse est nul,
     * si l'excuse n'existe pas ou si l'utilisateur a déjà voté pour l'excuse.
     *
     * @param idExcuse l'ID de l'excuse pour laquelle mettre à jour le compteur de votes
     * @return un ResponseEntity contenant un message de succès lorsque le vote est enregistré
     * @throws IllegalArgumentException si l'ID de l'excuse est nul, si l'excuse n'existe pas ou si l'utilisateur a déjà voté
     */
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
                .orElseThrow(
                        () -> new IllegalArgumentException("L'excuse n'existe pas")
                );
    }

    /**
     * Vérifie si un utilisateur a déjà voté pour une excuse donnée.
     *
     * @param idUtilisateur l'identifiant unique de l'utilisateur
     * @param idExcuse l'identifiant unique de l'excuse
     * @return true si l'utilisateur a déjà voté pour l'excuse, false sinon
     */
    private boolean utilisateurDejaVoter(Long idUtilisateur, Long idExcuse) {
        return voteExcuseRepository.findVoteExcuseByIdUtilisateurAndIdExcuse(idUtilisateur, idExcuse).stream().findFirst().isPresent();
    }
}