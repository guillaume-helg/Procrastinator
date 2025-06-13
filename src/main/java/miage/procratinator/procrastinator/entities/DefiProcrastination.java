package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.Difficulte;
import miage.procratinator.procrastinator.entities.enumeration.Statut;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DefiProcrastination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDefiProcrastination;
    private String titre;
    private String description;
    private Float duree;
    private Difficulte difficulte;
    private int pointsAGagner;
    private Long idGestionnaire;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Statut statut;
}