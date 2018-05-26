package info.hebbeker.david.memorex;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements GameBoardInterface, View.OnClickListener
{
    private static final String packageName = MainActivity.class.getPackage().getName();
    static final String HIGH_SCORE_DATA = packageName + ".HIGH_SCORE_DATA";
    private final SymbolButton[] symbols = new SymbolButton[4];
    private final Game game = new Game(this, symbols);
    private View startGameButton = null;
    private Toast notification = null;
    private HighScoreContainer highScoreContainer = null;
    private AppUpdater appUpdater;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        startGameButton = findViewById(R.id.startButton);
        highScoreContainer = new HighScoreContainer(getSharedPreferences(packageName + ".PREFERENCE_SCORE_FILE_KEY", Context.MODE_PRIVATE));

        symbols[0] = findViewById(R.id.button1);
        symbols[1] = findViewById(R.id.button2);
        symbols[2] = findViewById(R.id.button3);
        symbols[3] = findViewById(R.id.button4);
        for (SymbolButton symbolButton : symbols) symbolButton.setOnClickListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        // Search for updates
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String preferenceKeyAutoUpdate = getString(R.string.preference_switch_autoUpdate);
        appUpdater = new AppUpdater(this)
                .setButtonDoNotShowAgain("")
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo(getString(R.string.gitHubUsername), getString(R.string.gitHubRepositoryName));
        if (defaultSharedPreferences.getBoolean(preferenceKeyAutoUpdate, false))
        {
            appUpdater.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * Called when the user presses a symbol button
     */
    @SuppressWarnings("WeakerAccess")
    public void signalSymbol2Game(View view)
    {
        SymbolButton pressedButton = (SymbolButton) view;
        pressedButton.signalSymbol(); // signal symbol to user
        game.putPlayerInput(pressedButton); // signal symbol to game
    }

    public void startGame(@SuppressWarnings("unused") final View view)
    {
        game.startNewGame();
    }

    @Override
    public void queueDisplaySymbolSequence(final ArrayList<Symbol> symbolSequence)
    {
        for (SymbolButton symbolButton : symbols) symbolButton.setClickable(false);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(SymbolButton.getSignallingDuration() + 450);
                    for (final Symbol symbol : symbolSequence)
                    {
                        final SymbolButton symbolButton = (SymbolButton) symbol;
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                symbolButton.signalSymbol();
                            }
                        });
                        sleep((long) (SymbolButton.getSignallingDuration() * 1.2));
                    }
                } catch (InterruptedException e)
                {
                    // can not do much in case of an InterruptedException.
                    // It makes no sense to continue the sequence.
                    e.printStackTrace();
                }
                for (SymbolButton symbolButton : symbols) symbolButton.setClickable(true);
            }
        }).start();
    }

    @Override
    public void notifyUser(final String userMessage)
    {
        if (notification != null) // check if a notification has been created before
        {
            notification.cancel(); // cancel potential previous notification
        }
        notification = Toast.makeText(this, userMessage, Toast.LENGTH_LONG);
        notification.show();
    }

    @Override
    public void showEndOfGameDialog(final Score score)
    {
        final boolean isNewHighScore = highScoreContainer.setNewHighScore(score);
        DialogFragment endOfGameDialog = new EndOfGameDialogFragment();

        /* the arguments for the dialog need to be put into a bundle */
        final Bundle dialogArguments = new Bundle(3);
        dialogArguments.putSerializable(EndOfGameDialogFragment.keyLastScore, score);
        dialogArguments.putSerializable(EndOfGameDialogFragment.keyCurrentHighScore, highScoreContainer.getCurrentHighScore());
        dialogArguments.putBoolean(EndOfGameDialogFragment.keyIsNewHighScore, isNewHighScore);
        endOfGameDialog.setArguments(dialogArguments);

        endOfGameDialog.show(getFragmentManager(), EndOfGameDialogFragment.class.getCanonicalName());
    }

    @Override
    public void deployGameBoard()
    {
        startGameButton.setVisibility(View.GONE);
        for (SymbolButton symbolButton : symbols)
        {
            symbolButton.setVisibility(View.VISIBLE);
            symbolButton.setClickable(true);
        }
    }

    @Override
    public void clearBoard()
    {
        for (SymbolButton symbolButton : symbols) symbolButton.setClickable(false);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(SymbolButton.getSignallingDuration() + 750);
                } catch (InterruptedException e)
                {
                    // can not do much in case of an InterruptedException. Also it does not matter.
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (SymbolButton symbolButton : symbols)
                            symbolButton.setVisibility(View.INVISIBLE);
                        startGameButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    public void showHighScore(@SuppressWarnings("unused") final MenuItem menuItem)
    {
        Intent intent = new Intent(this, DisplayHighScore.class);
        intent.putExtra(HIGH_SCORE_DATA, highScoreContainer.getCurrentHighScore());
        startActivity(intent);
    }

    public void updateNow(@SuppressWarnings("unused") final MenuItem menuItem)
    {
        appUpdater.showAppUpdated(true);
        appUpdater.start();
    }

    public void showAbout(@SuppressWarnings("unused") final MenuItem menuItem)
    {
        Intent intent = new Intent(this, DisplayAboutActivity.class);
        startActivity(intent);
    }

    public void showSettings(@SuppressWarnings("unused") final MenuItem item)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showLicenses(@SuppressWarnings("unused") final MenuItem item)
    {
        Intent intent = new Intent(this, LicenseView.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view)
    {
        signalSymbol2Game(view);
    }
}
