package fr.esiee.blackjack.controller;

import java.util.Collections;
import java.util.LinkedList;

import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;
import fr.esiee.blackjack.model.Color;
import fr.esiee.blackjack.model.Value;

/**
 * Classe qui represente les cartes disponible dans une partie de blackjack
 */
public class Deck {

    /**
     * Represente les cartes presentes dans le deck
     */
    private final LinkedList<Card> cardList;

    /**
     * Constructeur par defaut qui creer un deck de 3 jeu de 52 cartes
     */
    public Deck() {
        this(3);
    }

    /**
     * Constructeur pour creer le deck
     * @param nbBox nombre de jeu de carte a inclure
     */
    public Deck(int nbBox) {
        this.cardList = new LinkedList<>();
        for (int i = 0; i < nbBox; i++) {
            for (Color color : Color.values()) {
                for (Value value : Value.values()) {
                    cardList.add(new Card(value, color));
                }
            }
        }
        Collections.shuffle(cardList);
    }

    /**
     * Tire une carte du deck et retire celle ci
     * @return La carte retire
     * @throws EmptyDeckException Si le deck est vide
     */
    public Card draw() throws EmptyDeckException {
        if (cardList.isEmpty()) {
            throw new EmptyDeckException();
        }
        return cardList.pop();
    }

    @Override
    public String toString() {
        return cardList.toString();
    }
}
