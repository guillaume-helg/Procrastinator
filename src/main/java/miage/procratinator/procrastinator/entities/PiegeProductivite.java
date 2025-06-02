package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;
import miage.procratinator.procrastinator.entities.enumeration.Statut;
import miage.procratinator.procrastinator.entities.enumeration.Type;

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
    @ManyToOne
    @JoinColumn(name = "idRecompense")
    private Recompense recompense;
    private Date dateCreation;
    private Statut statut;



}


