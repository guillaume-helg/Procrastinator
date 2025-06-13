package miage.procratinator.procrastinator.utilities;

import miage.procratinator.procrastinator.dao.UtilisateurRepository;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    /**
     * Initialise un utilisateur "gestionnaire" dans la base de données s'il n'existe pas déjà.
     * Cet utilisateur est configuré avec des attributs spécifiques comme l'email, le pseudo et la date d'inscription.
     *
     * @param utilisateurRepository le repository utilisé pour interroger et sauvegarder l'utilisateur "gestionnaire"
     * @return une instance de CommandLineRunner qui exécute la logique d'initialisation au démarrage de l'application
     */
    @Bean
    CommandLineRunner initGestionnaire(UtilisateurRepository utilisateurRepository) {
        return args -> {
            String email = "gestionnaire@procrastinapp.fr";
            if (utilisateurRepository.findUtilisateurByMail(email).isEmpty()) {
                Utilisateur gestionnaire = new Utilisateur();
                gestionnaire.setPseudo("Le Grand Gestionnaire");
                gestionnaire.setMail(email);
                gestionnaire.setDateInscription(LocalDate.now().minusMonths(6));
                utilisateurRepository.save(gestionnaire);
                System.out.println("Creation du Big Boss");
            }
        };
    }
}

