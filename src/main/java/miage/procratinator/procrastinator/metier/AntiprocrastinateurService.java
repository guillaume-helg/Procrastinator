package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.PiegeProductiviteRepository;
import miage.procratinator.procrastinator.entities.PiegeProductivite;
import miage.procratinator.procrastinator.entities.PiegeProductivite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntiprocrastinateurService {

    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;


    public PiegeProductivite creerPiegeProductivite(Long idPiegeProductivite, String titre, String description) {
        List<PiegeProductivite> piegeProductivites = piegeProductiviteRepository.findByIdPiegeProductivite(idPiegeProductivite);
        PiegeProductivite piegeProductivite;

        if (piegeProductivites.isEmpty()) {
            piegeProductivite = new PiegeProductivite();
            piegeProductivite.setIdPiegeProductivite(idPiegeProductivite);
            piegeProductivite.setTitre(titre);
            piegeProductivite.setDescription(description);
            piegeProductivite.setTitre(titre);
            piegeProductivite = piegeProductiviteRepository.save(piegeProductivite);
        } else {
            piegeProductivite = piegeProductivites.getFirst();
        }
        return piegeProductivite;
    }
}
