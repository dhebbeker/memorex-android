package info.hebbeker.david.memorex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements GameBoardInterface
{
    private final SymbolButton[] symbols = new SymbolButton[4];
    private final Game game = new Game(this, symbols);
    private View startGameButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGameButton = findViewById(R.id.startButton);

        symbols[0] = findViewById(R.id.button1);
        symbols[1] = findViewById(R.id.button2);
        symbols[2] = findViewById(R.id.button3);
        symbols[3] = findViewById(R.id.button4);
    }


    /**
     * Called when the user presses a symbol button
     */
    public void signalSymbol2Game(final View view)
    {
        SymbolButton pressedButton = (SymbolButton) view;
        pressedButton.signalSymbol(); // signal symbol to user
        game.putPlayerInput(pressedButton); // signal symbol to game
    }

    public void startGame(final View view)
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
                    try
                    {
                        sleep((long) (symbolButton.getSignallingDuration() * 1.2));
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                for (SymbolButton symbolButton : symbols) symbolButton.setClickable(true);
            }
        }).start();
    }

    @Override
    public void notifyUser(final String userMessage)
    {
        Toast notification = Toast.makeText(this, userMessage, Toast.LENGTH_LONG);
        notification.show();
    }

    @Override
    public void deployGameBoard()
    {
        startGameButton.setVisibility(View.GONE);
        for (SymbolButton symbolButton : symbols) symbolButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearBoard()
    {
        for (SymbolButton symbolButton : symbols) symbolButton.setVisibility(View.INVISIBLE);
        startGameButton.setVisibility(View.VISIBLE);
    }
}
