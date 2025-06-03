package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.Procrastinateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProcrastinateurRepository extends CrudRepository<Procrastinateur, Long> {
    List<Procrastinateur> findProcrastinateurByMail(String email);
    List<Procrastinateur> findProcrastinateurByPseudo(String pseudo);
}
