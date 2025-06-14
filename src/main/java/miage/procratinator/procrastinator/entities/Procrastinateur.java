package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.NiveauProcrastination;


@Getter
@Setter
@Entity
public class Procrastinateur extends Utilisateur {

    @Enumerated(EnumType.STRING)
    private NiveauProcrastination niveauProcrastination;

    private int pointsAccumules;

    @ManyToOne
    @JoinColumn(name = "idExcuse")
    private Excuse excusePreferee;
}

