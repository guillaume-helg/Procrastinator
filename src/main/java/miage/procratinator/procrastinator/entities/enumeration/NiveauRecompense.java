package miage.procratinator.procrastinator.entities.enumeration;

public enum NiveauRecompense {

    PAPIER_MACHE(50),
    CARTON(200),
    BRONZE(500),
    ARGENT(1000),
    OR(2000),
    PLATINE(3000),
    DIAMANT(5000);

    private final int pointsAttribues;

    NiveauRecompense(int pointsAttribues) {
        this.pointsAttribues = pointsAttribues;
    }

    public int getPointsAttribues() {
        return pointsAttribues;
    }

}
