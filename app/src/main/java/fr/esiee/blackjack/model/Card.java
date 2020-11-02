package fr.esiee.blackjack.model;

public class Card {
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
