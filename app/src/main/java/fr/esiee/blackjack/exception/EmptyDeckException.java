package fr.esiee.blackjack.exception;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        this("Le deck est vide");
    }

    EmptyDeckException(String message) {
        super(message);
    }
}
