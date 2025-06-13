package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;
    private String pseudo;
    private String mail;
    private LocalDate dateInscription;
}
