package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.DegresUrgence;
import miage.procratinator.procrastinator.entities.enumeration.StatutTache;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TacheAEviter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTacheAEviter;

    private Long idProcrastinateur;

    private String description;

    @Enumerated(EnumType.STRING)
    private DegresUrgence degresUrgence;

    private LocalDate dateLimite;

    private String consequence;

    @Enumerated(EnumType.STRING)
    private StatutTache statut;

    private LocalDate dateCreation;
}
