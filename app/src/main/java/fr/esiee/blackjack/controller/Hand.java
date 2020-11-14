package fr.esiee.blackjack.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.esiee.blackjack.model.Card;
import fr.esiee.blackjack.model.Value;

/**
 * Represente les cartes qu un joueur dispose pour ce round
 */
public class Hand {

    /**
     * La liste des cartes que le joueur possede
     */
    private final LinkedList<Card> cardList;

    /**
     * Constructeur de la classe
     */
    public Hand() {
        this.cardList = new LinkedList<>();
    }

    /**
     * On ajoute une carte a la main
     * @param card La carte a ajouter
     */
    public void add(Card card) {
        cardList.add(card);
    }

    /**
     * Retire les cartes de la main
     */
    public void clear() {
        cardList.clear();
    }

    /**
     * Calcul les differents scores fait par le joueur
     * Il peut y avoir deux score possible comme un as peut avoir deux valeur
     * @return Liste des scores realises
     */
    public List<Integer> count() {
        List<Integer> list = new LinkedList<>();
        list.add(0);
        boolean hasAs = false;
        for (Card card : cardList) {
            if (card.getValue().equals(Value.AS)) {
                hasAs = true;
            }
            list.set(0, list.get(0) + card.getPoints());
        }
        // Si on a eu un as on ajoute score + 10 a une nouvelle case
        if (hasAs) {
            list.add(list.get(0) + 10);
        }
        return list;
    }

    /**
     * Calcul a partir des scores realiser par le joueur le meilleur score
     * Le meilleur est le score le plus proche de 21
     * @return Le meilleur score
     */
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

    /**
     * @return Les cartes que le joueur possede
     */
    public List<Card> getCardList() {
        return cardList;
    }

    /**
     * Verifie si le joueur a depasse 21
     * @return true si on depasse 21 sinon false
     */
    public boolean isOver21() {
        return Collections.min(count()) > 21;
    }

    @Override
    public String toString() {
        return cardList.toString() + " " + count();
    }
}
