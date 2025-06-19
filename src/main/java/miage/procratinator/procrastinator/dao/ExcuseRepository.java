package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.Excuse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExcuseRepository extends CrudRepository<Excuse, Long> {
    List<Excuse> findExcuseByIdExcuse(Long idExcuse);
    List<Excuse> findAll();
    List<Excuse> findAllByOrderByVotesRecusDesc();
}
