package fr.esiee.blackjack.controller;

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
            // Si on a 2 scores on incremente le second
            if (isAs) {
                list.set(1, list.get(1) + card.getPoints());
            }
            // Si on tombe sur un as pour la premiere fois (2 as = 22 donc on prend pas en compte)
            if (!isAs && card.getValue().equals(Value.AS)) {
                isAs = true;
                list.add(list.get(0) + 11);
            }
            list.set(0, list.get(0) + card.getPoints());
        }
        return list;
    }

    public int best() {
        int bestValue = 0;
        for (int value : count()) {
            if (value > bestValue && value < 22) {
                bestValue = value;
            }
        }
        return bestValue;
    }

    public boolean isOver21() {
        return Collections.min(count()) > 21;
    }

    @Override
    public String toString() {
        return cardList.toString() + " " + count();
    }
}
