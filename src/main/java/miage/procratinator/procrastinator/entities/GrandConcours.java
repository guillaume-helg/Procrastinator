package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class GrandConcours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrandConcour;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String recompense;
}
