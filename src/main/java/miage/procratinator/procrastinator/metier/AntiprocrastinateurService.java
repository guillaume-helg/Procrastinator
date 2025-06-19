package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.PiegeProductiviteRepository;
import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.entities.PiegeProductivite;
import miage.procratinator.procrastinator.entities.enumeration.Statut;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AntiprocrastinateurService {

    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

    /**
     * Crée un nouveau piège de productivité ou récupère un piège existant si un piège avec le même identifiant existe déjà.
     *
     * @param piegeProductivite l'objet contenant les informations du piège de productivité à créer
     * @return l'objet PiegeProductivite créé ou existant
     */
    public PiegeProductivite creerPiegeProductivite(PiegeProductivite piegeProductivite) throws IllegalArgumentException {
        if (piegeProductivite == null) {
            throw new IllegalArgumentException("piegeProductivite ne peut pas être null");
        }

        return piegeProductiviteRepository.findByIdPiegeProductivite(piegeProductivite.getIdPiegeProductivite())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    PiegeProductivite nouveauPiege = new PiegeProductivite();
                    piegeProductivite.setIdAntiProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
                    piegeProductivite.setDateCreation(LocalDate.now());
                    piegeProductivite.setStatut(Statut.ACTIF);
                    BeanUtils.copyProperties(piegeProductivite, nouveauPiege);
                    return piegeProductiviteRepository.save(nouveauPiege);
                });
    }

    /**
     * Récupère une analyse des pièges de productivité pour un utilisateur spécifique basée sur l'ID utilisateur fourni.
     * L'analyse inclut le nombre total de pièges de productivité et le nombre de pièges actifs.
     * Si aucun piège n'est trouvé pour l'utilisateur, un message approprié est retourné.
     *
     * @param idUtilisateur L'ID de l'utilisateur dont les pièges de productivité doivent être analysés.
     *                      Ne doit pas être null.
     * @return Un ResponseEntity contenant l'analyse sous forme de chaîne.
     * Si l'utilisateur n'a pas de pièges, un message l'indiquant est retourné.
     * @throws IllegalArgumentException Si l'ID utilisateur fourni est null.
     */
    public ResponseEntity<?> getAnalyse(Long idUtilisateur) {
        if (procrastinateurRepository.findProcrastinateurByIdUtilisateur(idUtilisateur).isEmpty() || idUtilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }

        List<PiegeProductivite> pieges = piegeProductiviteRepository.findAll();
        long totalPieges = pieges.size();

        if (totalPieges == 0) {
            return ResponseEntity.ok("Aucun piège créé");
        }

        long piegesActifs = piegeProductiviteRepository.countByIdProcrastinateurAndStatut(idUtilisateur, Statut.ACTIF);
        long piegesInactifs = piegeProductiviteRepository.countByIdProcrastinateurAndStatut(idUtilisateur, Statut.INACTIF);

        return ResponseEntity.ok("Total pièges : " + totalPieges + "; Pièges actifs : " + piegesActifs + "; Pieges inactifs : " + piegesInactifs);
    }
}