package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.Procrastinateur;
import miage.procratinator.procrastinator.entities.TacheAEviter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TachesAEviterRepository extends CrudRepository<TacheAEviter, Long> {
    List<TacheAEviter> findTacheAEviterByIdTacheAEviter(Long idTacheAEviter);
}
