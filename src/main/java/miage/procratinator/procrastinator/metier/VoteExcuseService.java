package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.ExcuseRepository;
import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.Excuse;
import miage.procratinator.procrastinator.entities.enumeration.StatutExcuse;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VoteExcuseService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ExcuseRepository excuseRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;



    public Excuse createExcuse(Excuse excuse) {
        if (excuse == null) {
            throw new IllegalArgumentException("L'excuse ne peut pas être null");
        }
        return excuseRepository
                .findExcuseByIdExcuse(excuse.getIdExcuse())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Excuse nouvelleExcuse = new Excuse();
                    excuse.setDateCreation(LocalDate.now());
                    excuse.setIdProcrastinateur(utilisateurCourant.getUtilisateurConnecte().getIdUtilisateur());
                    excuse.setStatut(StatutExcuse.EN_ATTENTE);
                    BeanUtils.copyProperties(excuse, nouvelleExcuse);
                    return excuseRepository.save(nouvelleExcuse);
                });
    }
}
