package miage.procratinator.procrastinator.entities.enumeration;

public enum NiveauProcrastination {

    DEBUTANT(0),
    INTERMEDIAIRE(500),
    EXPERT(5000);

    private final int pointsRequis;

    NiveauProcrastination(int pointsRequis) {
        this.pointsRequis = pointsRequis;
    }

    public int getPointsRequis() {
        return pointsRequis;
    }

}
