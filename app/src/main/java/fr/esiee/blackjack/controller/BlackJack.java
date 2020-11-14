package fr.esiee.blackjack.controller;

import java.util.LinkedList;
import java.util.List;

import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;

/**
 * Classe contenant la logique d un jeu de Blackjack
 */
public class BlackJack {

    // region Constants

    /**
     * Nombre de jeu de 52 cartes dans le deck
     */
    public static final int INIT_DECK = 4;

    /**
     * Nombre maximum de jeu de cartes dans le deck
     */
    public static final int MAX_DECK = 7;

    /**
     * Argent initial au debut d une partie
     */
    public static final int INIT_BALANCE = 5000;

    // endregion

    /**
     * Deck qui contient l ensemble des cartes
     */
    private final Deck deck;

    /**
     * L argent dont dispose le joueur
     */
    private int balance;

    /**
     * Les cartes que le joueur possede pour le round
     */
    private final Hand playerHand;

    /**
     * Les cartes que la banque possede pour le round
     */
    private final Hand bankHand;

    /**
     * Variable qui verifie si le round est termine
     */
    private boolean gameFinished;

    /**
     * Constructeur par defaut qui utilise les constantes du jeu
     */
    public BlackJack() {
        this(INIT_DECK, INIT_BALANCE);
    }

    /**
     * Constructeur qui va creer un objet Blackjack contenant nbBox de jeu de carte et somme argent
     * au depart de la partie
     * @param nbBox Le nombre de jeu de carte a ajouter
     * @param somme La somme de depart
     */
    public BlackJack(int nbBox, int somme) {
        if (nbBox > MAX_DECK) {
            throw new IllegalArgumentException("Le nombre maximum de deck est de 7");
        }
        this.deck = new Deck(nbBox);
        this.playerHand = new Hand();
        this.bankHand = new Hand();
        this.gameFinished = false;
        this.balance = somme;
    }

    /**
     * Reinitialise le jeu pour un nouveau round
     */
    public void reset() {
        playerHand.clear();
        bankHand.clear();
        gameFinished = false;
    }

    /**
     * Initialise un nouveau round
     * Le joueur tire deux carte et la banque en tire une
     * @throws EmptyDeckException Si le deck est vide
     */
    public void initRound() throws EmptyDeckException {
        for (int i = 0; i < 2; i++) {
            playerDrawAnotherCard();
        }
        bankHand.add(deck.draw());
    }

    /**
     * Retourne la main du joueur en chaine de caractere
     * @return La main du joueur en chaine de caractere
     */
    public String getPlayerHandString() {
        return playerHand.toString();
    }

    /**
     * Retourne la main de la banque en chaine de caractere
     * @return La main de la banque en chaine de caractere
     */
    public String getBankHandString() {
        return bankHand.toString();
    }

    /**
     * @return Le meilleur score obtenu par le joueur
     */
    public int getPlayerBest() {
        return playerHand.best();
    }

    /**
     * @return Le meilleur score obtenu par la banque
     */
    public int getBankBest() {
        return bankHand.best();
    }

    /**
     * @return L argent disponible
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Setter pour l attribut balance
     * @param balance La nouvelle balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Met a jour l argent disponible selon la mise du joueur
     * Multiplie par 2 si gagne et par 3 si Blackjack (21)
     * Si egalite alors on recupere la mise
     * @param bet La mise du joueur
     */
    public void updateBalance(int bet) {
        if (isPlayerWinner()) {
            if (getPlayerBest() == 21) {
                bet *= 3;
            } else {
                bet *= 2;
            }
            balance += bet;
        } else if (!isBankWinner() && !isPlayerWinner()) {
            balance += bet;
        }
    }

    /**
     * @return La main du joueur
     */
    public List<Card> getPlayerCardList() {
        return new LinkedList<>(playerHand.getCardList());
    }

    /**
     * @return La main de la banque
     */
    public List<Card> getBankCardList() {
        return new LinkedList<>(bankHand.getCardList());
    }

    /**
     * Verifie si le joueur a gagne
     * @return true si le joueur gagne sinon false
     */
    public boolean isPlayerWinner() {
        return isGameFinished() &&
                getPlayerBest() <= 21 &&
                getPlayerBest() > getBankBest() ||
                getBankBest() > 21 &&
                getPlayerBest() <= 21;
    }

    /**
     * Verifie si la banque a gagne
     * @return true si la banque gagne sinon false
     */
    public boolean isBankWinner() {
        return isGameFinished() &&
                getBankBest() <= 21 &&
                getBankBest() > getPlayerBest() ||
                getPlayerBest() > 21 &&
                getBankBest() <= 21;
    }

    /**
     * @return true si la partie est fini sinon false
     */
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Ajoute une carte a la main du joueur
     * @throws EmptyDeckException Si le deck est vide
     */
    public void playerDrawAnotherCard() throws EmptyDeckException {
        if (!playerHand.isOver21()) {
            playerHand.add(deck.draw());
        }
    }

    /**
     * La banque tire des cartes
     * Elle tire des cartes jusqu a obtenir un score superieur ou egal a 17
     * Exception si le joueur a fait un score inferieur a 17
     * @throws EmptyDeckException Si le deck est vide
     */
    public void bankLastTurn() throws EmptyDeckException {
        while (!isGameFinished()) {
            bankHand.add(deck.draw());
            if (bankHand.isOver21() || getBankBest() > getPlayerBest() || getPlayerBest() > 21 || getBankBest() >= 17) {
                gameFinished = true;
            }
        }
    }
}
