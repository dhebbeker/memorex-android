package info.hebbeker.david.memorex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    private final SymbolButton[] symbols = new SymbolButton[4];
    private int nextSymbolIndex = 0;
    private ArrayList<Integer> symbolIndexList = new ArrayList<Integer>();

    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        symbols[0] = findViewById(R.id.button1);
        symbols[1] = findViewById(R.id.button2);
        symbols[2] = findViewById(R.id.button3);
        symbols[3] = findViewById(R.id.button4);

        for (SymbolButton symbolButton : symbols) symbolButton.setVisibility(View.INVISIBLE);
    }

    void verifySequence(final int symbol, final View view)
    {
        Log.d(TAG, "Input=" + symbol + " Expected=" + symbolIndexList.get(nextSymbolIndex));
        boolean freeOfErrors = true;
        if (symbolIndexList.get(nextSymbolIndex) == symbol)
        {
            freeOfErrors = true;
        } else
        {
            freeOfErrors = false;
            disableSymbolInput();
        }
        nextSymbolIndex++;
        if ((nextSymbolIndex == symbolIndexList.size()) || !freeOfErrors)
        {
            CharSequence resultMessage = "";
            if (freeOfErrors)
            {
                resultMessage = "next level";
            } else
            {
                resultMessage = "Game Over!";
            }
            Toast notification = Toast.makeText(this, resultMessage + " (" + nextSymbolIndex + ")", Toast.LENGTH_LONG);
            notification.show();
            if (freeOfErrors) nextLevel(view);
            else
            {
                View button = findViewById(R.id.startButton);
                button.setVisibility(View.VISIBLE);
                button.setClickable(true);
            }
        }
    }

    /**
     * Called when the user presses a symbol button
     */
    public void signalSymbol2Game(View view)
    {
        SymbolButton pressedButton = (SymbolButton) view;
        pressedButton.signalSymbol();

        /* send symbol to game logic */
        /* @todo send symbol to game logic */

        verifySequence(pressedButton.getSymbol(), view);
    }

    public void signalSequence(final View view)
    {
        disableSymbolInput();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (final int symbolIndex : symbolIndexList)
                {
                    view.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            symbols[symbolIndex].signalSymbol();
                        }
                    });
                    try
                    {
                        sleep((long) (symbols[symbolIndex].getSignallingDuration() * 1.2));
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                enableSymbolInput();
            }
        }).start();
    }

    public void startGame(final View view)
    {
        view.setClickable(false);
        view.setVisibility(View.GONE);

        for (SymbolButton symbolButton : symbols) symbolButton.setVisibility(View.VISIBLE);
        newGame(view);
    }

    void newGame(final View view)
    {

        symbolIndexList.clear();
        nextLevel(view);
    }

    public final void nextLevel(final View view)
    {
        nextSymbolIndex = 0;
        symbolIndexList.add(rand.nextInt(4));
        signalSequence(view);
    }

    public void stopGame()
    {

    }

    public void disableSymbolInput()
    {
        for (SymbolButton symbolButton : symbols) symbolButton.setClickable(false);
    }

    public void enableSymbolInput()
    {
        for (SymbolButton symbolButton : symbols) symbolButton.setClickable(true);
    }
}
