package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.PiegeProductivite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PiegeProductiviteRepository extends CrudRepository<PiegeProductivite, Long> {
    List<PiegeProductivite> findByIdPiegeProductivite(Long idPiegeProductivite);
    List<PiegeProductivite> findAll();

}
