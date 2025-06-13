package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.DefiProcrastination;
import miage.procratinator.procrastinator.entities.ParticipationDefi;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipationDefiRepository extends CrudRepository<ParticipationDefi, Long> {
    List<ParticipationDefi> findParticipationDefiByIdDefiProcrastinateur(Long idDefiProcrastination);
}
