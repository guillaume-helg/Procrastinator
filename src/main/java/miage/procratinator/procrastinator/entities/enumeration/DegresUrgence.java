package miage.procratinator.procrastinator.entities.enumeration;

public enum DegresUrgence {
    UN(1),
    DEUX(2),
    TROIS(3),
    QUATRE(4),
    CINQ(5);

    private final int valeur;

    DegresUrgence(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return valeur;
    }
}
