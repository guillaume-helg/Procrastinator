package miage.procratinator.procrastinator.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miage.procratinator.procrastinator.entities.enumeration.Categorie;
import miage.procratinator.procrastinator.entities.enumeration.StatutExcuse;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Excuse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExcuse;
    private String texte;
    private String situation;
    private int votesRecus;
    private Long idProcrastinateur;
    private Categorie categorie;
    private LocalDate dateSoumission;
    private StatutExcuse statut;
}

