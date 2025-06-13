package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    List<Utilisateur> findUtilisateurByMail(String mail);

    List<Utilisateur> findAll();

    List<Utilisateur> findUtilisateurByPseudo(String pseudo);
}
