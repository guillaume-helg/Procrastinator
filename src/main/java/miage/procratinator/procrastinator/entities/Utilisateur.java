package miage.procratinator.procrastinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.Niveau;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Utilisateur {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;
    private String pseudo;
    private String mail;
    private Niveau niveau;
    @ManyToOne
    @JoinColumn(name = "idExcuse")
    private Excuse excusePreferee;
    private Date dateInscription;
    private int point;

}
