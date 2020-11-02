package fr.esiee.blackjack.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import fr.esiee.blackjack.R;
import fr.esiee.blackjack.controller.Deck;
import fr.esiee.blackjack.controller.Hand;
import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BlackJackConsole();
    }
}