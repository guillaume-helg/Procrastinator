package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.StatutParticipationDefi;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ParticipationGrandConcours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParticipationGrandConcours;
    private Long idGrandConcours;
    private Long idProcrastinateur;
    private LocalDate dateInscription;
    private StatutParticipationDefi statutParticipationGrandConcours;
}
