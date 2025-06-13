package miage.procratinator.procrastinator.entities.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NiveauRecompense {

    PAPIER_MACHE(50, 1),
    CARTON(200, 2),
    BRONZE(500, 3),
    ARGENT(1000, 4),
    OR(2000, 6),
    PLATINE(3000, 8),
    DIAMANT(5000, 12);

    private final int pointsAttribues;
    private final int nombreDeMois;
}
