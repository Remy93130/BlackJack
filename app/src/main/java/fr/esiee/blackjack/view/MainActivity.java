package fr.esiee.blackjack.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import fr.esiee.blackjack.R;
import fr.esiee.blackjack.controller.BlackJack;
import fr.esiee.blackjack.exception.EmptyDeckException;
import fr.esiee.blackjack.model.Card;

public class MainActivity extends AppCompatActivity {

    private BlackJack gameInstance;

    private int currentBet;

    // region widgets

    private LinearLayout layoutCardPlayer;
    private LinearLayout layoutResultPlayer;
    private LinearLayout layoutCardBank;
    private LinearLayout layoutResultBank;

    private SeekBar barBet;

    private Button buttonAddCard;
    private Button buttonStopRound;
    private Button buttonResetGame;
    private Button buttonBet;

    private TextView textPlayerName;
    private TextView textBalance;
    private TextView textBestPlayer;
    private TextView textBestBank;
    private TextView textBarBet;

    // endregion

    // region Attributes from SharedPreferences

    private String locale;
    private int themeId;
    private int deckNumber;
    private int startedBalance;
    private int orientation;
    private String playerName;

    // endregion

    // region Lifecycle methods

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences storage = newBase.getSharedPreferences("settings", 0);
        locale = storage.getString("locale", "fr");
        super.attachBaseContext(updateResources(newBase, locale));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreSettings();
        setTheme(themeId);
        super.onCreate(savedInstanceState);
        // new BlackJackConsole();
        setRequestedOrientation(orientation);
        gameInstance = new BlackJack(deckNumber, startedBalance);
        Log.i("GAME", "Create the game with " + deckNumber + " deck(s)");
        setContentView(R.layout.activity_main);
        hydrateWidgets();
        addListenerToButtonsAndBar();
        textBalance.setText(getString(R.string.txt_balance).replace("{}", String.valueOf(gameInstance.getBalance())));
        textBestBank.setText(getString(R.string.txt_bank_best).replace("{}", String.valueOf(gameInstance.getBankBest())));
        textBestPlayer.setText(getString(R.string.txt_bank_best).replace("{}", String.valueOf(gameInstance.getBankBest())));
        textPlayerName.setText(playerName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_settings) {
            createDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences storage = getSharedPreferences("settings", 0);
        SharedPreferences.Editor edit = storage.edit();
        edit.putString("locale", locale);
        edit.putInt("themeId", themeId);
        edit.putInt("deckNumber", deckNumber);
        edit.putInt("startedBalance", startedBalance);
        edit.putInt("orientation", orientation);
        edit.putString("playerName", playerName);
        edit.apply();
        super.onDestroy();
    }

    private static Context updateResources(Context context, String language) {
        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();
        Locale sysLocale = config.getLocales().get(0);
        String lang = sysLocale.getLanguage();
        if (!language.equals("") && !lang.equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        }
        return new ContextWrapper(context);
    }

    // endregion

    // region Game logic

    private void hydrateWidgets() {
        layoutCardPlayer = findViewById(R.id.layout_card_player);
        layoutResultPlayer = findViewById(R.id.layout_result_player);
        layoutCardBank = findViewById(R.id.layout_card_bank);
        layoutResultBank = findViewById(R.id.layout_result_bank);
        buttonAddCard = findViewById(R.id.btn_card);
        buttonStopRound = findViewById(R.id.btn_stop);
        buttonResetGame = findViewById(R.id.btn_reset);
        buttonBet = findViewById(R.id.btn_bet);
        barBet = findViewById(R.id.bar_bet);
        textPlayerName = findViewById(R.id.txt_player);
        textBalance = findViewById(R.id.txt_balance);
        textBestPlayer = findViewById(R.id.txt_best_player);
        textBestBank = findViewById(R.id.txt_best_bank);
        textBarBet = findViewById(R.id.txt_bet);
    }

    private void addListenerToButtonsAndBar() {
        buttonAddCard.setOnClickListener(v -> {
            Toast.makeText(this, R.string.btn_card, Toast.LENGTH_SHORT).show();
            try {
                gameInstance.playerDrawAnotherCard();
            } catch (EmptyDeckException e) {
                displayErrorMessageAsToast(e.getMessage());
            }
            showCards();
            // End player game we trigger bank turn automatically
            if (gameInstance.getPlayerBest() >= 21) {
                buttonStopRound.performClick();
            }
        });
        buttonStopRound.setOnClickListener(v -> {
            Toast.makeText(this, R.string.btn_stop, Toast.LENGTH_SHORT).show();
            try {
                gameInstance.bankLastTurn();
            } catch (EmptyDeckException e) {
                displayErrorMessageAsToast(e.getMessage());
            }
            setButtonsToPlayEnabled(false);
            showCards();
            showEndGamesCards();
            gameInstance.updateBalance(currentBet);
            textBalance.setText(getString(R.string.txt_balance).replace("{}", String.valueOf(gameInstance.getBalance())));

        });
        buttonResetGame.setOnClickListener(v -> {
            Toast.makeText(this, R.string.btn_reset, Toast.LENGTH_SHORT).show();
            gameInstance.reset();
            setButtonsToPlayEnabled(false);
            clearCardsLayout();
            showCards();
        });

        buttonBet.setOnClickListener(v -> {
            Toast.makeText(this, R.string.txt_btn_bet, Toast.LENGTH_SHORT).show();
            gameInstance.reset();
            try {
                gameInstance.initRound();
            } catch (EmptyDeckException e) {
                displayErrorMessageAsToast(e.getMessage());
                return;
            }
            currentBet = barBet.getProgress();
            if (currentBet > gameInstance.getBalance()) {
                Toast.makeText(this, R.string.txt_money_out, Toast.LENGTH_LONG).show();
                return;
            }
            clearCardsLayout();
            showCards();
            setButtonsToPlayEnabled(true);
            playerBet();
        });

        barBet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textBarBet.setText(getString(R.string.txt_bet).replace("150", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void playerBet() {
        gameInstance.setBalance(gameInstance.getBalance() - currentBet);
        textBalance.setText(getString(R.string.txt_balance).replace("{}", String.valueOf(gameInstance.getBalance())));
    }

    // endregion

    // region Settings management

    private void createDialog() {
        AlertDialog.Builder diag = new AlertDialog.Builder(this);
        diag.setTitle(R.string.txt_dialog_title);
        View viewDialog = getLayoutInflater().inflate(R.layout.diag_config, null);
        // Input from dialog
        EditText inputPlayerName = viewDialog.findViewById(R.id.diag_player);
        EditText inputBalance = viewDialog.findViewById(R.id.diag_init_balance);
        EditText inputDeck = viewDialog.findViewById(R.id.diag_deck_count);
        RadioButton radioBackgroundBlack = viewDialog.findViewById(R.id.diag_back_black);
        RadioButton radioBackgroundGreen = viewDialog.findViewById(R.id.diag_back_green);
        RadioButton radioLocaleEn = viewDialog.findViewById(R.id.diag_locale_en);
        RadioButton radioLocaleFr = viewDialog.findViewById(R.id.diag_locale_fr);
        RadioButton radioLandscape = viewDialog.findViewById(R.id.diag_orientation_landscape);
        RadioButton radioPortrait = viewDialog.findViewById(R.id.diag_orientation_portrait);
        // Fill inputs
        inputPlayerName.setText(playerName);
        inputBalance.setText(String.valueOf(startedBalance));
        inputDeck.setText(String.valueOf(deckNumber));
        // Set radio button checked by default
        if (themeId == R.style.Theme_Blackjack_Green) {
            radioBackgroundGreen.setChecked(true);
        } else {
            radioBackgroundBlack.setChecked(true);
        }
        if (locale.equals("fr")) {
            radioLocaleFr.setChecked(true);
        } else {
            radioLocaleEn.setChecked(true);
        }
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            radioPortrait.setChecked(true);
        } else {
            radioLandscape.setChecked(true);
        }
        diag.setView(viewDialog);
        // Listeners
        diag.setPositiveButton(R.string.txt_confirm, (dialog, sumthin) -> {
            changeGameSettings(inputDeck, radioBackgroundBlack, radioLocaleFr, inputPlayerName, inputBalance, radioLandscape);
            Toast.makeText(this, R.string.txt_confirm, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        diag.setNegativeButton(R.string.txt_cancel, (dialog, sumthin) -> {
            dialog.cancel();
            Toast.makeText(this, R.string.txt_cancel, Toast.LENGTH_SHORT).show();
        });
        diag.show();
    }

    private void changeGameSettings(EditText inputDeck, RadioButton radioBackgroundBlack, RadioButton radioButtonLocaleFr, EditText inputPlayer, EditText inputBalance, RadioButton isLandscape) {
        int nbDeck, balance;
        // Cast values and fallback to default value
        try {
            nbDeck = Integer.parseInt(inputDeck.getText().toString());
            if (nbDeck > BlackJack.MAX_DECK) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ignored) {
            nbDeck = BlackJack.INIT_DECK; // Default value
        }
        try {
            balance = Integer.parseInt(inputBalance.getText().toString());
        } catch (NumberFormatException ignored) {
            balance = BlackJack.INIT_BALANCE;
        }
        deckNumber = nbDeck;
        startedBalance = balance;
        playerName = (inputPlayer.getText().toString().isEmpty()) ? getResources().getString(R.string.player) : inputPlayer.getText().toString();
        Log.i("PL", "->" + playerName + " " + inputPlayer.getText().toString().isEmpty());
        themeId = (radioBackgroundBlack.isChecked()) ? R.style.Theme_Blackjack_Black : R.style.Theme_Blackjack_Green;
        locale = (radioButtonLocaleFr.isChecked()) ? "fr" : "en";
        orientation = (isLandscape.isChecked()) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        recreate();
    }

    private void restoreSettings() {
        SharedPreferences preferences = getSharedPreferences("settings", 0);
        deckNumber = preferences.getInt("deckNumber", BlackJack.INIT_DECK);
        themeId = preferences.getInt("themeId", R.style.Theme_Blackjack_Green);
        startedBalance = preferences.getInt("startedBalance", BlackJack.INIT_BALANCE);
        orientation = preferences.getInt("orientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        playerName = preferences.getString("playerName", getResources().getString(R.string.txt_player));
    }

    // endregion

    // region View management

    private void setButtonsToPlayEnabled(boolean state) {
        buttonAddCard.setEnabled(state);
        buttonStopRound.setEnabled(state);
        buttonBet.setEnabled(!state);
    }

    private void addToPanel(LinearLayout layout, String token) {
        ImageView imageView = new ImageView(this);
        int imageSlug = getResources().getIdentifier(
                "card_" + token.toLowerCase(),
                "drawable",
                getPackageName()
        );
        imageView.setPadding(10, 10, 10, 10);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSlug));
        layout.addView(imageView);
    }

    private void addCardsOnLayoutAndUpdateTextView(LinearLayout layout, String player, List<Card> cards) {
        layout.removeAllViewsInLayout();
        cards.forEach(card -> {
            String token = card.getColorName() + "_" + card.getValueSymbole();
            addToPanel(layout, token);
        });
        if (player.equals("bank")) {
            textBestBank.setText(getString(R.string.txt_bank_best).replace("{}", String.valueOf(gameInstance.getBankBest())));
        } else {
            textBestPlayer.setText(getString(R.string.txt_player_best).replace("{}", String.valueOf(gameInstance.getPlayerBest())));
        }
    }

    private void showCards() {
        addCardsOnLayoutAndUpdateTextView(layoutCardPlayer, "player", gameInstance.getPlayerCardList());
        addCardsOnLayoutAndUpdateTextView(layoutCardBank, "bank", gameInstance.getBankCardList());
    }

    private void showEndGamesCards() {
        if (gameInstance.isPlayerWinner()) {
            if (gameInstance.getPlayerBest() == 21) {
                addToPanel(layoutResultPlayer, Card.BLACKJACK_TOKEN);
            }
            addToPanel(layoutResultPlayer, Card.WIN_TOKEN);
            addToPanel(layoutResultBank, Card.LOOSE_TOKEN);
        } else if (gameInstance.isBankWinner()) {
            if (gameInstance.getBankBest() == 21) {
                addToPanel(layoutResultBank, Card.BLACKJACK_TOKEN);
            }
            addToPanel(layoutResultBank, Card.WIN_TOKEN);
            addToPanel(layoutResultPlayer, Card.LOOSE_TOKEN);
        } else {
            addToPanel(layoutResultBank, Card.DRAW_TOKEN);
            addToPanel(layoutResultPlayer, Card.DRAW_TOKEN);
        }
    }

    private void clearCardsLayout() {
        layoutCardBank.removeAllViewsInLayout();
        layoutCardPlayer.removeAllViewsInLayout();
        layoutResultBank.removeAllViewsInLayout();
        layoutResultPlayer.removeAllViewsInLayout();
    }

    private void displayErrorMessageAsToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // endregion
}
