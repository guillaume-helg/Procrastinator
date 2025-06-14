package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.PiegeProductiviteRepository;
import miage.procratinator.procrastinator.entities.PiegeProductivite;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntiprocrastinateurService {

    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;

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
                    BeanUtils.copyProperties(piegeProductivite, nouveauPiege);
                    return piegeProductiviteRepository.save(nouveauPiege);
                });
    }
}