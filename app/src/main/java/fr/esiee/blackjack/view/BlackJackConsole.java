package fr.esiee.blackjack.view;

import android.util.Log;

import fr.esiee.blackjack.controller.Deck;
import fr.esiee.blackjack.controller.Hand;
import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;
import fr.esiee.blackjack.model.Color;
import fr.esiee.blackjack.model.Value;

/**
 * Classe pour essayer la classe Blackjack en console avant de passer au layout
 */
public class BlackJackConsole {

    public BlackJackConsole() {
        testDisplayCards();
        testDisplayAs();
    }

    private void testDisplayCards() {
        Card[] tab = {
                new Card(Value.TWO, Color.HEART),
                new Card(Value.JACK, Color.SPADE)
        };
        for (Card c : tab) {
            Log.e("CHECK", "This card is a " + c + " worth " + c.getValueSymbole() + " points.");
            Log.e("CHECK", "It's a name " + c.getColorName());
            switch (c.getColorSymbole()) {
                case "\u2665":
                    Log.e("CHECK", "symbole : heart");
                    break;
                case "\u2660":
                    Log.e("CHECK", "symbole : spade");
                    break;
                case "\u2663":
                    Log.e("CHECK", "symbole : club");
                    break;
                case "\u2666":
                    Log.e("CHECK", "symbole : diamond");
                    break;
            }
            if (c.getValueSymbole().matches("[JQK]")) {
                Log.e("CHECK", "It's a face !");
            } else {
                Log.e("CHECK", "It's not a face");
            }
        }
    }

    private void testDisplayAs() {
        Deck d = new Deck(1);
        Hand h = new Hand();
        Log.i("CHECK", h.toString());

        try {
            for (int i = 0; i < 10; i++) {
                h.add(d.draw());
                Log.i("CHECK", h.toString());
            }
        } catch (EmptyDeckException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
