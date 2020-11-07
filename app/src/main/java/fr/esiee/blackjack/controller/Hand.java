package fr.esiee.blackjack.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.esiee.blackjack.model.Card;
import fr.esiee.blackjack.model.Value;

public class Hand {
    private final LinkedList<Card> cardList;

    public Hand() {
        this.cardList = new LinkedList<>();
    }

    public void add(Card card) {
        cardList.add(card);
    }

    public void clear() {
        cardList.clear();
    }

    public List<Integer> count() {
        List<Integer> list = new LinkedList<>();
        list.add(0);
        boolean isAs = false;
        for (Card card : cardList) {
            if (card.getValue().equals(Value.AS)) {
                isAs = true;
            }
            list.set(0, list.get(0) + card.getPoints());
        }
        // Si on a eu un as on ajoute score + 10 a une nouvelle case
        if (isAs) {
            list.add(list.get(0) + 10);
        }
        return list;
    }

    public int best() {
        int bestValue = -1;
        List<Integer> cards = count();
        for (int value : cards) {
            if (value > bestValue && value < 22) {
                bestValue = value;
            }
        }
        // Si depassement de 21 par les 2 score on prend le plus proche
        if (bestValue == -1) {
            bestValue = Collections.min(cards);
        }
        return bestValue;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public boolean isOver21() {
        return Collections.min(count()) > 21;
    }

    @Override
    public String toString() {
        return cardList.toString() + " " + count();
    }
}
