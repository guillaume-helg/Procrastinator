package miage.procratinator.procrastinator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
