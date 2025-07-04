package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import miage.procratinator.procrastinator.entities.enumeration.Statut;
import miage.procratinator.procrastinator.entities.enumeration.Type;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PiegeProductivite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPiegeProductivite;
    private String titre;
    private String description;
    private Type type;
    private Long idAntiProcrastinateur;
    private NiveauProcrastination niveauProcrastination;
    private Long recompense;
    private LocalDate dateCreation;
    private Statut statut;
    private Long idProcrastinateur; // procrastinateur ayant esquivé le piège
}


