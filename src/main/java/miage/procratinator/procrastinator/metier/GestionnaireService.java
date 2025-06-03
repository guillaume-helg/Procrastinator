package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.AntiProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.DefiProcrastinationRepository;
import miage.procratinator.procrastinator.dao.GrandConcoursRepository;
import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionnaireService {

    @Autowired
    private AntiProcrastinateurRepository antiProcrastinateurRepository;

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    @Autowired
    private GrandConcoursRepository grandConcoursRepository;

    @Autowired
    private UtilisateurCourant utilisateurCourant;

    public AntiProcrastinateur creerAntiProcrastinateur(String pseudo) {
        List<AntiProcrastinateur> clients = antiProcrastinateurRepository.findByPseudo(pseudo);
        AntiProcrastinateur antiProcrastinateur;

        if (clients.isEmpty()) {
            antiProcrastinateur = new AntiProcrastinateur();
            antiProcrastinateur.setPseudo(pseudo);
            antiProcrastinateur = antiProcrastinateurRepository.save(antiProcrastinateur);
        } else {
            antiProcrastinateur = clients.getFirst();
        }
        return antiProcrastinateur;
    }

    public DefiProcrastination creerDefiProcrastinateur(Long id, String titre) {
        List<DefiProcrastination> defiProcrastinations = defiProcrastinationRepository.findByIdDefiProcrastination(id);
        DefiProcrastination defiProcrastination;

        if (defiProcrastinations.isEmpty()) {
            defiProcrastination = new DefiProcrastination();
            defiProcrastination.setIdDefiProcrastination(id);
            defiProcrastination.setTitre(titre);
            defiProcrastination = defiProcrastinationRepository.save(defiProcrastination);
        } else {
            defiProcrastination = defiProcrastinations.getFirst();
        }
        return defiProcrastination;
    }

    public GrandConcours creerGrandConcour(GrandConcours grandConcour) {

        List<GrandConcours> grandConcours = grandConcoursRepository.findGrandConcourByIdGrandConcour(grandConcour.getIdGrandConcour());
        GrandConcours nouveauGrandConcours;

        if (grandConcours.isEmpty()) {
            nouveauGrandConcours = new GrandConcours();
            nouveauGrandConcours.setNom(grandConcour.getNom());
            nouveauGrandConcours.setRecompense(grandConcour.getRecompense());
            nouveauGrandConcours.setDateDebut(grandConcour.getDateDebut());
            nouveauGrandConcours.setDateFin(grandConcour.getDateFin());
            nouveauGrandConcours = grandConcoursRepository.save(nouveauGrandConcours);
        } else {
            nouveauGrandConcours = grandConcours.getFirst();
        }
        return nouveauGrandConcours;
    }
}
