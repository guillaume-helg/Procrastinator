package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.StatutParticipationDefi;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VoteExcuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoteExcuse;
    private Long idUtilisateur;
    private Long idExcuse;
    private LocalDate dateVote;
}
