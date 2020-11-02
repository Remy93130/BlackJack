package fr.esiee.blackjack.controller;

import fr.esiee.blackjack.exception.EmptyDeckException;

public class BlackJack {
    private final Deck deck;

    private final Hand playerHand;

    private final Hand bankHand;

    private boolean gameFinished;

    public BlackJack() {
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.bankHand = new Hand();
        this.gameFinished = false;
    }

    public void reset() {
        playerHand.clear();
        bankHand.clear();
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

    public boolean isPlayerWinner() {
        return isGameFinished() &&
                getPlayerBest() <= 21 &&
                getPlayerBest() > getBankBest();
    }

    public boolean isBankWinner() {
        return isGameFinished() &&
                getBankBest() <= 21 &&
                getBankBest() > getPlayerBest();
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void playerDrawAnotherCard() throws EmptyDeckException {
        if (!isGameFinished()) {
            playerHand.add(deck.draw());
            if (playerHand.isOver21()) {
                gameFinished = true;
            }
        }
    }

    public void bankLastTurn() throws EmptyDeckException {
        while (!isGameFinished()) {
            bankHand.add(deck.draw());
            if (bankHand.isOver21() || getBankBest() > getPlayerBest()) {
                gameFinished = true;
            }
        }
    }
}
