package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.Recompense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecompenseRepository extends CrudRepository<Recompense, Long> {
    List<Recompense> findRecompenseByIdRecompense(Long idRecompense);
}
