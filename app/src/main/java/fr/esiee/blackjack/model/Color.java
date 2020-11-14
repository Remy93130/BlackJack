package fr.esiee.blackjack.model;

/**
 * Enumere les 4 symboles dans un jeu de carte avec le symbole et le nom
 */
public enum Color {
    HEART("♥"),
    SPADE("♠"),
    CLUB("♣"),
    DIAMOND("♦");

    private final String symbole;

    Color(String symbole) {
        this.symbole = symbole;
    }

    public String getSymbole() {
        return symbole;
    }
}
