package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.GrandConcours;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GrandConcoursRepository extends CrudRepository<GrandConcours, Integer> {
    List<GrandConcours> findGrandConcourByIdGrandConcours(Long idGrandConcour);
}
