package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.DefiProcrastination;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DefiProcrastinationRepository extends CrudRepository<DefiProcrastination, Long> {
    List<DefiProcrastination> findByIdDefiProcrastination(Long idDefiProcrastination);
}
