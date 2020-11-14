package fr.esiee.blackjack.exception;

/**
 * Exception leve lorsqu on essaye de prendre une carte dans un deck alors que celui ci est vide
 */
public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        this("Le deck est vide");
    }

    EmptyDeckException(String message) {
        super(message);
    }
}
