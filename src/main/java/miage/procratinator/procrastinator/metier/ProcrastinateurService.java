package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.TachesAEviterRepository;
import miage.procratinator.procrastinator.entities.Consequence;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.TacheAEviter;
import miage.procratinator.procrastinator.entities.enumeration.DegresUrgence;
import miage.procratinator.procrastinator.entities.enumeration.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ProcrastinateurService {

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

    @Autowired
    private TachesAEviterRepository tachesAEviterRepository;

    public Procrastinateur createProcrastinateur(Long id) {
        List<Procrastinateur> procrastinateurs = procrastinateurRepository.findProcrastinateurByIdUtilisateur(id);
        Procrastinateur procrastinateur;

        if (procrastinateurs.isEmpty()) {
            procrastinateur = new Procrastinateur();
            procrastinateur = procrastinateurRepository.save(procrastinateur);
        } else {
            procrastinateur = procrastinateurs.getFirst();
        }
        return procrastinateur;
    }

    public TacheAEviter creerTacheAEviter(Long idTacheAEviter, Long idUtilisateur, DegresUrgence degresUrgence, Consequence consequence) {
        List<TacheAEviter> tacheAEviters = tachesAEviterRepository.findTacheAEviterByIdTacheAEviter(idTacheAEviter);
        TacheAEviter tacheAEviter;

        if (tacheAEviters.isEmpty()) {
            tacheAEviter = new TacheAEviter();
            tacheAEviter.setIdTacheAEviter(idTacheAEviter);
            tacheAEviter.setIdProcrastinateur(idUtilisateur);
            tacheAEviter.setDegresUrgence(degresUrgence);
            tacheAEviter.setConsequence(consequence);
            tacheAEviter.setDateCreation(new Date());
            tacheAEviter.setStatut(StatutTache.EN_ATTENTE);
            tacheAEviter = tachesAEviterRepository.save(tacheAEviter);
        } else {
            tacheAEviter = tacheAEviters.getFirst();
        }
        return tacheAEviter;
    }

    public List<TacheAEviter> getTachesByProcrastinateurId(Long idProcrastinateur) {
        return tachesAEviterRepository.findByIdProcrastinateur(idProcrastinateur);
    }

}
