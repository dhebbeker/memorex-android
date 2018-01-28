package info.hebbeker.david.memorex;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    private SoundPool soundPool;
    private int soundIdSymbol[] = new int[4];
    private List symbolButtons = new ArrayList();
    private volatile int gateKeeperStart = 0;
    private volatile int gateKeeperEnd = 0;
    private ArrayList shaker = new ArrayList();
    private int nextSymbolIndex = 0;
    private List symbolList = new ArrayList();

    /*private void signalNextSymbol2Human() {
Log.d(TAG,"nextSymbolIndex=" + nextSymbolIndex);
        if(nextSymbolIndex >=symbolList.size())
        {
            nextSymbolIndex =0;
            enableSymbolInput();
        }
        else
        {
            final int nextSymbolNr = (int) symbolList.get(nextSymbolIndex);
            Log.d(TAG,"nextSymbolNr=" + nextSymbolNr);
            ((SymbolButton) symbolButtons.get(nextSymbolNr)).startAnimation(shake);
            soundPool.play(soundIdSymbol[nextSymbolNr], 1f, 1f,1,0,1f);
            nextSymbolIndex++;
        }

    }*/
    private State state = State.STOPPED;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        soundIdSymbol[0] = soundPool.load(this, R.raw.keyok1, 1);
        soundIdSymbol[1] = soundPool.load(this, R.raw.keyok2, 1);
        soundIdSymbol[2] = soundPool.load(this, R.raw.keyok3, 1);
        soundIdSymbol[3] = soundPool.load(this, R.raw.keyok5, 1);
        shaker.add(AnimationUtils.loadAnimation(this, R.anim.shake));
        shaker.add(AnimationUtils.loadAnimation(this, R.anim.shake));
        shaker.add(AnimationUtils.loadAnimation(this, R.anim.shake));
        shaker.add(AnimationUtils.loadAnimation(this, R.anim.shake));
        /*shake.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
Log.d(TAG, "gKS0=" + gateKeeperStart++);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "gKE1=" + gateKeeperEnd++);
signalNextSymbol2Human();
                Log.d(TAG, "gKE2=" + gateKeeperEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/

        symbolButtons.add(findViewById(R.id.button1));
        symbolButtons.add(findViewById(R.id.button2));
        symbolButtons.add(findViewById(R.id.button3));
        symbolButtons.add(findViewById(R.id.button4));

        for (Object symbolButton : symbolButtons)
            ((SymbolButton) symbolButton).setVisibility(View.INVISIBLE);
    }

    /**
     * Called when the user presses a symbol button
     */
    public void signalSymbol2Game(View view)
    {
        SymbolButton pressedButton = (SymbolButton) view;
        Log.d(TAG, "Button pressed with symbol=" + pressedButton.symbol);
        /* animation of the button */

        /* play sound */
        if ((pressedButton.symbol < 0) || (pressedButton.symbol >= soundIdSymbol.length))
            throw new InvalidParameterException("illegal symbol " + pressedButton.symbol + " defined!");
        else
        {

            view.startAnimation((Animation) shaker.get(pressedButton.symbol));
            soundPool.play(soundIdSymbol[pressedButton.symbol], 1f, 1f, 1, 0, 1f);
        }



        /* send symbol to game logic */
        /* @todo send symbol to game logic */
    }

    public void signalSequence(final View view)
    {

        state = State.SIGNALLING;
        disableSymbolInput();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (final Object symbol : symbolList)
                {
                    view.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            soundPool.play(soundIdSymbol[(Integer) symbol], 1f, 1f, 1, 0, 1f);
                            ((SymbolButton) symbolButtons.get((Integer) symbol)).startAnimation((Animation) shaker.get((Integer) symbol));
                        }
                    });
                    try
                    {
                        sleep((long) (((Animation) shaker.get((Integer) symbol)).getDuration() * 0.9));
                    } catch (InterruptedException e)
                    {
                        Log.e(TAG, "Interrupted wait");
                        e.printStackTrace();
                    }
                }

                enableSymbolInput();
            }
        }).start();


    }

    public void startGame(View view)
    {
        if (state != State.STOPPED)
            throw new RuntimeException("Starting game is not allowed in state " + state.toString());
        else
        {
            view.setClickable(false);
            view.setVisibility(View.GONE);
            for (Object symbolButton : symbolButtons)
                ((SymbolButton) symbolButton).setVisibility(View.VISIBLE);
            symbolList.clear();
            nextSymbolIndex = 0;
            symbolList.add(0);
            symbolList.add(1);
            symbolList.add(2);
            symbolList.add(3);
            symbolList.add(0);
            symbolList.add(1);
            symbolList.add(2);
            symbolList.add(3);
            nextLevel(view);
        }

    }

    public void nextLevel(View view)
    {
        //symbolList.add(rand.nextInt(4));
        signalSequence(view);

    }

    public void stopGame()
    {

    }

    public void disableSymbolInput()
    {
        for (Object symbolButton : symbolButtons) ((SymbolButton) symbolButton).setClickable(false);

    }

    public void enableSymbolInput()
    {
        for (Object symbolButton : symbolButtons) ((SymbolButton) symbolButton).setClickable(true);

    }

    private enum State
    {
        STOPPED, SIGNALLING, WAITING
    }
}
