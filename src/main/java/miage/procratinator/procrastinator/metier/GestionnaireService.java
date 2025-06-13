package miage.procratinator.procrastinator.metier;

import miage.procratinator.procrastinator.dao.AntiProcrastinateurRepository;
import miage.procratinator.procrastinator.dao.DefiProcrastinationRepository;
import miage.procratinator.procrastinator.dao.GrandConcoursRepository;
import miage.procratinator.procrastinator.dao.ProcrastinateurRepository;
import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.GrandConcours;
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.enumeration.Difficulte;
import miage.procratinator.procrastinator.entities.enumeration.Statut;
import miage.procratinator.procrastinator.utilities.UtilisateurCourant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

    @Autowired
    private ProcrastinateurRepository procrastinateurRepository;

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

    public DefiProcrastination creerDefiProcrastinateur(DefiProcrastination defiProcrastination) {
        List<DefiProcrastination> defiProcrastinations = defiProcrastinationRepository.findByIdDefiProcrastination(defiProcrastination.getIdDefiProcrastination());
        DefiProcrastination newDefiProcrastination;

        if (defiProcrastinations.isEmpty()) {
            newDefiProcrastination = new DefiProcrastination();
            newDefiProcrastination.setIdDefiProcrastination(defiProcrastination.getIdDefiProcrastination());
            newDefiProcrastination.setTitre(defiProcrastination.getTitre());
            newDefiProcrastination.setDateDebut(defiProcrastination.getDateDebut());
            newDefiProcrastination.setDateFin(defiProcrastination.getDateFin());
            newDefiProcrastination.setDescription(defiProcrastination.getDescription());
            newDefiProcrastination.setDuree(defiProcrastination.getDuree());
            newDefiProcrastination.setDifficulte(defiProcrastination.getDifficulte());
            newDefiProcrastination.setPointsAGagner(defiProcrastination.getPointsAGagner());
            newDefiProcrastination.setIdAntiProcrastinateur(defiProcrastination.getIdAntiProcrastinateur());
            newDefiProcrastination.setStatut(defiProcrastination.getStatut());
            newDefiProcrastination = defiProcrastinationRepository.save(newDefiProcrastination);
        } else {
            newDefiProcrastination = defiProcrastinations.getFirst();
        }
        return newDefiProcrastination;
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