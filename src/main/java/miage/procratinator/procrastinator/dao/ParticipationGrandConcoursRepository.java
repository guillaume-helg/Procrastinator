package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.ParticipationDefi;
import miage.procratinator.procrastinator.entities.ParticipationGrandConcours;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipationGrandConcoursRepository extends CrudRepository<ParticipationGrandConcours, Long> {
    List<ParticipationGrandConcours> findParticipationDefiByIdGrandConcoursAndIdProcrastinateur(Long idGrandConcours, Long idProcrastinateur);
    List<ParticipationGrandConcours> findParticipationDefiByIdGrandConcours(Long idGrandConcours);
}
