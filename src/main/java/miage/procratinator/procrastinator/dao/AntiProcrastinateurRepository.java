package miage.procratinator.procrastinator.dao;

import miage.procratinator.procrastinator.entities.AntiProcrastinateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AntiProcrastinateurRepository extends CrudRepository<AntiProcrastinateur, Long> {

    List<AntiProcrastinateur> findByPseudo(String pseudo);
}
