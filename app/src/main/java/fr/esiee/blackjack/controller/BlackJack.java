package fr.esiee.blackjack.controller;

import java.util.LinkedList;
import java.util.List;

import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;

public class BlackJack {

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

    private final Deck deck;

    private int balance;

    private final Hand playerHand;
    private final Hand bankHand;

    private boolean gameFinished;

    public BlackJack() {
        this(INIT_DECK, INIT_BALANCE);
    }

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

    public void reset() {
        playerHand.clear();
        bankHand.clear();
        gameFinished = false;

    }

    public void initRound() throws EmptyDeckException {
        for (int i = 0; i < 2; i++) {
            playerDrawAnotherCard();
        }
        bankHand.add(deck.draw());
    }

    public String getPlayerHandString() {
        return playerHand.toString();
    }

    public String getBankHandString() {
        return bankHand.toString();
    }

    public int getPlayerBest() {
        return playerHand.best();
    }

    public int getBankBest() {
        return bankHand.best();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

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

    public List<Card> getPlayerCardList() {
        return new LinkedList<>(playerHand.getCardList());
    }

    public List<Card> getBankCardList() {
        return new LinkedList<>(bankHand.getCardList());
    }

    public boolean isPlayerWinner() {
        return isGameFinished() &&
                getPlayerBest() <= 21 &&
                getPlayerBest() > getBankBest() ||
                getBankBest() > 21 &&
                getPlayerBest() <= 21;
    }

    public boolean isBankWinner() {
        return isGameFinished() &&
                getBankBest() <= 21 &&
                getBankBest() > getPlayerBest() ||
                getPlayerBest() > 21 &&
                getBankBest() <= 21;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void playerDrawAnotherCard() throws EmptyDeckException {
        if (!playerHand.isOver21()) {
            playerHand.add(deck.draw());
        }
    }

    public void bankLastTurn() throws EmptyDeckException {
        while (!isGameFinished()) {
            bankHand.add(deck.draw());
            if (bankHand.isOver21() || getBankBest() > getPlayerBest() || getPlayerBest() > 21) {
                gameFinished = true;
            }
        }
    }
}
