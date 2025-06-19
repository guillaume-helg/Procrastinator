package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import miage.procratinator.procrastinator.entities.AttributionRecompense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttributionRecompenseRepository extends CrudRepository<AttributionRecompense, Long> {
    List<AttributionRecompense> findAttributionRecompensesByIdProcrastinateur(Long idProcrastinateur);
}
