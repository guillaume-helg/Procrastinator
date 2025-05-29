package miage.procratinator.procrastinator.utilities;

import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import miage.procratinator.procrastinator.entities.enumeration.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initGestionnaire(UtilisateurRepository utilisateurRepository) {
        return args -> {
            String email = "gestionnaire@procrastinapp.fr";
            if (utilisateurRepository.findUtilisateurByMail(email).isEmpty()) {
                Utilisateur gestionnaire = new Utilisateur();
                gestionnaire.setPseudo("Le Grand Gestionnaire");
                gestionnaire.setMail(email);
                gestionnaire.setRole(Role.GESTIONNAIRE);
                gestionnaire.setNiveauProcrastination(NiveauProcrastination.EXPERT);
                gestionnaire.setDateInscription(LocalDate.now().minusMonths(6));
                gestionnaire.setPointsAccumules(5000);
                utilisateurRepository.save(gestionnaire);
                System.out.println("Gestionnaire initialisé");
            }
        };
    }
}

