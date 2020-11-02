package fr.esiee.blackjack.model;

public enum Color {
    HEART("♥"),
    SPADE("♠"),
    CLUB("♣"),
    DIAMOND("♦");

    private final String symbole;

    private Color(String symbole) {
        this.symbole = symbole;
    }

    public String getSymbole() {
        return symbole;
    }
}
