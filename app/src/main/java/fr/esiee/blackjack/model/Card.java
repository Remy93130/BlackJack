package fr.esiee.blackjack.model;

public class Card {

    // region Constants
    public static final String WIN_TOKEN = "winner";

    public static final String LOOSE_TOKEN = "looser";

    public static final String DRAW_TOKEN = "draw";

    public static final String BLACKJACK_TOKEN = "blackjack";

    // endregion

    /**
     * Le nombre de point que vaut une carte
     */
    private final Value value;

    /**
     * Le symbole sur la carte (roi, reine, valet)
     */
    private final Color symbole;

    /**
     * Constructeur
     * @param value La valeur de la carte
     * @param symbole Son symbole
     */
    public Card(Value value, Color symbole) {
        this.value = value;
        this.symbole = symbole;
    }

    /**
     * @return Le nom du symbole de la carte
     */
    public Color getSymbole() {
        return symbole;
    }

    /**
     * @return La valeur de la carte
     */
    public Value getValue() {
        return value;
    }

    /**
     * @return Le symbole de la carte
     */
    public String getColorSymbole() {
        return symbole.getSymbole();
    }

    /**
     *
     * @return Le nom du symbole
     */
    public String getColorName() {
        return symbole.name();
    }

    /**
     * @return La symbole de la valeur
     */
    public String getValueSymbole() {
        return value.getSymbole();
    }

    /**
     * @return Le nombre de point que vaut la carte
     */
    public int getPoints() {
        return value.getPoints();
    }

    @Override
    public String toString() {
        return value.getPoints() + symbole.getSymbole();
    }
}
