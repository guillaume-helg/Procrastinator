package miage.procratinator.procrastinator.utilities;

import miage.procratinator.procrastinator.dao.RecompenseRepository;
import miage.procratinator.procrastinator.dao.UtilisateurRepository;
<<<<<<< Updated upstream
import miage.procratinator.procrastinator.entities.Excuse;
=======
>>>>>>> Stashed changes
import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.Recompense;
import miage.procratinator.procrastinator.entities.Utilisateur;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import miage.procratinator.procrastinator.entities.enumeration.NiveauRecompense;
import miage.procratinator.procrastinator.entities.enumeration.TypeRecompense;
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
    CommandLineRunner initGestionnaire(UtilisateurRepository utilisateurRepository, RecompenseRepository recompenseRepository) {
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

            Recompense recompense = new Recompense();
            recompense.setNiveauRecompense(NiveauRecompense.PAPIER_MACHE);
            recompense.setTitre("Procrastinateur en Danger");
            recompense.setDescription("Tombe dans un piège");
            recompense.setConditionObtention("Tombe dans un piege");
            recompense.setTypeRecompense(TypeRecompense.BADGE);
        };
    }

    @Bean
    CommandLineRunner initProcrastinateur(UtilisateurRepository utilisateurRepository) {
        return args -> {
            String email = "veteran@procrastinapp.fr";
            if (utilisateurRepository.findUtilisateurByMail(email).isEmpty()) {
                Procrastinateur veteran = new Procrastinateur();
                veteran.setPseudo("Véteran Brian");
                veteran.setMail(email);
                veteran.setNiveauProcrastination(NiveauProcrastination.INTERMEDIAIRE);
<<<<<<< Updated upstream
                veteran.setPointsAccumules(4950);
                veteran.setExcusePreferee("Je peux pas j'ai acqua poney");
=======
                veteran.setPointsAccumules(4850);
>>>>>>> Stashed changes
                veteran.setDateInscription(LocalDate.now().minusMonths(8));
                utilisateurRepository.save(veteran);
                System.out.println("Creation du Veteran");
            }
        };
    }
}

