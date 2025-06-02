package miage.procratinator.procrastinator.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Procrastinateur extends Utilisateur {

    /*
    @OneToMany(mappedBy = "procrastinateur", cascade = CascadeType.ALL)
    private List<TacheAEviter> taches = new ArrayList<>();

    @OneToMany(mappedBy = "procrastinateur", cascade = CascadeType.ALL)
    private List<Confrontation> confrontations = new ArrayList<>();

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    private List<Excuse> excusesSoumises = new ArrayList<>();

    @OneToMany(mappedBy = "procrastinateur", cascade = CascadeType.ALL)
    private List<ParticipationDefi> participations = new ArrayList<>();
     */
}

