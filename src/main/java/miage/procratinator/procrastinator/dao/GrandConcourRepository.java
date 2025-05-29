package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.GrandConcour;
import miage.procratinator.procrastinator.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GrandConcourRepository extends CrudRepository<GrandConcour, Integer> {
    List<GrandConcour> findGrandConcourByIdGrandConcour(Long idGrandConcour);
}
