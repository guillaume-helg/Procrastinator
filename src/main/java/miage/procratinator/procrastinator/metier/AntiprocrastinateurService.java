package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.PiegeProductiviteRepository;
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

    /**
     * Crée un nouveau piège de productivité ou récupère un piège existant si un piège avec le même identifiant existe déjà.
     *
     * @param piegeProductivite l'objet contenant les informations du piège de productivité à créer
     * @return l'objet PiegeProductivite créé ou existant
     */
    public PiegeProductivite creerPiegeProductivite(PiegeProductivite piegeProductivite) {
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
     *
     */
    public ResponseEntity<?> getAnalyse(Long idUtilisateur) {
        if (idUtilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur peut pas être null");
        }

        List<PiegeProductivite> pieges = piegeProductiviteRepository.findAll();
        long totalPieges = pieges.stream()
                .filter(p -> p.getIdAntiProcrastinateur().equals(idUtilisateur))
                .count();

        if (totalPieges == 0) {
            return ResponseEntity.ok("Aucun piège créé pour cet utilisateur");
        }

        long piegesActifs = pieges.stream()
                .filter(p -> p.getIdAntiProcrastinateur().equals(idUtilisateur))
                .filter(p -> p.getStatut() == Statut.ACTIF)
                .count();

        return ResponseEntity.ok("Total pièges : " + totalPieges + ", Pièges actifs : " + piegesActifs);
    }

    /**
     *
     * @return
     */
    public ResponseEntity<?> getAnalyse() {
        List<PiegeProductivite> pieges = piegeProductiviteRepository.findAll();
        long totalPieges = pieges.size();

        long piegesActifs = pieges.stream()
                .filter(p -> p.getStatut() == Statut.ACTIF)
                .count();

        return ResponseEntity.ok("Total pièges : " + totalPieges + ", Pièges actifs : " + piegesActifs);
    }
}