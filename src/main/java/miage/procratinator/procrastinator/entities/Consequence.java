package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.NiveauRecompense;
import miage.procratinator.procrastinator.entities.enumeration.TypeRecompense;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Consequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsequence;
    private String titre;
    private String description;
}
