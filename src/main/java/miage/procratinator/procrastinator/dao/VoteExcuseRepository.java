package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.VoteExcuse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteExcuseRepository extends CrudRepository<VoteExcuse, Long> {
    List<VoteExcuse> findVoteExcuseByIdUtilisateurAndIdExcuse(Long idUtilisateur, Long idExcuse);
}
