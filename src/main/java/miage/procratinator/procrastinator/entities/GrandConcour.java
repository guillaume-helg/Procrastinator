package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class GrandConcour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrandConcour;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String recompense;
}
