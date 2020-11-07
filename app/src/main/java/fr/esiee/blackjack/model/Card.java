package fr.esiee.blackjack.model;

public class Card {

    // Suffixe du nom des cartes dans les ressources
    public static final String WIN_TOKEN = "winner";

    public static final String LOOSE_TOKEN = "looser";

    public static final String DRAW_TOKEN = "draw";

    public static final String BLACKJACK_TOKEN = "blackjack";

    private final Value value;

    private final Color symbole;

    public Card(Value value, Color symbole) {
        this.value = value;
        this.symbole = symbole;
    }

    public Color getSymbole() {
        return symbole;
    }

    public Value getValue() {
        return value;
    }

    public String getColorSymbole() {
        return symbole.getSymbole();
    }

    public String getColorName() {
        return symbole.name();
    }

    public String getValueSymbole() {
        return value.getSymbole();
    }

    public int getPoints() {
        return value.getPoints();
    }

    @Override
    public String toString() {
        return value.getPoints() + symbole.getSymbole();
    }
}
