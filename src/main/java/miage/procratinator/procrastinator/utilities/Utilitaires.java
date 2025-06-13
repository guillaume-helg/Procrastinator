package miage.procratinator.procrastinator.utilities;

import java.time.LocalDate;
import java.time.Period;

public class Utilitaires {

    /**
     * Calcule la différence entre deux dates en termes de jours, mois et années.
     * Cette méthode prend en compte les années bissextiles et les différences de longueur des mois.
     *
     * @param dateDebut La date de début pour le calcul
     * @param dateFin   La date de fin pour le calcul
     * @return Period Un objet Period contenant la différence en années, mois et jours
     */
    public static Period calculerDifferenceEntreDate(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Dates pas nulles");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }
        return Period.between(dateDebut, dateFin);
    }
}
