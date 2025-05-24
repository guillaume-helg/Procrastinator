package miage.procratinator.procrastinator.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.Resultat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Confrontation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConfrontation;
    private Long idPiegeProductivite;
    private Long idProcrastinateur;
    private Date dateConfrontation;
    private Resultat resultat;
    private int pointEnJeu;
    private String commentaire;

}


